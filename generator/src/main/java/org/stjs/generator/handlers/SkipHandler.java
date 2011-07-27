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

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;

import org.stjs.generator.GenerationContext;

public class SkipHandler extends DefaultHandler {

	public SkipHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(ClassOrInterfaceType n, GenerationContext arg) {
		// skip
	}

	@Override
	public void visit(ReferenceType n, GenerationContext arg) {
		// skip
	}

	@Override
	public void visit(ImportDeclaration n, GenerationContext arg) {
		// skip
	}

	@Override
	public void visit(PackageDeclaration n, GenerationContext arg) {
		// skip
	}

	@Override
	public void visit(MarkerAnnotationExpr n, GenerationContext arg) {
		// skip
	}

	@Override
	public void visit(CastExpr n, GenerationContext arg) {
		// skip to cast type - continue with the expression
		if (n.getExpr() != null) {
			n.getExpr().accept(getRuleVisitor(), arg);
		}
	}
}
