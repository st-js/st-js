package org.stjs.generator.writer.expression;

import java.util.Collections;
import java.util.List;

import javacutils.TypesUtils;

import javax.lang.model.type.TypeMirror;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;

import com.sun.source.tree.TypeCastTree;
import com.sun.source.util.TreePath;

public class TypeCastWriter implements VisitorContributor<TypeCastTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, TypeCastTree tree,
			GenerationContext context, List<AstNode> prev) {
		TypeMirror type = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getType()));
		List<AstNode> expr = visitor.scan(tree.getExpression(), context);
		if (TypesUtils.isIntegral(type)) {
			// add explicit cast in this case
			expr = Collections.<AstNode>singletonList(JavaScriptNodes.functionCall(JavaScriptNodes.name("stjs"), "trunc", expr.get(0)));
		}
		// otherwise skip to cast type - continue with the expression
		return expr;
	}
}
