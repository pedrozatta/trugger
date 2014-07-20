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
package tools.devnull.trugger.test.general;

import static tools.devnull.trugger.util.Utils.areAssignable;
import static tools.devnull.trugger.util.Utils.isEmpty;
import static tools.devnull.trugger.util.Utils.objectClass;
import static tools.devnull.trugger.util.Utils.resolveType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import tools.devnull.trugger.test.Flag;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
@Flag
public class UtilsTest {

  @Test
  public void testIsEmpty() {
    assertTrue(isEmpty(""));
    assertTrue(isEmpty("  "));
    assertTrue(isEmpty(null));
    assertFalse(isEmpty(" a "));
  }

  @Test
  public void testResolveType() {
    assertEquals(String.class, resolveType(""));
    assertEquals(String.class, resolveType(String.class));
    assertEquals(Flag.class, resolveType(UtilsTest.class.getAnnotation(Flag.class)));
  }

  @Test
  public void testObjectClass() {
    assertEquals(Boolean.class, objectClass(boolean.class));
    assertEquals(Byte.class, objectClass(byte.class));
    assertEquals(Short.class, objectClass(short.class));
    assertEquals(Character.class, objectClass(char.class));
    assertEquals(Integer.class, objectClass(int.class));
    assertEquals(Long.class, objectClass(long.class));
    assertEquals(Float.class, objectClass(float.class));
    assertEquals(Double.class, objectClass(double.class));

    assertEquals(Object.class, objectClass(new Object()));
    assertEquals(Flag.class, objectClass(UtilsTest.class.getAnnotation(Flag.class)));
  }

  @Test
  public void testAssignable() {
    assertTrue(areAssignable(Boolean.class, boolean.class));
    assertTrue(areAssignable(Byte.class, byte.class));
    assertTrue(areAssignable(Short.class, short.class));
    assertTrue(areAssignable(Character.class, char.class));
    assertTrue(areAssignable(Integer.class, int.class));
    assertTrue(areAssignable(Long.class, long.class));
    assertTrue(areAssignable(Float.class, float.class));
    assertTrue(areAssignable(Double.class, double.class));

    assertTrue(areAssignable(CharSequence.class, StringBuilder.class));
    assertTrue(areAssignable(Annotation.class, Flag.class));
  }

}