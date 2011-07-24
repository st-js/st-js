package org.stjs.generator.handlers;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;

import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.SourcePosition;

public class ClassOrInterfaceDeclarationHandler extends DefaultHandler {

	public ClassOrInterfaceDeclarationHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	private int getModifiers(BodyDeclaration member) {
		if (member instanceof FieldDeclaration) {
			return ((FieldDeclaration) member).getModifiers();
		}
		if (member instanceof MethodDeclaration) {
			return ((MethodDeclaration) member).getModifiers();
		}
		return 0;
	}

	private void printMembers(List<BodyDeclaration> members, GenerationContext arg, boolean staticFields) {
		boolean first = true;
		for (int i = 0; i < members.size(); ++i) {
			BodyDeclaration member = members.get(i);
			if (member instanceof ConstructorDeclaration) {
				continue;
			}
			int memberModifiers = getModifiers(member);
			if (ModifierSet.isStatic(memberModifiers) != staticFields) {
				continue;
			}
			if (!first) {
				getPrinter().print(",");
			}
			first = false;
			getPrinter().printLn();
			member.accept(getRuleVisitor(), arg);
		}
	}

	private ConstructorDeclaration getConstructor(List<BodyDeclaration> members, GenerationContext arg) {
		ConstructorDeclaration constr = null;
		for (BodyDeclaration member : members) {
			if (member instanceof ConstructorDeclaration) {
				if (constr != null) {
					throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(member),
							"Only maximum one constructor is allowed");
				} else {
					constr = (ConstructorDeclaration) member;
				}
			}
		}
		return constr;
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, GenerationContext arg) {
		// TODO how to handle interfaces !?
		getPrinter().print(n.getName());

		getPrinter().print("= stjs.define(");
		if (n.getExtends() != null && n.getExtends().size() > 0) {
			getPrinter().print(n.getExtends().get(0).getName());
		} else {
			getPrinter().print("null");
		}
		getPrinter().printLn(",");
		if (n.getMembers() != null) {
			getPrinter().indent();
			getPrinter().printLn(" // constructor");
			ConstructorDeclaration constr = getConstructor(n.getMembers(), arg);
			if (constr != null) {
				constr.accept(getRuleVisitor(), arg);
			} else {
				getPrinter().printLn("function(){}");
			}
			getPrinter().printLn(", {");
			getPrinter().printLn("// instance methods and fields");
			printMembers(n.getMembers(), arg, false);
			getPrinter().printLn("},{ ");
			getPrinter().printLn("// static methods and fields");
			printMembers(n.getMembers(), arg, true);
			getPrinter().printLn("}");
			getPrinter().unindent();
		}
		getPrinter().printLn(");");
	}

}
