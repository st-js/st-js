package org.stjs.generator.visitor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreeScanner;

public class TreePathScannerContributors<R, P extends TreePathHolder, V extends TreePathScannerContributors<R, P, V>> extends TreeScanner<R, P> {
	private final Multimap<Class<?>, VisitorContributor<? extends Tree, R, P, V>> contributors = LinkedListMultimap.create();

	private final Map<DiscriminatorKey, VisitorContributor<? extends Tree, R, P, V>> contributorsWithDiscriminator =
			new HashMap<DiscriminatorKey, VisitorContributor<? extends Tree, R, P, V>>();

	private boolean continueScanning = false;

	public boolean isContinueScanning() {
		return continueScanning;
	}

	public void setContinueScanning(boolean continueScanning) {
		this.continueScanning = continueScanning;
	}

	public <T extends VisitorContributor<? extends Tree, R, P, V>> TreePathScannerContributors<R, P, V> contribute(T contributor) {
		assert contributor != null;
		Class<?> treeNodeClass = getTreeNodeClass(contributor.getClass());
		if (treeNodeClass == null) {
			throw new RuntimeException("Cannot guess the tree node class from the contributor " + contributor + " ");
		}
		contributors.put(treeNodeClass, contributor);
		return this;
	}

	public <T extends VisitorContributor<? extends Tree, R, P, V>> TreePathScannerContributors<R, P, V> contribute(
			DiscriminatorKey discriminatorKey, T contributor) {
		assert contributor != null;
		contributorsWithDiscriminator.put(discriminatorKey, contributor);
		return this;
	}

	/**
	 * @return
	 */
	private Class<?> getTreeNodeClass(Class<?> clazz) {
		Type[] interfaces = clazz.getGenericInterfaces();
		for (Type iface : interfaces) {
			if (iface instanceof ParameterizedType) {
				ParameterizedType ptype = (ParameterizedType) iface;
				if (ptype.getRawType().equals(VisitorContributor.class)) {
					// I need the class of the tree
					return (Class<?>) ptype.getActualTypeArguments()[0];
				}
			}
		}
		return null;
	}

	private Class<?> getTreeInteface(Class<?> clazz) {
		Type[] interfaces = clazz.getGenericInterfaces();
		for (Type iface : interfaces) {
			if (iface instanceof Class<?>) {
				Class<?> type = (Class<?>) iface;
				if (Tree.class.isAssignableFrom(type)) {
					return type;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends Tree> R forward(DiscriminatorKey discriminator, T node, P param) {
		VisitorContributor<? extends Tree, R, P, V> contributor = contributorsWithDiscriminator.get(discriminator);
		if (contributor != null) {
			// here i should be sure i have the right type
			return ((VisitorContributor<T, R, P, V>) contributor).visit((V) this, node, param);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected <T extends Tree> R visit(T node, P p, R r) {
		if (node == null) {
			return r;
		}
		Collection<VisitorContributor<? extends Tree, R, P, V>> nodeContributors = contributors.get(getTreeInteface(node.getClass()));
		if (nodeContributors.isEmpty() && !continueScanning) {
			System.err.println("No contributors for node of type:" + getTreeInteface(node.getClass()));
		}
		R lastR = r;
		for (VisitorContributor<? extends Tree, R, P, V> vc : nodeContributors) {
			lastR = ((VisitorContributor<T, R, P, V>) vc).visit((V) this, node, p);
		}
		if (continueScanning) {
			return node.accept(this, p);
		}
		return lastR;
	}

	@Override
	public R scan(Tree tree, P p) {
		if (tree == null) {
			return null;
		}
		TreePath prev = p.getCurrentPath();
		p.setCurrentPath(new TreePath(prev, tree));
		try {
			return visit(tree, p, null);
		}
		finally {
			p.setCurrentPath(prev);
		}
	}

}
