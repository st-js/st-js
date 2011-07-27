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
package org.stjs.generator;

import static junit.framework.Assert.assertEquals;
import japa.parser.ast.Node;
import japa.parser.ast.visitor.VoidVisitor;
import java.io.File;
import org.stjs.generator.GenerationContextFactory.PartialGenerationContext;
import org.stjs.generator.handlers.RuleBasedVisitor;

public class GeneratedScriptTester {

	public static GeneratedScriptTester handlerTester(Class<? extends VoidVisitor<GenerationContext>> handlerClass,
			boolean trimAllWhiteSpaces) {
		return new GeneratedScriptTester(handlerClass, trimAllWhiteSpaces);
	}

	private final Class<? extends VoidVisitor<GenerationContext>> handlerClass;
	private final boolean trimAllWhiteSpaces;

	private GeneratedScriptTester(Class<? extends VoidVisitor<GenerationContext>> handlerClass,
			boolean trimAllWhiteSpaces) {
		this.handlerClass = handlerClass;
		this.trimAllWhiteSpaces = trimAllWhiteSpaces;
	}

	public void assertGenerateString(String expected, Node node) {
    assertGenerateString(expected, node, new GenerationContext(new File("test.java")));
  }
	
	public void assertGenerateString(String expected, Node node, GenerationContext context) {
		try {
			RuleBasedVisitor visitor = new RuleBasedVisitor();
			VoidVisitor<GenerationContext> handler = handlerClass.getConstructor(RuleBasedVisitor.class).newInstance(
					visitor);
			node.accept(handler, context);
			String got = visitor.getPrinter().toString();
			if (trimAllWhiteSpaces) {
				got = got.replaceAll("\\s", "");
			}
			assertEquals(expected, got);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

  public void assertGenerateString(String expected, Node node,
      PartialGenerationContext context) {
    assertGenerateString(expected, node, context.build());    
  }

}