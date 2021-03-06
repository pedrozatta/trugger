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
package tools.devnull.trugger.element;

import org.junit.Test;
import tools.devnull.kodo.TestScenario;

import java.util.Map;

import static tools.devnull.kodo.Spec.raise;
import static tools.devnull.kodo.Spec.should;
import static tools.devnull.trugger.element.Elements.element;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class NonSpecificElementTest implements ElementSpecs {

  private Element nonSpecificElement =
      element("nonSpecificElement").in(Map.class);

  @Test
  public void testRead() throws Exception {
    TestScenario.given(nonSpecificElement)
        .then(attempToGetValue(), should(raise(NonSpecificElementException.class)));
  }

  @Test
  public void testWrite() throws Exception {
    TestScenario.given(nonSpecificElement)
        .then(attempToChangeValue(), should(raise(NonSpecificElementException.class)));
  }

}
