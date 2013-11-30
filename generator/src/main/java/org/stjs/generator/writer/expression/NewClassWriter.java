package org.stjs.generator.writer.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;

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
		// if (ClassUtils.isJavascriptFunction(clazz)) {
		// printInlineFunction(n, context);
		// return;
		// }
		return null;
	}

	private List<AstNode> getAnonymousClassDeclaration(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewClassTree tree,
			GenerationContext context) {
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
		return null;
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

		List<AstNode> arguments = new ArrayList<AstNode>();
		for (Tree arg : tree.getArguments()) {
			arguments.addAll(visitor.scan(arg, context));
		}

		Element type = TreeUtils.elementFromUse(tree.getIdentifier());
		return Collections.<AstNode>singletonList(JavaScriptNodes.newExpression(
				JavaScriptNodes.name(context.getNames().getTypeName(context, type)), arguments));

		// if (clazz instanceof ClassWrapper && ClassUtils.isSyntheticType(clazz)) {
		// // this is a call to an mock type
		// printer.print("{}");
		// return;
		// }

	}

}
