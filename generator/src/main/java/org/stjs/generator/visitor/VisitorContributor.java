package org.stjs.generator.visitor;

import com.sun.source.tree.Tree;

public interface VisitorContributor<T extends Tree, R, P extends TreePathHolder> {
	public R visit(TreePathScannerContributors<R, P> visitor, T tree, P p, R prev);
}
