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

import tools.devnull.trugger.element.*;
import tools.devnull.trugger.selector.ElementsSelector;
import tools.devnull.trugger.util.Utils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The default implementation for the property copy operation.
 *
 * @author Marcelo Guimarães
 */
public final class TruggerElementCopier implements ElementCopier,
    CopyDestination {

  private final ElementsSelector selector;
  private final Function<ElementCopy, Object> function;
  private final Predicate<? super ElementCopy> predicate;
  private final boolean copyNull;

  private final Object src;

  public TruggerElementCopier() {
    this.selector = Elements.elements();
    this.function = copy -> copy.value();
    this.copyNull = true;
    this.predicate = copy -> true;
    this.src = null;
  }

  public TruggerElementCopier(ElementsSelector selector) {
    this.selector = selector;
    this.function = copy -> copy.value();
    this.copyNull = true;
    this.predicate = copy -> true;
    this.src = null;
  }

  private TruggerElementCopier(ElementsSelector selector,
                               Function<ElementCopy, Object> function,
                               Predicate<? super ElementCopy> predicate,
                               boolean copyNull,
                               Object src) {
    this.selector = selector;
    this.function = function;
    this.predicate = predicate;
    this.copyNull = copyNull;
    this.src = src;
  }

  public CopyDestination notNull() {
    return new TruggerElementCopier(selector, function, predicate, false, src);
  }

  public CopyDestination from(Object src) {
    return
        new TruggerElementCopier(selector, function, predicate, copyNull, src);
  }

  @Override
  public CopyDestination filter(Predicate<? super ElementCopy> predicate) {
    return
        new TruggerElementCopier(selector, function, predicate, copyNull, src);
  }

  @Override
  public CopyDestination applying(Function function) {
    return new TruggerElementCopier(selector, function, predicate, copyNull,
        src);
  }

  public void to(Object object) {
    startCopy(object);
  }

  private void startCopy(Object dest) {
    List<Element> elements = selector.in(src);
    Element destProperty;
    for (Element element : elements) {
      String name = element.name();
      if (src.getClass().equals(dest.getClass())) {
        destProperty = element.isWritable() ? element : null;
      } else {
        destProperty = Elements.element(name).in(dest);
      }
      if (destProperty != null && element.isReadable()
          && destProperty.isWritable()) {
        copy(destProperty, element, dest);
      }
    }
  }

  private void copy(Element destElement, Element srcElement, Object dest) {
    Object value = srcElement.in(this.src).value();
    PropertyCopyImpl copy = new PropertyCopyImpl(srcElement, destElement, value);
    if (predicate.test(copy)) {
      if (value != null) {
        value = function.apply(copy);
        if (Utils.areAssignable(destElement.type(), value.getClass())) {
          destElement.in(dest).set(value);
        }
      } else if (copyNull) {
        destElement.in(dest).set(value);
      }
    }
  }

  private static class PropertyCopyImpl implements ElementCopy {

    private final Element from;

    private final Element to;

    private final Object value;

    private PropertyCopyImpl(Element from, Element to, Object value) {
      this.from = from;
      this.to = to;
      this.value = value;
    }

    public Element src() {
      return from;
    }

    public Object value() {
      return value;
    }

    public Element dest() {
      return to;
    }

  }

}
