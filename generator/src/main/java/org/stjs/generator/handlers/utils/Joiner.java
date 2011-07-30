/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator.handlers.utils;

import static org.stjs.generator.handlers.utils.PreConditions.checkNotNull;
import japa.parser.ast.Node;
import java.io.IOException;
import java.util.Iterator;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.handlers.RuleBasedVisitor;

public class Joiner<T> {

  private final String separator;
  private final Function<T, ?> function;
  private Appendable printer;

  private Joiner(String separator, Function<T, ?> function, Appendable printer) {
    this.function = function;
    this.separator = separator;
    this.printer = printer ;
  }

  public String join(Iterable<? extends T> nodes) {
    try {
      if (nodes != null) {
        if (printer == null) {
          printer = new StringBuilder();
        }
        Iterator<? extends T> iterator = nodes.iterator();
        if (iterator.hasNext()) {
          Object obj = function.apply(iterator.next());
          if (obj != null && obj instanceof String) {
            printer.append((String)obj);
          }
          while (iterator.hasNext()) {
            printer.append(separator);
            obj = function.apply(iterator.next());
            if (obj != null && obj instanceof String) {
              printer.append((String)obj);
            }
          }
        }
        return printer.toString();
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }
  
  public static <T> JoinerBuilder2<T> joiner(Function<T, ?> function) {
    return new JoinerBuilder2<T>(function);
  }

  public static JoinerBuilder joiner(RuleBasedVisitor visitor, GenerationContext context) {
    return new JoinerBuilder(visitor, context);
  }

  public static class JoinerBuilder2<T> {
    private final Function<T, ?> function;
    
    private JoinerBuilder2(Function<T, ?> function) {
      this.function = checkNotNull(function);
    }
    
    public Joiner<T> on(String separator) {
      return new Joiner<T>(separator, function, null);
    }
    
  }
  public static class JoinerBuilder {
    private final RuleBasedVisitor visitor;
    private final GenerationContext context;

    public JoinerBuilder(RuleBasedVisitor visitor, GenerationContext context) {
      this.context = checkNotNull(context);
      this.visitor = checkNotNull(visitor);
    }

    public <T extends Node> Joiner<T> on(String separator) {
      return new Joiner<T>(separator,
          new Function<T, Boolean>() {
            @Override
            public Boolean apply(T input) {
              input.accept(visitor, context);
              return false;
            }},
          new Appendable() {
            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
              throw new UnsupportedOperationException();
            }
            @Override
            public Appendable append(char c) throws IOException {
              throw new UnsupportedOperationException();
            }
            @Override
            public Appendable append(CharSequence csq) throws IOException {
              visitor.getPrinter().print((String)csq);
              return this;
            }
          });
    }
  }

}
