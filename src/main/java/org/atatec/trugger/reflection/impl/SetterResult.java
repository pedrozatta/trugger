/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger.reflection.impl;

import org.atatec.trugger.Result;

import java.lang.reflect.Method;
import java.util.Set;

import static org.atatec.trugger.reflection.MethodPredicates.takes;

/**
 * @author Marcelo Guimarães
 */
public class SetterResult implements Result<Method, Object> {

  private final Result<Set<Method>, Object> selector;
  private final Class<?> type;

  public SetterResult(Result<Set<Method>, Object> selector, Class<?> type) {
    this.selector = selector;
    this.type = type;
  }

  public Method in(Object target) {
    Set<Method> methods = selector.in(target);
    return methods.stream().filter(takes(type)).findAny().orElse(null);
  }

}
