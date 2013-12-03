package org.stjs.generator.writer.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;

/**
 * $array() -> []
 * @author acraciun
 */
public class ArrayTemplate implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {
		List<AstNode> values = new ArrayList<AstNode>();
		for (ExpressionTree arg : tree.getArguments()) {
			values.addAll(visitor.scan(arg, context));
		}

		return Collections.<AstNode> singletonList(JavaScriptNodes.array(values));
	}
}
