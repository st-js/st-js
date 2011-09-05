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

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.stmt.BlockStmt;

import java.util.Iterator;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;

public class MethodDeclarationHandler extends DefaultHandler {
	private final boolean anonymous;

	public MethodDeclarationHandler(RuleBasedVisitor ruleVisitor, boolean anonymous) {
		super(ruleVisitor);
		this.anonymous = anonymous;
	}

	private void printMethod(String name, List<Parameter> parameters, BlockStmt body, GenerationContext arg) {
		if (anonymous) {
			printer.print("function");
		} else {
			printer.print(name);
			printer.print(" = function");
		}

		printer.print("(");
		if (parameters != null) {
			boolean first = true;
			for (Iterator<Parameter> i = parameters.iterator(); i.hasNext();) {
				Parameter p = i.next();
				// don't display the special THIS parameter
				if (GeneratorConstants.SPECIAL_THIS.equals(p.getId().getName())) {
					continue;
				}
				if (!first) {
					printer.print(", ");
				}
				p.accept(getRuleVisitor(), arg);
				first = false;
			}
		}
		printer.print(")");
		// skip throws
		if (body == null) {
			printer.print(";");
		} else {
			printer.print(" ");
			body.accept(getRuleVisitor(), arg);
		}
	}

	@Override
	public void visit(MethodDeclaration n, GenerationContext arg) {
		printMethod(n.getName(), n.getParameters(), n.getBody(), arg);
	}

	@Override
	public void visit(ConstructorDeclaration n, GenerationContext arg) {
		printMethod(n.getName(), n.getParameters(), n.getBlock(), arg);
	}

	@Override
	public void visit(Parameter n, GenerationContext arg) {
		n.getType().accept(getRuleVisitor(), arg);
		n.getId().accept(getRuleVisitor(), arg);
	}
}
