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
package tools.devnull.trugger.scan;

import tools.devnull.trugger.selector.ClassesSelector;

/**
 * Interface that defines a class capable of scanning other classes in the
 * classpath.
 *
 * @author Marcelo Guimarães
 * @since 2.3
 */
public interface ClassScanner {

  /**
   * Finds classes in a package
   *
   * @return a selector for the operation.
   */
  ClassesSelector classes();

  /**
   * Sets the class loader to use.
   *
   * @param classLoader the class loader to use.
   */
  ClassScanner with(ClassLoader classLoader);

}
