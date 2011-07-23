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
		getPrinter().print(n.getName());

		// TODO implements not considered
		getPrinter().print(" = ");
		getPrinter().printLn(" {");
		getPrinter().indent();
		if (n.getEntries() != null) {
			for (Iterator<EnumConstantDeclaration> i = n.getEntries().iterator(); i.hasNext();) {
				EnumConstantDeclaration e = i.next();
				getPrinter().print(e.getName());
				getPrinter().print(" : \"");
				getPrinter().print(e.getName());
				getPrinter().print("\"");
				if (i.hasNext()) {
					getPrinter().printLn(", ");
				}
			}
		}
		// TODO members not considered
		getPrinter().printLn("");
		getPrinter().unindent();
		getPrinter().print("}");
	}
}
