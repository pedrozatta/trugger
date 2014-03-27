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
package org.atatec.trugger.test.element;

import org.atatec.trugger.Finder;
import org.atatec.trugger.Result;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.impl.TruggerElementSelector;
import org.atatec.trugger.selector.ElementSelector;
import org.atatec.trugger.test.Flag;
import org.junit.Test;

import static org.atatec.trugger.element.ElementPredicates.*;
import static org.atatec.trugger.test.TruggerTest.element;
import static org.atatec.trugger.util.mock.Mock.mock;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementSelectorTest {

  private Finder<Element> finder;
  private Element element;

  public ElementSelectorTest() {
    finder = createMock(Finder.class);
    Result<Element, Object> result = target -> element;
    expect(finder.find("name")).andReturn(result).anyTimes();
    replay(finder);
  }

  private ElementSelector select() {
    return new TruggerElementSelector("name", finder);
  }

  @Test
  public void testAnnotatedSelector() {
    element = mock(element().annotatedWith(Flag.class));
    assertSame(
        element,
        select().filter(annotatedWith(Flag.class)).in(this)
    );
    assertSame(
        element,
        select().filter(annotated()).in(this)
    );
    assertNull(
        select().filter(annotatedWith(Flag.class).negate()).in(this)
    );
    assertNull(
        select().filter(annotated().negate()).in(this)
    );
  }

  @Test
  public void testReadableSelector() {
    element = mock(element().readable());
    assertSame(
        element,
        select().filter(readable()).in(this)
    );
  }

  @Test
  public void testSpecificSelector() {
    element = mock(element().specific());
    assertSame(
        element,
        select().filter(specific()).in(this)
    );
  }

  @Test
  public void testWritableSelector() {
    element = mock(element().writable());
    assertSame(
        element,
        select().filter(writable()).in(this)
    );
  }

  @Test
  public void testNonWritableSelector() {
    element = mock(element().nonWritable());
    assertNull(
        select().filter(writable()).in(this)
    );
  }

  @Test
  public void testOfTypeSelector() {
    element = mock(element().ofType(String.class));
    assertSame(
        element,
        select().filter(type(String.class)).in(this)
    );
    assertNull(
        select().filter(type(Integer.class)).in(this)
    );
    assertNull(
        select().filter(type(CharSequence.class)).in(this)
    );

    element = mock(element().ofType(int.class));
    assertSame(
        element,
        select().filter(type(int.class)).in(this)
    );
    assertNull(
        select().filter(type(Integer.class)).in(this)
    );
  }

  @Test
  public void testAssignableToSelector() {
    element = mock(element().ofType(String.class));
    assertSame(
        element,
        select().filter(assignableTo(String.class)).in(this)
    );
    assertNull(
        select().filter(assignableTo(Integer.class)).in(this)
    );
    assertSame(
        element,
        select().filter(assignableTo(CharSequence.class)).in(this)
    );

    element = mock(element().ofType(int.class));
    assertSame(
        element,
        select().filter(assignableTo(Integer.class)).in(this)
    );
    assertSame(
        element,
        select().filter(assignableTo(int.class)).in(this)
    );
  }

}
