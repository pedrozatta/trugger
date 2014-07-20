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
package tools.devnull.trugger.iteration;

import tools.devnull.trugger.iteration.impl.FindResult;
import tools.devnull.trugger.predicate.Predicate;

/**
 * Interface that defines a factory for iteration operations.
 *
 * @author Marcelo Guimarães
 * @since 2.4
 */
public interface IterationFactory {

  /**
   * Creates the implementation for retaining elements based on giving conditions.
   *
   * @return the component for this operation.
   */
  IterationSourceSelector createRetainOperation(Predicate predicate);

  /**
   * Creates the implementation for removing elements based on giving conditions.
   *
   * @return the component for this operation.
   */
  IterationSourceSelector createRemoveOperation(Predicate predicate);

  /**
   * Creates the implementation for moving elements based on giving conditions.
   *
   * @return the component for this operation.
   */
  SourceSelector createMoveOperation(Predicate predicate);

  /**
   * Creates the implementation for copying elements based on giving conditions.
   *
   * @return the component for this operation.
   */
  SourceSelector createCopyOperation(Predicate predicate);

  /**
   * Creates the implementation for searching elements.
   *
   * @return the component for this operation
   */
  FindResult createFindOperation(Predicate predicate);

  /**
   * Creates the implementation for searching elements.
   *
   * @return the component for this operation
   */
  FindResult createFindFirstOperation(Predicate predicate);

  /**
   * Creates the implementation for searching elements.
   *
   * @return the component for this operation
   */
  FindAllResult createFindAllOperation(Predicate predicate);

}