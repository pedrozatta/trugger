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

package org.atatec.trugger.test.validation;

import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorFactory;

import java.lang.annotation.Annotation;

import static org.atatec.trugger.util.mock.Mock.annotation;
import static org.atatec.trugger.util.mock.Mock.mock;

/**
 * @author Marcelo Guimarães
 */
public abstract class BaseValidatorTest {

  protected final ValidatorFactory factory = Validation.factory();

  protected final Validator validatorFor(Class<? extends Annotation> type) {
    return factory.create(mock(annotation(type)));
  }

}
