package org.stjs.generator.handlers;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;

public class SkipHandler extends DefaultHandler {

	public SkipHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	@Override
	public void visit(ClassOrInterfaceType n, Object arg) {
		//skip
	}

	@Override
	public void visit(ReferenceType n, Object arg) {
		//skip
	}

	@Override
	public void visit(ImportDeclaration n, Object arg) {
		//skip
	}

	@Override
	public void visit(PackageDeclaration n, Object arg) {
		//skip
	}
}
