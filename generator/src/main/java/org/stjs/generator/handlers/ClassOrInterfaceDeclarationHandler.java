package org.stjs.generator.handlers;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;

import java.util.List;

import org.stjs.generator.GenerationContext;

public class ClassOrInterfaceDeclarationHandler extends DefaultHandler {

	public ClassOrInterfaceDeclarationHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	private void printMembers(List<BodyDeclaration> members, GenerationContext arg) {
		for (int i = 0; i < members.size(); ++i) {
			BodyDeclaration member = members.get(i);
			getPrinter().printLn();
			member.accept(getRuleVisitor(), arg);
			if ((i < members.size() - 1) && (members.size() > 1)) {
				getPrinter().print(",");
			}
			getPrinter().printLn();
		}
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, GenerationContext arg) {

		getPrinter().print(n.getName());

		getPrinter().printLn("= {");
		getPrinter().indent();
		if (n.getMembers() != null) {
			printMembers(n.getMembers(), arg);
		}
		getPrinter().unindent();
		getPrinter().print("};");
	}

}
