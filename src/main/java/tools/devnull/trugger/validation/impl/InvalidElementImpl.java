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

package tools.devnull.trugger.validation.impl;

import tools.devnull.trugger.HandlingException;
import tools.devnull.trugger.ValueHandler;
import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.validation.InvalidElement;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of an invalid element.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class InvalidElementImpl implements InvalidElement {

  private final Element element;
  private final Object invalidValue;
  private final Map<Class<? extends Annotation>, Annotation> violatedConstraints;

  public InvalidElementImpl(Element element, Object invalidValue) {
    this.element = element;
    this.invalidValue = invalidValue;
    this.violatedConstraints = new HashMap<>();
  }

  public InvalidElementImpl(Element element, Object invalidValue,
                     Collection<Annotation> violatedConstraints) {
    this.element = element;
    this.invalidValue = invalidValue;
    this.violatedConstraints = new HashMap<>();
    for (Annotation constraint : violatedConstraints) {
      this.violatedConstraints.put(constraint.annotationType(), constraint);
    }
  }

  @Override
  public Annotation violatedConstraint(Class<? extends Annotation> constraint) {
    return violatedConstraints.get(constraint);
  }

  @Override
  public boolean isConstraintViolated(Class<? extends Annotation> constraint) {
    return violatedConstraints.containsKey(constraint);
  }

  public void addViolatedConstraint(Class<? extends Annotation> constraint,
                             Annotation annotation) {
    violatedConstraints.put(constraint, annotation);
  }

  @Override
  public Object invalidValue() {
    return invalidValue;
  }

  @Override
  public Collection<Annotation> violatedConstraints() {
    return Collections.unmodifiableCollection(violatedConstraints.values());
  }

  @Override
  public Class declaringClass() {
    return element.declaringClass();
  }

  @Override
  public String name() {
    return element.name();
  }

  @Override
  public Class type() {
    return element.type();
  }

  @Override
  public boolean isReadable() {
    return element.isReadable();
  }

  @Override
  public boolean isWritable() {
    return element.isWritable();
  }

  @Override
  public ValueHandler in(Object target) {
    return element.in(target);
  }

  @Override
  public boolean isSpecific() {
    return element.isSpecific();
  }

  @Override
  public <E> E target() {
    return element.target();
  }

  @Override
  public <E> E value() throws HandlingException {
    return element.value();
  }

  @Override
  public void set(Object value) throws HandlingException {
    element.set(value);
  }

  @Override
  public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
    return element.getAnnotation(annotationClass);
  }

  @Override
  public Annotation[] getAnnotations() {
    return element.getAnnotations();
  }

  @Override
  public Annotation[] getDeclaredAnnotations() {
    return element.getDeclaredAnnotations();
  }
}
