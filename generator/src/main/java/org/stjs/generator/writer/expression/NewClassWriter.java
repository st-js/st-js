package org.stjs.generator.writer.expression;

import static org.stjs.generator.writer.JavaScriptNodes.name;
import static org.stjs.generator.writer.JavaScriptNodes.newExpression;
import static org.stjs.generator.writer.JavaScriptNodes.paren;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Tree;

public class NewClassWriter implements VisitorContributor<NewClassTree, List<AstNode>, GenerationContext> {
	private List<AstNode> getObjectInitializer(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context) {
		// special construction for object initialization new Object(){{x = 1; y = 2; }};
		if (tree.getEnclosingExpression() == null) {
			return null;
		}
		// for (BodyDeclaration d : n.getAnonymousClassBody()) {
		// if (d instanceof InitializerDeclaration) {
		// return (InitializerDeclaration) d;
		// }
		// }
		return null;
	}

	private List<AstNode> getInlineFunctionDeclaration(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context) {
		// special construction for inline function definition
		Element type = TreeUtils.elementFromUse(tree.getIdentifier());
		if (!JavaNodes.isJavaScriptFunction(type)) {
			return null;
		}

		// here there should be a check that verified the existence of a single method (first is the generated
		// constructor)
		Tree method = tree.getClassBody().getMembers().get(1);
		return visitor.scan(method, context);
	}

	private List<AstNode> getAnonymousClassDeclaration(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context) {
		if (tree.getClassBody() == null) {
			return null;
		}

		List<AstNode> typeDeclaration = visitor.scan(tree.getClassBody(), context);

		return Collections.<AstNode>singletonList(newExpression(paren(typeDeclaration.get(0)), arguments(visitor, tree, context)));

		// if (!Lists.isNullOrEmpty(n.getAnonymousClassBody())) {
		//
		// // special construction to handle the inline body
		// printer.print("new ");
		// ClassOrInterfaceDeclaration inlineFakeClass = buildClassDeclaration(GeneratorConstants.SPECIAL_INLINE_TYPE,
		// n.getType(),
		// n.getAnonymousClassBody());
		// inlineFakeClass.setData(n.getData());
		// inlineFakeClass.accept(this, context);
		//
		// printArguments(n.getArgs(), context);
		// return;
		// }
	}

	private List<AstNode> arguments(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context) {
		List<AstNode> arguments = new ArrayList<AstNode>();
		for (Tree arg : tree.getArguments()) {
			arguments.addAll(visitor.scan(arg, context));
		}
		return arguments;
	}

	private List<AstNode> getRegularNewExpression(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context) {
		Element type = TreeUtils.elementFromUse(tree.getIdentifier());
		return Collections.<AstNode>singletonList(newExpression(name(context.getNames().getTypeName(context, type)),
				arguments(visitor, tree, context)));
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context, List<AstNode> prev) {
		List<AstNode> js = getObjectInitializer(visitor, tree, context);
		if (js != null) {
			return js;
		}

		js = getInlineFunctionDeclaration(visitor, tree, context);
		if (js != null) {
			return js;
		}

		js = getAnonymousClassDeclaration(visitor, tree, context);
		if (js != null) {
			return js;
		}

		return getRegularNewExpression(visitor, tree, context);

		// if (clazz instanceof ClassWrapper && ClassUtils.isSyntheticType(clazz)) {
		// // this is a call to an mock type
		// printer.print("{}");
		// return;
		// }

	}

}
