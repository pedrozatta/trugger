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

package org.atatec.trugger.element;

import java.util.function.Function;

/**
 * Interface for defining a destination to a copy.
 *
 * @author Marcelo Guimarães
 * @since 4.1
 */
public interface CopyDestination {

  /**
   * Executes the given function to transform the elements before the copy.
   *
   * @param function the function to use.
   * @return a new object that uses the given function
   */
  CopyDestination applying(Function<ElementCopy, ?> function);

  /**
   * Copies only the elements that are not <code>null</code>.
   *
   * @return a new object that don't copy null values
   */
  CopyDestination notNull();

  /**
   * Copy the elements to the given object.
   *
   * @param dest the object to copy the elements.
   */
  void to(Object dest);

}