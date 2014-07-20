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
package tools.devnull.trugger.reflection.impl;

import tools.devnull.trugger.Result;
import tools.devnull.trugger.predicate.Predicate;
import tools.devnull.trugger.reflection.ReflectionPredicates;
import tools.devnull.trugger.selector.SetterMethodSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Default implementation for the setter method selector.
 *
 * @author Marcelo Guimarães
 */
public class TruggerSetterMethodSelector implements SetterMethodSelector {

  private final MembersSelector<Method> selector;

  public TruggerSetterMethodSelector(String name, MembersFinder<Method> finder) {
    this.selector = new MembersSelector<Method>(new SettersFinder(name, finder));
  }

  public SetterMethodSelector annotated() {
    selector.builder().add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public SetterMethodSelector notAnnotated() {
    selector.builder().add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public SetterMethodSelector annotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }

  public SetterMethodSelector notAnnotatedWith(Class<? extends Annotation> type) {
    selector.builder().add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }

  public SetterMethodSelector that(Predicate<? super Method> predicate) {
    selector.builder().add(predicate);
    return this;
  }

  public Set<Method> in(Object target) {
    return selector.in(target);
  }

  public SetterMethodSelector recursively() {
    selector.useHierarchy();
    return this;
  }

  public Result<Method, Object> forType(final Class<?> type) {
    return new SetterResult(selector, type);
  }

}