package org.stjs.generator.writer;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;

public class CompilationUnitWriter implements VisitorContributor<CompilationUnitTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, CompilationUnitTree tree,
			GenerationContext p, List<AstNode> prev) {
		AstRoot root = new AstRoot();
		for (Tree type : tree.getTypeDecls()) {
			List<AstNode> jsNodes = visitor.scan(type, p);
			for (AstNode jsNode : jsNodes) {
				root.addChild(jsNode);
			}
		}
		return Collections.<AstNode>singletonList(root);
	}

}
