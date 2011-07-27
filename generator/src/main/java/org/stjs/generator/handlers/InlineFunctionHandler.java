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

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.ObjectCreationExpr;

import org.stjs.generator.GenerationContext;

public class InlineFunctionHandler extends DefaultHandler {
	public InlineFunctionHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	private MethodDeclaration getMethodDeclaration(ObjectCreationExpr n) {
		for (BodyDeclaration d : n.getAnonymousClassBody()) {
			if (d instanceof MethodDeclaration) {
				return (MethodDeclaration) d;
			}
		}
		return null;
	}

	@Override
	public void visit(ObjectCreationExpr n, GenerationContext arg) {
		MethodDeclaration method = getMethodDeclaration(n);
		if (method == null) {
			// TODO error here
			return;
		}
		method.accept(getRuleVisitor(), arg);
		// getRuleVisitor().visit(, Boolean.TRUE);
	}

}
