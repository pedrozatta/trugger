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
package tools.devnull.trugger.reflection.impl;

import tools.devnull.trugger.reflection.ConstructorInvoker;
import tools.devnull.trugger.reflection.Reflection;
import tools.devnull.trugger.reflection.ReflectionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A default implementation for invoking {@link Constructor} objects.
 *
 * @author Marcelo Guimarães
 */
public class TruggerConstructorInvoker implements ConstructorInvoker {

  private final Constructor<?> constructor;

  public TruggerConstructorInvoker(final Constructor<?> constructor) {
    if (!constructor.isAccessible()) {
      Reflection.setAccessible(constructor);
    }
    this.constructor = constructor;
  }

  public <E> E withArgs(Object... args) {
    try {
      return (E) constructor.newInstance(args);
    } catch (InvocationTargetException e) {
      throw new ReflectionException(e.getCause());
    } catch (Exception e) {
      throw new ReflectionException(e);
    }
  }

  public <E> E withoutArgs() {
    return withArgs();
  }

}
