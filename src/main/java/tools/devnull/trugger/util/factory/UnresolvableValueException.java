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

package tools.devnull.trugger.util.factory;

import tools.devnull.trugger.TruggerException;

/**
 * Exception thrown to indicate that the context cannot resolve a value.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class UnresolvableValueException extends TruggerException {

  public UnresolvableValueException() {
  }

  public UnresolvableValueException(String message) {
    super(message);
  }

  public UnresolvableValueException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnresolvableValueException(Throwable cause) {
    super(cause);
  }
}
