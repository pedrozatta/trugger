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
package tools.devnull.trugger.element.impl;

import tools.devnull.trugger.HandlingException;
import tools.devnull.trugger.ValueHandler;
import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.element.UnwritableElementException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * @author Marcelo Guimarães
 */
public final class ResultSetElement extends AbstractElement implements Element {

  private static final Pattern DIGITS_PATTERN = Pattern.compile("\\d+");

  public ResultSetElement(String name) {
    super(name);
  }

  @Override
  public Class<?> declaringClass() {
    return ResultSet.class;
  }

  @Override
  public ValueHandler in(final Object target) {
    if (target instanceof ResultSet) {
      final ResultSet resultSet = (ResultSet) target;
      return new ValueHandler() {

        public void set(Object value) throws HandlingException {
          throw new UnwritableElementException(
              "Cannot write a value in a ResultSet");
        }

        public <E> E value() throws HandlingException {
          try {
            //if the name is the column index
            if (DIGITS_PATTERN.matcher(name).matches()) {
              return (E) resultSet.getObject(Integer.parseInt(name));
            }
            return (E) resultSet.getObject(name);
          } catch (SQLException e) {
            throw new HandlingException(e);
          }
        }
      };
    }
    throw new HandlingException("Target is not a " + ResultSet.class);
  }

  @Override
  public boolean isReadable() {
    return true;
  }

  @Override
  public boolean isWritable() {
    return false;
  }

}
