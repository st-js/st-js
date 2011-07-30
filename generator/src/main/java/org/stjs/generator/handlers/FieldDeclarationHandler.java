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

import static org.stjs.generator.handlers.utils.Joiner.joiner;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import org.stjs.generator.GenerationContext;

public class FieldDeclarationHandler extends DefaultHandler {

	public FieldDeclarationHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(FieldDeclaration n, GenerationContext arg) {
		n.getType().accept(getRuleVisitor(), arg);
		joiner(getRuleVisitor(), arg).on(", ").join(n.getVariables());
	}

	@Override
	public void visit(VariableDeclarator n, GenerationContext arg) {
		n.getId().accept(getRuleVisitor(), arg);
		if (n.getInit() != null) {
			getPrinter().print(" = ");
			n.getInit().accept(getRuleVisitor(), arg);
		} else {
			getPrinter().print(" = null");
		}
	}

	@Override
	public void visit(ClassOrInterfaceType n, GenerationContext arg) {
		// skip
	}

	@Override
	public void visit(PrimitiveType n, GenerationContext arg) {
		// skip
	}
}
