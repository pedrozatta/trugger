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
package org.atatec.trugger.test.element;

import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.atatec.trugger.test.TruggerTest.assertThrow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.atatec.trugger.HandlingException;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.ElementFactory;
import org.atatec.trugger.loader.ImplementationLoader;
import org.atatec.trugger.loader.impl.TruggerRegistry;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class AnnotationElementTest {

  private Element getAnnotationElement(String name) {
    //doing this we make sure that two properties having the same name will have diferent IDs
    ElementFactory factory = new ImplementationLoader(new TruggerRegistry()).get(ElementFactory.class);
    return factory.createElementSelector(name).in(TestAnnotation.class);
  }

  @TestAnnotation(bool = false, name = "name", number = 1)
  private class AnnotationTestClass {}

  @Test
  public void annotationElementTest() {
    Element element1 = getAnnotationElement("name");
    Element element2 = getAnnotationElement("name");
    assertNotNull(element1);
    assertAnnotationElement(element1);
    assertFalse(element1 == element2);
    assertEquals(element1.hashCode(), element2.hashCode());
    assertTrue(element1.equals(element2));
    assertEquals(element1, element2);

    Collection<Element> elements = elements().in(TestAnnotation.class);
    for (Element prop : elements) {
      assertAnnotationElement(prop);
    }
    element2 = getAnnotationElement("bool");
    assertFalse(element1.equals(element2));

    TestAnnotation annotation = AnnotationTestClass.class.getAnnotation(TestAnnotation.class);
    final Element specific = element("bool").in(annotation);
    assertEquals(boolean.class, specific.type());
    assertEquals(false, specific.value());
    assertThrow(new Runnable(){
      public void run() {
        specific.value(true);
      }
    }, HandlingException.class);
  }

  private void assertAnnotationElement(Element element) {
    assertNotNull(element);
    assertTrue(Annotation.class.isAssignableFrom(element.declaringClass()));
    assertTrue(element.isReadable());
    assertFalse(element.isWritable());
  }

  @Test
  public void testNullSpecificElement() {
    TestAnnotation annotation = AnnotationTestClass.class.getAnnotation(TestAnnotation.class);
    Element el = element("non_existent").in(annotation);
    assertNull(el);
  }

}