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

package tools.devnull.trugger.validation.validator;

import tools.devnull.trugger.validation.ValidatorClass;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that all items must be valid. This annotation applies to
 * collections arrays and map values.
 *
 * Note that this constraint will not report which items are invalid.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(ValidsValidator.class)
public @interface Valids {
}
