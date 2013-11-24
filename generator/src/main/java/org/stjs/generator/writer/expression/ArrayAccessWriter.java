package org.stjs.generator.writer.expression;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.ArrayAccessTree;

public class ArrayAccessWriter implements VisitorContributor<ArrayAccessTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ArrayAccessTree tree, GenerationContext p,
			List<AstNode> prev) {
		ElementGet array = new ElementGet();
		array.setTarget(visitor.scan(tree.getExpression(), p).get(0));
		array.setElement(visitor.scan(tree.getIndex(), p).get(0));
		return Collections.<AstNode>singletonList(array);
	}
}
