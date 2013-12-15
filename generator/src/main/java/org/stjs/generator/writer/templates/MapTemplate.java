package org.stjs.generator.writer.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.JavaScriptNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $map(n1, v1, n2, v2) -> {n1: v1, n2: v2}
 * 
 * @author acraciun
 */
public class MapTemplate implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {

		List<AstNode> names = new ArrayList<AstNode>();
		List<AstNode> values = new ArrayList<AstNode>();
		for (int i = 0; i < tree.getArguments().size(); i += 2) {
			names.add(visitor.scan(tree.getArguments().get(i), context).get(0));
			values.add(visitor.scan(tree.getArguments().get(i + 1), context).get(0));
		}
		return Collections.<AstNode>singletonList(JavaScriptNodes.object(names, values));
	}
}
