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
import tools.devnull.trugger.Flag;
import tools.devnull.trugger.HandlingException;

import static tools.devnull.kodo.Spec.*;
import static tools.devnull.trugger.element.ElementPredicates.*;
import static tools.devnull.trugger.element.Elements.element;

/**
 *
 */
public class ObjectElementTest implements ElementSpecs {

  class HandlingTestObject {

    public void setName(String name) {
      throw new RuntimeException();
    }

    public String getInfo() {
      throw new RuntimeException();
    }
  }

  @Test
  public void testHandlingExceptions() {
    TestScenario.given(element("name").in(new HandlingTestObject()))
        .it(should(notBe(readable())))
        .then(attempToGetValue(), should(raise(UnreadableElementException.class)))
        .and(attempToChangeValue(), should(raise(HandlingException.class)));

    TestScenario.given(element("info").in(new HandlingTestObject()))
        .it(should(notBe(writable())))
        .then(attempToChangeValue(), should(raise(UnwritableElementException.class)))
        .and(attempToChangeValue(), should(raise(HandlingException.class)));
  }

  class AnnotationPrecedenceTest {

    @Flag
    private String name;
    private String phone;
    private String address;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Flag
    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getAddress() {
      return address;
    }

    @Flag
    public void setAddress(String address) {
      this.address = address;
    }
  }

  @Test
  public void testAnnotationPrecedence() {
    TestScenario.given(AnnotationPrecedenceTest.class)
        .the(elementNamed("name"), should(be(annotatedWith(Flag.class))))
        .the(elementNamed("phone"), should(be(annotatedWith(Flag.class))))
        .the(elementNamed("address"), should(be(annotatedWith(Flag.class))));
  }

  class TypeTest {
    private String name;

    private String phone;

    private String address;

    public String getPhone() {
      return null;
    }

    public void setPhone(String phone) {

    }

    public void setAddress(String address) {

    }
  }

  @Test
  public void testType() {
    TestScenario.given(TypeTest.class)
        .the(elementNamed("name"), should(be(ofType(String.class))))
        .the(elementNamed("phone"), should(be(ofType(String.class))))
        .the(elementNamed("address"), should(be(ofType(String.class))));
  }

}
