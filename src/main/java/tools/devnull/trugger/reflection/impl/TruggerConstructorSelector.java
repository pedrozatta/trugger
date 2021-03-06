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

import tools.devnull.trugger.reflection.ReflectionException;
import tools.devnull.trugger.selector.ConstructorSelector;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Predicate;

/**
 * A default implementation for the constructor selector.
 *
 * @author Marcelo Guimarães
 */
public class TruggerConstructorSelector implements ConstructorSelector {

  private final Predicate<? super Constructor<?>> predicate;
  private final Class[] parameterTypes;
  private final MemberFindersRegistry registry;

  public TruggerConstructorSelector(MemberFindersRegistry registry) {
    this.registry = registry;
    this.predicate = null;
    this.parameterTypes = null;
  }

  public TruggerConstructorSelector(MemberFindersRegistry registry,
                                    Predicate<? super Constructor<?>> predicate,
                                    Class[] parameterTypes) {
    this.predicate = predicate;
    this.parameterTypes = parameterTypes;
    this.registry = registry;
  }

  public ConstructorSelector filter(
      Predicate<? super Constructor<?>> predicate) {
    return new TruggerConstructorSelector(registry, predicate, parameterTypes);
  }

  public ConstructorSelector withParameters(Class<?>... parameterTypes) {
    return new TruggerConstructorSelector(registry, predicate, parameterTypes);
  }

  public ConstructorSelector withoutParameters() {
    return withParameters();
  }

  @Override
  public Constructor<?> in(Object target) throws ReflectionException {
    if (parameterTypes != null) {
      return (Constructor<?>)
          new MemberSelector(registry.constructorFinder(parameterTypes),
              predicate).in(target);
    }
    List<Constructor<?>> constructors =
        new MembersSelector<>(registry.constructorsFinder()).in(target);
    if (predicate != null) {
      return constructors.stream()
          .filter(predicate)
          .findAny().orElse(null);
    } else if (constructors.size() > 1) {
      throw new ReflectionException("More than one constructor found for " +
          target.getClass());
    } else {
      return constructors.iterator().next();
    }
  }

}
