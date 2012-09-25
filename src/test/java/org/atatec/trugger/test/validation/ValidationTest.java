/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package org.atatec.trugger.test.validation;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.selector.ElementSelector;
import org.atatec.trugger.validation.InvalidElement;
import org.atatec.trugger.validation.ValidationResult;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.validation.Validation.validation;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Varella Barca Guimarães.
 */
public class ValidationTest {
  
  @Test
  public void validObjectTest() {
    Calendar cal = Calendar.getInstance();
    Date parting = cal.getTime();
    cal.add(Calendar.SECOND, 1);
    Date arrival = cal.getTime();
    
    Ticket ticket = new Ticket(new Owner("Marcelo"), parting, arrival, new City("Brasília"), new City("Natal"));
    assertTrue(validation().validate(ticket).isValid());
    
    ticket = new Ticket(new Owner("Marcelo"), parting, parting, new City("Brasília"), new City("Natal"));
    assertTrue(validation().validate(element("owner").in(ticket)).isValid());
  }
  
  @Test
  public void invalidObjectTest() {
    Calendar cal = Calendar.getInstance();
    Date parting = cal.getTime();
    cal.add(Calendar.SECOND, -1);
    Date invalidArrival = cal.getTime();
    cal.add(Calendar.SECOND, 2);
    Date arrival = cal.getTime();
    
    ElementSelector selection = Elements.element("owner");
    
    Ticket ticket = new Ticket(new Owner("Marcelo"), parting, invalidArrival, new City("Brasília"), new City("Natal"));
    ValidationResult result = validation().validate(ticket);
    assertInvalid(result, "parting", "arrival", "before", "after");
    
    ticket = new Ticket(new Owner("Marcelo"), parting, parting, new City("Brasília"), new City("Natal"));
    result = validation().validate(ticket);
    assertInvalid(result, "parting", "arrival");
    
    ticket = new Ticket(new Owner("Marcelo"), parting, null, new City(null), new City("Natal"));
    result = validation().validate(ticket);
    assertInvalid(result, "route", "routeMap", "routeArray", "arrival", "after");
    
    ticket = new Ticket(new Owner(null), parting, arrival, new City("Brasília"), new City("Natal"));
    result = validation().validate(ticket);
    assertInvalid(result, "owner", "owner.name");
    
    ticket = new Ticket(null, parting, arrival, new City("Brasília"), new City("Natal"));
    result = validation().validate(ticket);
    assertInvalid(result, "owner");
    result = validation().validate(element("owner").in(ticket));
    assertInvalid(result, "owner");
    
    ticket = new Ticket(new Owner("Marcelo"), arrival, parting);
    result = validation().validate(ticket);
    assertInvalid(result, "route", "routeMap", "routeArray", "parting", "arrival", "before", "after");
  }
  
  private void assertInvalid(ValidationResult result, String... elements) {
    assertFalse(result.isValid());
    Collection<InvalidElement> invalidElements = result.invalidElements();
    for (String name : elements) {
      InvalidElement invalidElement = result.invalidElement(name);
      assertNotNull(invalidElement);
      assertTrue(invalidElements.contains(invalidElement));
    }
    assertEquals(elements.length, invalidElements.size());
    Set<String> set = new HashSet<String>(Arrays.asList(elements));
    Element element;
    for (InvalidElement invalidProperty : invalidElements) {
      assertTrue(set.contains(invalidProperty.path()));
      element = Elements.element(invalidProperty.path()).in(result.target());
      if (invalidProperty.path().indexOf('.') > 0) {
        assertEquals(element.name(), invalidProperty.path());
      } else {
        assertEquals(element.name(), invalidProperty.name());
      }
      assertEquals(element.declaringClass(), invalidProperty.declaringClass());
      assertEquals(element.isReadable(), invalidProperty.isReadable());
      assertEquals(element.isWritable(), invalidProperty.isWritable());
      assertArrayEquals(element.getDeclaredAnnotations(), invalidProperty.getDeclaredAnnotations());
      Annotation[] annotations = element.getAnnotations();
      assertArrayEquals(annotations, invalidProperty.getAnnotations());
      for (Annotation annotation : annotations) {
        Class<? extends Annotation> type = annotation.annotationType();
        assertTrue(element.isAnnotationPresent(type));
        assertTrue(invalidProperty.isAnnotationPresent(type));
      }
    }
  }
  
}
