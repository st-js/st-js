package org.stjs.generator.visitor;

import com.sun.source.tree.Tree;

public interface VisitorFilterContributor<T extends Tree, R, P extends TreePathHolder, V extends TreePathScannerContributors<R, P, ?>> {
	R visit(V visitor, T tree, P p, VisitorContributor<T, R, P, V> chain);
}
