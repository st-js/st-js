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
package org.stjs.generator.handlers;

import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;

import java.util.Iterator;

import org.stjs.generator.GenerationContext;

public class EnumHandler extends DefaultHandler {

	public EnumHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(EnumDeclaration n, GenerationContext arg) {
		printer.print(n.getName());

		// TODO implements not considered
		printer.print(" = ");
		printer.printLn(" {");
		printer.indent();
		if (n.getEntries() != null) {
			for (Iterator<EnumConstantDeclaration> i = n.getEntries().iterator(); i.hasNext();) {
				EnumConstantDeclaration e = i.next();
				printer.print(e.getName());
				printer.print(" : \"");
				printer.print(e.getName());
				printer.print("\"");
				if (i.hasNext()) {
					printer.printLn(", ");
				}
			}
		}
		// TODO members not considered
		printer.printLn("");
		printer.unindent();
		printer.print("}");
	}
}
