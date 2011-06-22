package org.stjs.generator;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.FileWriter;
import java.io.InputStream;

import org.stjs.generator.handlers.ClassOrInterfaceDeclarationHandler;
import org.stjs.generator.handlers.DefaultHandler;
import org.stjs.generator.handlers.FieldDeclarationHandler;
import org.stjs.generator.handlers.InlineFunctionHandler;
import org.stjs.generator.handlers.InlineObjectHandler;
import org.stjs.generator.handlers.MarkerAnnotationHandler;
import org.stjs.generator.handlers.MethodDeclarationHandler;
import org.stjs.generator.handlers.RuleBasedVisitor;
import org.stjs.generator.handlers.SkipHandler;
import org.stjs.generator.handlers.VariableDeclarationHandler;
import org.stjs.generator.handlers.VariableTypeHandler;

public class Generator {

	private static MatchingRule rule(String name, String xpath, int priority, DefaultHandler handler) {
		return (new MatchingRule(name, xpath, new NodeHandlerWithPriority(handler, priority)));
	}

	private static void rules(RuleBasedVisitor ruleVisitor) {
		// to skip
		ruleVisitor.addRule(rule("Parameter Type", "//Parameter/ReferenceType", 100, new SkipHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Parameter Type", "//PackageDeclaration", 100, new SkipHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Parameter Type", "//ImportDeclaration", 100, new SkipHandler(ruleVisitor)));

		ruleVisitor.addRule(rule("VariableDeclaration", "//VariableDeclaratorId", 100, new VariableDeclarationHandler(
				ruleVisitor)));

		ruleVisitor.addRule(rule("Variable Type", "//VariableDeclarationExpr/ReferenceType/ClassOrInterfaceType", 100,
				new VariableTypeHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Variable Type", "//VariableDeclarationExpr/PrimitiveType", 100,
				new VariableTypeHandler(ruleVisitor)));

		ruleVisitor
				.addRule(rule("Annotations", "//MarkerAnnotationExpr", 100, new MarkerAnnotationHandler(ruleVisitor)));

		ruleVisitor
				.addRule(rule("Method", "//MethodDeclaration", 100, new MethodDeclarationHandler(ruleVisitor, false)));
		ruleVisitor.addRule(rule("Method Params", "//MethodDeclaration/Parameter", 100, new MethodDeclarationHandler(
				ruleVisitor, false)));
		ruleVisitor.addRule(rule("Class/Interface Declaration", "//ClassOrInterfaceDeclaration", 100,
				new ClassOrInterfaceDeclarationHandler(ruleVisitor)));

		// field declaration
		ruleVisitor.addRule(rule("Field", "//FieldDeclaration", 100, new FieldDeclarationHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Field - Variable Type", "//FieldDeclaration/ReferenceType/ClassOrInterfaceType", 100,
				new FieldDeclarationHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Field - Variable Type", "//FieldDeclaration/PrimitiveType", 100,
				new FieldDeclarationHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Field - Variable Type", "//FieldDeclaration/VariableDeclarator", 100,
				new FieldDeclarationHandler(ruleVisitor)));

		// method declaration
		ruleVisitor.addRule(rule("Inline Function", "//ObjectCreationExpr[MethodDeclaration]", 100,
				new InlineFunctionHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Inline Method", "//ObjectCreationExpr/MethodDeclaration", 200,
				new MethodDeclarationHandler(ruleVisitor, true)));

		// inline obj
		ruleVisitor.addRule(rule("Inline Object Decl", "//ObjectCreationExpr[InitializerDeclaration]", 100,
				new InlineObjectHandler(ruleVisitor)));

		ruleVisitor.addRule(rule("Inline Object Decl", "//ObjectCreationExpr/InitializerDeclaration/BlockStmt", 100,
				new InlineObjectHandler(ruleVisitor)));

		ruleVisitor.addRule(rule("Inline Object Decl",
				"//ObjectCreationExpr/InitializerDeclaration/BlockStmt/ExpressionStmt", 100, new InlineObjectHandler(
						ruleVisitor)));

		ruleVisitor.addRule(rule("Inline Object Decl",
				"//ObjectCreationExpr/InitializerDeclaration/BlockStmt/ExpressionStmt/AssignExpr", 100,
				new InlineObjectHandler(ruleVisitor)));

	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/swissquote/foundation/js/test/Transaction.java");

		RuleBasedVisitor ruleVisitor = new RuleBasedVisitor();

		rules(ruleVisitor);

		try {
			CompilationUnit cu = null;
			try {
				// parse the file
				cu = JavaParser.parse(in);
			} finally {
				in.close();
			}

			System.out.println("----------------------------");
			ruleVisitor.generate(cu);

			System.out.println("----------------------------");
			FileWriter writer = new FileWriter("target/Transaction.js");
			writer.write(ruleVisitor.getSource());
			writer.flush();
			writer.close();
			/*
			 * ASTCompilationUnit pmd = (ASTCompilationUnit) new Java16Parser().parse(new InputStreamReader(in));
			 * //chose the node for each node for (MatchingRule rule : rules) { List<SimpleNode> nodes =
			 * pmd.findChildNodesWithXPath(rule.getRule()); for (SimpleNode node : nodes) { NodeHandlerWithPriority nh =
			 * nodeHandlers.get(node); if ((nh == null) || (nh.getPriority() < rule.getHandler().getPriority())) {
			 * System.out.println("Rule:" + rule.getName() + " node:" + node); nodeHandlers.put(node,
			 * rule.getHandler()); } } }
			 * 
			 * JsPrinter printer = new JsPrinter(new OutputStreamWriter(System.out)); processNode(pmd, printer);
			 * printer.close();
			 */
		} finally {
			in.close();
		}
	}
}