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
import tools.devnull.trugger.HandlingException;

import java.util.Properties;

import static tools.devnull.kodo.Spec.*;
import static tools.devnull.trugger.element.ElementPredicates.readable;
import static tools.devnull.trugger.element.ElementPredicates.writable;
import static tools.devnull.trugger.element.Elements.element;
import static tools.devnull.trugger.element.Elements.elements;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class PropertiesElementTest implements ElementSpecs {

  @Test
  public void propertiesElementTest() {
    Properties properties = new Properties();
    properties.setProperty("login", "admin");
    properties.setProperty("password", "admin");

    TestScenario.given(elements().in(properties))
        .it(should(have(elementsNamed("login", "password"))));

    TestScenario.given(elements().in(Properties.class))
        .it(should(be(EMPTY)));

    TestScenario.given(element("login").in(properties))
        .the(type(), should(be(String.class)))
        .the(name(), should(be("login")))
        .the(value(), should(be("admin")))
        .the(declaringClass(), should(be(Properties.class)))
        .it(should(be(readable())))
        .it(should(be(writable())))
        .when(valueIsSetTo("guest"))
        .the(value(), should(be("guest")))
        .and(properties.getProperty("login"), should(be("guest")))
        .then(settingValueTo(new Object()), should(raise(HandlingException.class)))
        .and(settingValueTo("", new Object()), should(raise(IllegalArgumentException.class)));
  }
}
