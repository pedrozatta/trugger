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

import tools.devnull.trugger.validation.ValidationEngine;
import tools.devnull.trugger.validation.ValidationFactory;
import tools.devnull.trugger.validation.ValidatorFactory;

/**
 * The default validation factory.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class TruggerValidationFactory implements ValidationFactory {

  private final ValidatorFactory factory = new TruggerValidatorFactory();

  @Override
  public ValidationEngine createValidationEngine() {
    return new TruggerValidationEngine(createValidatorFactory());
  }

  @Override
  public ValidatorFactory createValidatorFactory() {
    return factory;
  }

}
