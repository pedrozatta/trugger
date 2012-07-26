/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package org.atatec.trugger.validation.validator;

import org.atatec.trugger.validation.Validator;

/**
 * Implementation of the {@link Length} validation.
 * 
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class LengthValidator implements Validator<CharSequence> {
  
  private Length annotation;
  
  public boolean isValid(@NotNull CharSequence value) {
    int length = annotation.trim() ? value.toString().trim().length() : value.length();
    
    return (length >= annotation.min()) && (length <= annotation.max());
  }
}