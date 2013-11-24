package org.stjs.generator.writer.expression;

import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.TypeCastTree;

public class TypeCastWriter implements VisitorContributor<TypeCastTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, TypeCastTree tree, GenerationContext p,
			List<AstNode> prev) {
		throw new UnsupportedOperationException();
	}
}
