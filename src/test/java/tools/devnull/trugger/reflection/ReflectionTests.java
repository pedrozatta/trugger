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
package tools.devnull.trugger.reflection;

import org.junit.Test;
import tools.devnull.trugger.reflection.impl.TruggerFieldsSelector;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static tools.devnull.trugger.TruggerTest.assertThrow;
import static tools.devnull.trugger.reflection.Reflection.reflect;
import static tools.devnull.trugger.reflection.Reflection.wrapperFor;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ReflectionTests {

  @Test
  public void testWrapperResolver() {
    assertEquals(Boolean.class, wrapperFor(boolean.class));
    assertEquals(Byte.class, wrapperFor(byte.class));
    assertEquals(Short.class, wrapperFor(short.class));
    assertEquals(Character.class, wrapperFor(char.class));
    assertEquals(Integer.class, wrapperFor(int.class));
    assertEquals(Long.class, wrapperFor(long.class));
    assertEquals(Float.class, wrapperFor(float.class));
    assertEquals(Double.class, wrapperFor(double.class));
  }

  private static class TestObject {

    public TestObject(Collection c) {
    }

    public TestObject(int i) {
    }

    public TestObject() {
    }

  }

  private class GenericClass<E> {
  }

  @Test
  public void testGenericTypeResolver() throws Exception {
    final Map<String, Integer> map = new HashMap<String, Integer>() {
    };
    assertEquals(String.class, reflect().genericType("K").in(map));
    assertEquals(Integer.class, reflect().genericType("V").in(map));

    GenericClass<String> genericClass = new GenericClass<String>() {
    };
    assertEquals(String.class, reflect().genericType("E").in(genericClass));
    assertEquals(String.class, reflect().genericType().in(genericClass));

    assertThrow(ReflectionException.class, () -> {
      reflect().genericType().in(map);
    });

    assertThrow(ReflectionException.class, () -> {
      reflect().genericType().in(Object.class);
    });
  }

  public static class DeclaredTest {

    public String name;
    private String phone;

    public DeclaredTest() {

    }

    private DeclaredTest(String name) {

    }

    public void foo() {

    }

    private void bar() {

    }

  }

  @Test
  public void testDeclared() {
    assertEquals(
        1, reflect().visible().constructors().in(DeclaredTest.class).size()
    );
    assertEquals(
        1, reflect().visible().fields().in(DeclaredTest.class).size()
    );
    assertTrue(
        reflect().visible().methods().in(DeclaredTest.class).size() > 1
    );

    assertEquals(
        2, reflect().declared().constructors().in(DeclaredTest.class).size()
    );
    assertEquals(
        2, reflect().declared().fields().in(DeclaredTest.class).size()
    );
    assertEquals(
        2, reflect().declared().methods().in(DeclaredTest.class).size()
    );
  }

  @Test
  public void testInterfacesReflection() {
    List<Class> interfaces =
        reflect().interfaces().in(TruggerFieldsSelector.class);
    assertEquals(4, interfaces.size());
  }

}
