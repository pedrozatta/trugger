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
package tools.devnull.trugger.element.impl;

import tools.devnull.trugger.HandlingException;
import tools.devnull.trugger.ValueHandler;
import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.element.UnwritableElementException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class that represents an Annotation property.
 *
 * @author Marcelo Guimarães
 */
public final class AnnotationElement extends AbstractElement implements Element {

  private Method method;

  /**
   * Creates a new AnnotationProperty based on the given method.
   *
   * @param method
   *          the method that allows access to the property.
   */
  public AnnotationElement(Method method) {
    super(method.getName());
    this.method = method;
    this.annotatedElement = method;
  }

  @Override
  public Class<?> type() {
    return method.getReturnType();
  }

  public ValueHandler in(final Object target) {
    return new ValueHandler() {

      public <E> E value() throws HandlingException {
        try {
          return (E) method.invoke(target);
        } catch (InvocationTargetException e) {
          throw new HandlingException(e.getCause());
        } catch (Exception e) {
          throw new HandlingException(e);
        }
      }

      public void set(Object value) throws HandlingException {
        throw new UnwritableElementException(name());
      }

    };
  }

  public Class<?> declaringClass() {
    return method.getDeclaringClass();
  }

  public boolean isReadable() {
    return true;
  }

  public boolean isWritable() {
    return false;
  }

}
