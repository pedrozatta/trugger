# Validation

## Validating an Object

### Basic Validation

To validate an object, you can simply use the Validation class and the provided DSL:

    ValidationResult result = validation().validate(object);

The result will give all information needed to integrate the validation to almost all
frameworks and architectures. (You can access messages, values and use reflection and
other DSLs using the invalid elements.)

You can also give a resource bundle to the Validation constructor for externalize the
validation messages. The provided constraints uses a key following the pattern
**validation.** plus the Annotation name (**validation.NotNull** for *NotNull*
constraint).

### Validation using Element selectors

You can pass a selector for restrict the elements to validate:

    ValidationResult result = validation().validate(
        elements().ofType(String.class
    ).in(object));

### Validation using context

Context are useful for defining different validations for the same object (like simple and
full validations). You can define a context using *forContext* method:

    ValidationResult result = validation().forContext("context_name")
      .validate(object);

The contexts can be defined using the *context* property in the constraint annotations.
All constraints without contexts will be used no matter what context was defined.

## Creating Validators

### Basic Validators

The validation constraints are defined using two components: an Annotation for defining
the constraint and a Validator to implement it.

Example: A basic validator for null objects.

    @Retention(RetentionPolicy.RUNTIME)
    @ValidatorClass(NotNullValidator.class)
    public @interface NotNull { }

    public class NotNullValidator implements Validator<Object> {

      public boolean isValid(Object object) {
        return object != null;
      }

    }

You can now annotate a field or a getter method:

    public class Person {

      @NotNull
      private String name;

      //...

    }

### Type Validators

You can create validators for a specific type or a set of types using the validator
generic type or the annotations @AcceptedTypes, @AcceptedArrayTypes or @AcceptArrays.

Example: a regex validator.

    @Retention(RetentionPolicy.RUNTIME)
    @ValidatorClass(PatternValidator.class)
    public @interface Pattern {

      String value();

    }

    public class PatternValidator implements Validator<CharSequence> {

      private Pattern annotation; //the annotation value will be injected here

      public boolean isValid(CharSequence value) {
        if(value != null) {
          return true;
        }
        java.util.regex.Pattern pattern =
          java.util.regex.Pattern.compile(annotation.value());
        return pattern.matcher(value).matches();
      }
    }

In this example, everything that is a *CharSequence* can be validated with *@Pattern*. The
same effect can be reached using *@AcceptedTypes*:

    @AcceptedTypes(CharSequence.class)
    public class PatternValidator implements Validator<Object> {

or

    @AcceptedTypes({String.class, StringBuilder.class})
    public class PatternValidator implements Validator<Object> {

The second form will work only for *String* and *StringBuilder* objects. The same
restriction can be used for arrays with *@AcceptedArrayTypes* for defining the array types
or *@AcceptArrays* to accept every array type.

Note that the validator doesn't have an initialize method that takes the annotation (like
in Hibernate Validator). The annotation will be injected automatically in the field with
the same type. (You can use *@TargetAnnotation* if you want to force the injection to
another compatible field - like *Object* or *Annotation*.)

### Validation of arguments

Take a look at the first instruction in the validation method of PatternValidator. That
check can be a pain if you are going to create a extensive set of validators. To avoid
that you can use validations in parameter declared in *isValid* method.

    public boolean isValid(@NotNull CharSequence value) {
      java.util.regex.Pattern pattern =
        java.util.regex.Pattern.compile(annotation.value());
      return pattern.matcher(value).matches();
    }

Much more elegant!

You can use almost all validations in there (almost all because there is a special type of
validators that use binds, and they cannot be used here) and as much as needed.

### Binds

#### Reference binds

Suppose you have two dates and one must be after the other.

    public class Ticket {

      private Date leaving;

      private Date arrival;

      //...

    }

Validating the *arrival* field will require access to the *leaving* field. This can be
done by using *References* in the annotation. Let's look the *@After* validation:

    @Retention(RetentionPolicy.RUNTIME)
    @ValidatorClass(AfterValidator.class)
    public @interface After {

      @NotNull
      @Reference("referenceValue")
      String value();

    }

    public class AfterValidator implements Validator<Date> {

      private Date referenceValue; //same name as declared in @After "value" property
      private After annotation; //injected automatically

      public boolean isValid(@NotNull Date value) {
        return value.after(referenceValue);
      }

    }

Notice that the references can be validated as well as the arguments in validator. If the
reference is invalid the validation will not be processed.

The class will now looks like this:

    public class Ticket {

      @NotNull
      private Date leaving;

      @NotNull
      @After("leaving")
      private Date arrival;

      //...

    }

#### Target bind

You can bind the target object in validator using the annotation *@TargetObject*.

#### Elements bind

You can bind any element (fields, properties, ...) from the target object using
*@TargetElement* annotation. If you don't define the element name, the current element
being validated will be injected.

#### Bridge bind

If you need a way to interact with the *ValidationEngine*, just declare an element of type
*ValidationBridge* and the bridge will be injected here. A *ValidationBridge* is useful
for recursive validations (you can create validators using the same configurations and add
invalid elements).

#### Context bind

The current context of the validation will be injected in elements annotated with
*ValidationContext*.

#### Validator bind

If you need a validation that depends on other validators, you can declare the constraints
in elements of type Validator and a proper validator will be injected on it.

Example:

    public class MyValidator implements Validator<Double> {

      @Range(min = 5, max = 15, includeMin = false)
      private Validator range;

      public boolean isValid(@NotNull Double value) {
        if(range.isValid(value) {
          //do a proper validation
        } else {
          //do a proper validation
        }
      }

    }

### Domain Validations

Supose you have some properties that requires more than one validation. You can group then
into a single constraint (a domain constraint) using the *GroupValidator* as the
*@ValidatorClass*.

Example:

    @NotNull
    @After("leaving")
    @Retention(RetentionPolicy.RUNTIME)
    @ValidatorClass(GroupValidator.class)
    public @interface ArrivalDate {}

    public class Ticket {

      @NotNull
      private Date leaving;

      @ArrivalDate
      private Date arrival;

      //...

    }

## Validation Messages

### Message Resolving

By default, every *Validator* can have a validation message mapped in the annotation with
the property named **message**. If the annotation doesn't have a *message* property, then
its name will be used

The message can be a ResourceBundle key (and the ResourceBundle used will be the one
passed to the Validation instance) or a literal message.

    @NotNull(message = "Name required!")
    private String name;

or

    @NotNull(message = "name.required")
    private String name;

The annotation now has a *String* property:

    @Retention(RetentionPolicy.RUNTIME)
    @ValidatorClass(NotNullValidator.class)
    public @interface NotNull {
      String message() default "";
    }

### Message Formatting

You can format a message using values from constraints and invalid elements.

In case of an *Element*, you can use the following attributes:

* *name* - the name of the element
* *type* - the type of the element
* *value* - the value of the element

To use a constraint value in the message, just put the desired name of the element between
**${** and **}**. You can also use the reference elements.

There is a way to format the message using a *java.util.Formatter*. Just put the arguments
between **@{** and **}**

Examples:

    @After(reference = "date1", message = "Must be after ${reference.value}"
    private Date date2;

Will use *date1* value in the message.


    @After(reference = "date1", message = "{value} is not after ${reference.value}"
    private Date date2;

Will use both *date1* value and *date2* value.


    @After(reference = "date1", message = "#{name} is not after #{reference.name}"
    private Date date2;

Will search for keys **date2** and **date1** in the *ResourceBundle* passed to the
*Validation* instance and their values will be used in the message.


    @Range(min = 0, max = 1,
     message = "@{%.3f,{value}} is out of range [@{%.3f,${min}} ; @{%.3f,${max}}]"
    private double rate;

For a *6.789* value, the message will be "6.789 is out of range [0.000 ; 1.000]".