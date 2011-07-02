package org.stjs.generator.handlers;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
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
}
