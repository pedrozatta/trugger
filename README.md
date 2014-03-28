# Overview

Trugger is a framework that helps you write code that anyone can read. The
idea is to provide a set of fluent interfaces to deal with operations related
to reflection (such as creating proxies, reflecting members and scanning
classes)

## What a hell does the name trugger mean?

When I was learning java, I came up with the name "Atatec" (Ataxexe
Technology) for my company (kidding, of couse) and then I send a message to my
friend Ives:

> Atatec

And the response was:

> trugger

When I ask him why he came up with this response, he said:

> Isn't that the name of that special kick in Street Fighter?

The special kick mentioned is the *Tatsumaki Senpuu Kyaku* and Ken says
something like "atatec trugger" (at least here in Brazil) when he does the kick.
So the name Trugger became my first choice when I start to write this framework.

# How To Use

Just put the jar file on your **classpath** and you're done. Trugger does not
require any dependency at runtime.

## About Maven Repository

Trugger currently is not in the Maven Central Repository, but a pom is generated
and distributed in the download so you can import it on your local repository.

# Reflection

The Reflection module was the first one to show up in Trugger, it allows simple
and much less verbose use of the Reflection API to reflect constructors,
methods, fields and generic type declarations.

The base class of this module is `org.atatec.trugger.reflection.Reflection`
and you will find a nice fluent interface there with the methods `reflect`,
`invoke` and `handle`.

## Fields

### Single

Reflecting a field is done with the code `reflect().field(field name)`. You can
also apply a filter by using the method `filter` and the target class (or
object) by using the method `in`. If you need a recursive search through the
entire target class hierarchy, just use the method `recursively`.

Here is some examples (assuming a `static import`):

```java
Field value = reflect().field("value").in(String.class);

Field name = reflect().field("name").recursively().in(MyClass.class);

Field id = reflect().field("id")
    .filter(field -> field.getType().equals(Long.class))
    .in(someInstance);
```

The `filter` method receive a `java.util.function.Predicate` to stay in touch
with Java 8.

### Multiple

If you need to reflect a set of fields, use the `reflect().fields()`. The same
features of a single reflect is available here.

~~~java
List<Field> stringFields = reflect().fields()
    .filter(field -> field.getType().equals(String.class))
    .recursively()
    .in(MyClass.class);
~~~

### Predicates

There are some builtin predicates in the class `org.atatec.trugger.reflection
.FieldPredicates`.

~~~java
List<Field> stringFields = reflect().fields()
    .filter(type(String.class)) // a static import
    .recursively()
    .in(MyClass.class);
~~~

### Handling Values

A field can have its value manipulated through the method `Reflection#handle`.
It will create a handler to set and get the field's value without the verbosity
of the Reflection API.

~~~java
//for static fields
String value = handle(field).value();
//for non static fields
String value = handle(field).in(instance).value();

//for static fields
handle(field).value("new value");
//for non static fields
handle(field).in(instance).value("new value");
~~~

You can also use a a `FieldSelector`:

~~~java
String value = handle(field("name")).in(instance).value();
~~~

## Constructors

### Single

### Multiple

### Predicates

### Invocation

## Methods

### Single

### Multiple

### Predicates

### Invocation

## Generic Type

# Class Scanning

## Basic Scanning

## Predicates

## Defining a Protocol Handler

# Proxy Creation

# Elements of an Object

## What is an Element?

## Creating a Custom Element Finder

# Utilities

## Annotation Mock

# Extending

## How To Implement the Fluent Interfaces

The fluent interfaces are always defined through java interfaces and may be
customized by your own implementation. Trugger uses a `ServiceLoader` to load
a factory that knows the implementations to instantiate,
so you can override the implementation of any fluent interface by defining a
file in your **META-INF/services** directory with the factory implementation.

The factory interfaces that can be customized are listed bellow:

- `org.atatec.trugger.element.ElementFactory`: used for reflecting elements
- `org.atatec.trugger.property.PropertyFactory`: used for reflecting properties
- `org.atatec.trugger.reflection.ReflectionFactory`: used for reflection in
    general
- `org.atatec.trugger.scan.ClassScannerFactory`: used for class scanning
- `org.atatec.trugger.interception.InterceptorFactory`: used for method
  interception