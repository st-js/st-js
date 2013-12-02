package org.stjs.generator.writer.expression;

import java.util.Collections;
import java.util.List;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;

import com.sun.source.tree.MemberSelectTree;

public class MemberSelectWriter implements VisitorContributor<MemberSelectTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MemberSelectTree tree,
			GenerationContext p, List<AstNode> prev) {
		// this is only for fields. Methods are handled in MethodInvocationWriter

		Element element = TreeUtils.elementFromUse(tree);
		if (element == null || element.getKind() == ElementKind.PACKAGE) {
			//package names are ignored
			return Collections.emptyList();
		}
		if (element.getKind() == ElementKind.CLASS && JavaNodes.isGlobal(element)) {
			//global classes are ignored
			return Collections.emptyList();
		}
		//		if (element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.ENUM || element.getKind() == ElementKind.INTERFACE) {
		//			if (JavaNodes.isGlobal(element)) {
		//				return Collections.emptyList();
		//			}
		//			name = context.getNames().getTypeName(context, def);
		//		}

		AstNode target = null;
		if (JavaNodes.isSuper(tree.getExpression())) {
			// super.field does not make sense, so convert it to this
			target = JavaScriptNodes.THIS();
		} else {
			List<AstNode> exprNodes = visitor.scan(tree.getExpression(), p);
			target = exprNodes.isEmpty() ? null : exprNodes.get(0);
		}

		return Collections.<AstNode> singletonList(JavaScriptNodes.property(target, tree.getIdentifier().toString()));
	}
}
