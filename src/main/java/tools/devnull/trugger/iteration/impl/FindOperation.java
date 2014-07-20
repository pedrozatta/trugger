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

package tools.devnull.trugger.iteration.impl;

import tools.devnull.trugger.iteration.NonUniqueMatchException;
import tools.devnull.trugger.predicate.Predicate;

import java.util.Collection;

/** @author Marcelo Guimarães */
public class FindOperation implements FindResult {

  private final Predicate predicate;

  public FindOperation(Predicate predicate) {
    this.predicate = predicate;
  }

  @Override
  public <E> E in(Collection<? extends E> collection) {
    E found = null;
    for (E e : collection) {
      if(predicate.evaluate(e)) {
        if(found != null) {
          throw new NonUniqueMatchException();
        }
        found = e;
      }
    }
    return found;
  }

}