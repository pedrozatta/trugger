/*
 * Copyright 2009-2014 Marcelo Guimarães
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.devnull.trugger.validation;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.trugger.Flag;
import tools.devnull.trugger.validation.validator.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static tools.devnull.trugger.AnnotationMock.mockAnnotation;
import static tools.devnull.trugger.element.ElementPredicates.ofType;

/**
 * @author Marcelo Guimarães
 */
public class ValidationTest extends BaseValidatorTest {

  @NotNull
  @Valid
  @MergeElements
  @ValidatorClass(DomainValidator.class)
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface ValidAndNotNull {

  }

  //no getters and setters...keeping it simple

  public static class Product {

    @NotNull
    @NotEmpty
    public String description;

    @Min(0)
    public double price;

  }

  public static class Item {

    @ValidAndNotNull
    public Product product;

    @Min(1)
    public int quantity;

    @Flag
    public double total() {
      return product.price * quantity;
    }

  }

  public static class Purchase {

    @NotNull
    @NotEmpty
    public String number;

    @NotNull
    @Valids
    public List<Item> items = new ArrayList<>();

    @Valid
    @NotNull
    public Customer customer;

  }

  public static class Customer {

    @NotNull
    public String name;

    @NotNull
    public String address;

    @NotNull
    public String phone;

    @NotNull
    public String email;

  }

  Customer invalidCustomer;
  Purchase invalidPurchase;
  Item invalidItem;
  Product invalidProduct;

  Customer validCustomer;
  Purchase validPurchase;
  Item validItem;
  Product validProduct;

  @Before
  public void initialize() {
    invalidCustomer = new Customer();
    invalidCustomer.name = "Marcelo";
    invalidCustomer.phone = "9013901031";

    validCustomer = new Customer();
    validCustomer.name = "Marcelo";
    validCustomer.phone = "9013901031";
    validCustomer.email = "myemail@server.com";
    validCustomer.address = "some address";

    invalidPurchase = new Purchase();
    invalidPurchase.customer = invalidCustomer;

    validProduct = new Product();
    validProduct.price = 25.90;
    validProduct.description = "Thing";

    invalidProduct = new Product();
    invalidProduct.price = 0;
    invalidProduct.description = "";

    validItem = new Item();
    validItem.quantity = 1;
    validItem.product = validProduct;

    invalidItem = new Item();
    invalidItem.quantity = 0;
    invalidItem.product = invalidProduct;

    validPurchase = new Purchase();
    validPurchase.customer = validCustomer;
    validPurchase.items.add(validItem);
    validPurchase.number = "AB201030ADBA00113";

    invalidPurchase = new Purchase();
    invalidPurchase.customer = invalidCustomer;
    invalidPurchase.items.add(validItem);
    invalidPurchase.items.add(invalidItem);
  }

  private Validator validatorFor(Class<? extends Annotation> type) {
    return Validation.factory().create(mockAnnotation(type));
  }

  @Test
  public void testInvalidSimpleValidation() {
    ValidationResult result = Validation.engine().validate(invalidCustomer);
    assertTrue(result.isInvalid());

    assertTrue(result.isElementInvalid("email"));
    assertTrue(result.isElementInvalid("address"));

    assertFalse(result.isElementInvalid("name"));
    assertFalse(result.isElementInvalid("phone"));

    assertEquals(2, result.invalidElements().size());

    InvalidElement invalidElement = result.invalidElement("address");
    assertNotNull(invalidElement);
    assertEquals(null, invalidElement.invalidValue());

    testResultElements(result);

    invalidCustomer.address = "New address set";
    assertEquals(null, invalidElement.invalidValue());
    assertTrue(invalidElement.isConstraintViolated(NotNull.class));
  }

  @Test
  public void testInvalidDeepValidation() {
    ValidationResult result = Validation.engine().validate(invalidPurchase);

    assertTrue(result.isInvalid());
    assertTrue(result.isElementInvalid("customer"));
    assertTrue(result.isElementInvalid("customer.address"));
    assertTrue(result.invalidElement("customer")
        .isConstraintViolated(Valid.class));
    assertTrue(result.invalidElement("customer.address")
        .isConstraintViolated(NotNull.class));

    testResultElements(result);
  }

  @Test
  public void testInvalidArgumentsValidator() {
    ValidationResult result = Validation.engine().validate(new Purchase());

    assertTrue(result.isElementInvalid("customer"));
    assertFalse(result.invalidElement("customer").isConstraintViolated(Valid.class));

    testResultElements(result);
  }

  @Test
  public void testInvalidDomainValidator() {
    Item item = new Item();
    ValidationResult result = Validation.engine().validate(item);
    assertTrue(result.isInvalid());
    assertTrue(result.isElementInvalid("product"));
    assertTrue(result.invalidElement("product")
        .isConstraintViolated(ValidAndNotNull.class));

    testResultElements(result);

    item.product = new Product();
    result = Validation.engine().validate(item);
    assertTrue(result.isInvalid());
    assertTrue(result.isElementInvalid("product"));
    assertTrue(result.invalidElement("product")
        .isConstraintViolated(ValidAndNotNull.class));

    assertTrue(result.isElementInvalid("product.description"));
    assertTrue(result.invalidElement("product.description")
        .isConstraintViolated(NotNull.class));
    assertNotNull(result.invalidElement("product.description")
        .violatedConstraint(NotNull.class));

    testResultElements(result);
  }

  @Test
  public void testValidDomainValidator() {
    ValidationResult result = Validation.engine().validate(validItem);

    assertFalse(result.isInvalid());
    assertTrue(result.invalidElements().isEmpty());
  }

  @Test
  public void testValidValidator() {
    Validator validator = validatorFor(Valid.class);
    assertFalse(validator.isValid(invalidCustomer));
    assertTrue(validator.isValid(validItem));
  }

  @Test
  public void testValidsValidator() {
    Validator validator = validatorFor(Valids.class);

    List list = new ArrayList<>();
    list.add(validItem);
    list.add(invalidCustomer);
    assertFalse(validator.isValid(list));
    list.remove(1);
    assertTrue(validator.isValid(list));

    Object[] objects = new Object[]{validItem, invalidCustomer};
    assertFalse(validator.isValid(objects));
    objects[1] = validProduct;
    assertTrue(validator.isValid(objects));

    Map<String, Object> map = new HashMap<>();
    map.put("item", validItem);
    map.put("customer", invalidCustomer);
    assertFalse(validator.isValid(map));
    map.remove("customer");
    assertTrue(validator.isValid(map));

    assertTrue(validator.isValid(null));
  }

  @Test
  public void testValidation() {
    ValidationResult result = Validation.engine().validate(validPurchase);
    assertFalse(result.isInvalid());
    assertSame(validPurchase, result.target());

    result = Validation.engine().validate(invalidPurchase);
    assertTrue(result.isInvalid());
    assertSame(invalidPurchase, result.target());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiTypeNotConfigured() {
    new MultiTypeValidator().isValid(new Object());
  }

  @Test
  public void testFilterForward() {
    ValidationEngine engine = Validation.engine()
        .filter(ofType(String.class).or(ofType(Item.class)));
    Purchase purchase = new Purchase();
    purchase.number = "01309429";
    purchase.items.add(new Item());
    purchase.items.add(new Item());
    purchase.items.add(new Item());
    purchase.items.add(new Item());

    assertFalse(engine.validate(purchase).isInvalid());
  }

  @MergeElements
  @ValidatorClass(MyValidator.class)
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface MyConstraint {

  }

  public static class MyValidator implements Validator {

    private final ValidationEngine engine;

    public MyValidator(ValidationEngine engine) {
      this.engine = engine;
    }

    @Override
    public boolean isValid(@NotNull Object value) {
      ValidationResult result = engine.filter(el -> false).validate(value);
      return !result.isInvalid();
    }
  }

  @Test
  public void testFilterOverride() {
    Object test = new Object() {
      @MyConstraint
      Customer customer = new Customer();
    };
    assertFalse(Validation.engine().validate(test).isInvalid());
  }

}
