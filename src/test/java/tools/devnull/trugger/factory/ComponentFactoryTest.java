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

package tools.devnull.trugger.factory;

import org.junit.Test;
import tools.devnull.trugger.ElementMock;
import tools.devnull.trugger.Flag;
import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.util.factory.ComponentFactory;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.*;
import static tools.devnull.trugger.AnnotationMock.mockAnnotation;

public class ComponentFactoryTest {

  @Test
  public void testCreate() {
    ToString annotation = mockAnnotation(ToString.class);

    ComponentFactory<ConverterClass, Converter> factory
        = new ComponentFactory<>(ConverterClass.class);

    Converter converter = factory.create(annotation);
    assertEquals("10", converter.convert(10));
    assertEquals("null", converter.convert(null));
  }

  @Test
  public void testListCreate() {
    Element element = new ElementMock()
        .annotatedWith(mockAnnotation(Flag.class))
        .annotatedWith(mockAnnotation(ToNull.class))
        .annotatedWith(mockAnnotation(Resource.class))
        .annotatedWith(mockAnnotation(ToString.class))
        .createMock();

    ComponentFactory<ConverterClass, Converter> factory
        = new ComponentFactory<>(ConverterClass.class);

    List<Converter> converters = factory.createAll(element);
    assertEquals(2, converters.size());
  }

  @Test
  public void testElementCreate() {
    Element element = new ElementMock()
        .annotatedWith(mockAnnotation(Resource.class))
        .annotatedWith(mockAnnotation(ToString.class))
        .annotatedWith(mockAnnotation(ToNull.class))
        .annotatedWith(mockAnnotation(Flag.class))
        .createMock();

    ComponentFactory<ConverterClass, Converter> factory
        = new ComponentFactory<>(ConverterClass.class);

    Converter converter = factory.create(element);
    assertNotNull(converter);

    element = new ElementMock()
        .annotatedWith(mockAnnotation(Resource.class))
        .annotatedWith(mockAnnotation(Flag.class))
        .createMock();

    converter = factory.create(element);
    assertNull(converter);
  }

}
