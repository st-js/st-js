package org.stjs.generator.visitor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.stjs.generator.STJSRuntimeException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreeScanner;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class TreePathScannerContributors<R, P extends TreePathHolder, V extends TreePathScannerContributors<R, P, V>> extends TreeScanner<R, P> {

	private static final Logger LOG = Logger.getLogger(TreePathScannerContributors.class.getName());

	private final Map<Class<?>, ContributorHolder<? extends Tree>> contributors = Maps.newHashMap();

	private final Map<DiscriminatorKey, ContributorHolder<? extends Tree>> contributorsWithDiscriminator = Maps.newHashMap();

	private boolean continueScanning;

	private boolean onlyOneFinalContributor;

	public TreePathScannerContributors() {
		super();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TreePathScannerContributors(TreePathScannerContributors<R, P, V> copy) {
		super();
		// deep clone the maps
		contributors.clear();
		for (Map.Entry<Class<?>, ContributorHolder<? extends Tree>> entry : copy.contributors.entrySet()) {
			contributors.put(entry.getKey(), new ContributorHolder(entry.getValue()));
		}

		contributorsWithDiscriminator.clear();
		for (Map.Entry<DiscriminatorKey, ContributorHolder<? extends Tree>> entry : copy.contributorsWithDiscriminator.entrySet()) {
			contributorsWithDiscriminator.put(entry.getKey(), new ContributorHolder(entry.getValue()));
		}

		continueScanning = copy.continueScanning;
		onlyOneFinalContributor = copy.continueScanning;
	}

	public boolean isContinueScanning() {
		return continueScanning;
	}

	public void setContinueScanning(boolean continueScanning) {
		this.continueScanning = continueScanning;
	}

	public boolean isOnlyOneFinalContributor() {
		return onlyOneFinalContributor;
	}

	public void setOnlyOneFinalContributor(boolean onlyOneFinalContributor) {
		this.onlyOneFinalContributor = onlyOneFinalContributor;
	}

	@SuppressWarnings("unchecked")
	private <T extends Tree> ContributorHolder<T> getHolder(Class<?> contributorClass) {
		Class<?> treeNodeClass = getTreeNodeClass(contributorClass);
		if (treeNodeClass == null) {
			throw new STJSRuntimeException("Cannot guess the tree node class from the contributor " + contributorClass + ". ");
		}
		ContributorHolder<T> holder = (ContributorHolder<T>) contributors.get(treeNodeClass);
		if (holder == null) {
			holder = new ContributorHolder<T>();
			contributors.put(treeNodeClass, holder);
		}
		return holder;
	}

	public <T extends Tree, C extends VisitorContributor<T, R, P, V>, N> void contribute(@Nonnull C contributor) {
		if (onlyOneFinalContributor) {
			this.<T> getHolder(contributor.getClass()).setContributor(contributor);
		} else {
			this.<T> getHolder(contributor.getClass()).addContributor(contributor);
		}
	}

	public <T extends Tree, C extends VisitorContributor<T, R, P, V>, N> void contribute(@Nonnull C contributor, Class<T> nodeClass) {
		if (onlyOneFinalContributor) {
			this.<T> getHolder(nodeClass).setContributor(contributor);
		} else {
			this.<T> getHolder(nodeClass).addContributor(contributor);
		}
	}

	public <T extends Tree, F extends VisitorFilterContributor<T, R, P, V>> void addFilter(@Nonnull F filter) {
		this.<T> getHolder(filter.getClass()).addFilter(filter);
	}

	public <T extends Tree, F extends VisitorFilterContributor<T, R, P, V>> void addFilter(@Nonnull F filter, Class<?> nodeClass) {
		this.<T> getHolder(nodeClass).addFilter(filter);
	}

	@SuppressWarnings("unchecked")
	private <T extends Tree> ContributorHolder<T> getHolder(DiscriminatorKey discriminatorKey) {
		ContributorHolder<T> holder = (ContributorHolder<T>) contributorsWithDiscriminator.get(discriminatorKey);
		if (holder == null) {
			holder = new ContributorHolder<T>();
			contributorsWithDiscriminator.put(discriminatorKey, holder);
		}
		return holder;
	}

	public <T extends Tree, C extends VisitorContributor<T, R, P, V>> void contribute(@Nonnull DiscriminatorKey discriminatorKey,
			@Nonnull C contributor) {
		if (onlyOneFinalContributor) {
			this.<T> getHolder(discriminatorKey).setContributor(contributor);
		} else {
			this.<T> getHolder(discriminatorKey).addContributor(contributor);
		}
	}

	public <T extends Tree, F extends VisitorFilterContributor<T, R, P, V>> void addFilter(@Nonnull DiscriminatorKey discriminatorKey,
			@Nonnull F filter) {
		this.<T> getHolder(discriminatorKey).addFilter(filter);
	}

	private Class<?> getTreeNodeClassFromInteface(Type iface) {
		if (iface instanceof ParameterizedType) {
			ParameterizedType ptype = (ParameterizedType) iface;
			for (Type arg : ptype.getActualTypeArguments()) {
				// look for the first argument that is a descendant of Tree
				if (arg instanceof Class<?> && Tree.class.isAssignableFrom((Class<?>) arg)) {
					return (Class<?>) arg;
				}
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	private Class<?> getTreeNodeClass(Class<?> clazz) {
		if (Tree.class.isAssignableFrom(clazz)) {
			return clazz;
		}
		Type[] interfaces = clazz.getGenericInterfaces();
		for (Type iface : interfaces) {
			Class<?> nodeClass = getTreeNodeClassFromInteface(iface);
			if (nodeClass != null) {
				return nodeClass;
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
		LOG.warning("No contributor found with key:" + discriminator);
		return null;
	}

	@SuppressWarnings("unchecked")
	protected <T extends Tree> R visit(T node, P p, R r) {
		if (node == null) {
			return r;
		}
		ContributorHolder<T> holder = (ContributorHolder<T>) contributors.get(getTreeInteface(node.getClass()));
		R lastR = holder == null ? null : holder.visit((V) this, node, p);
		if (continueScanning) {
			lastR = node.accept(this, p);
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

	/**
	 *
	 * Keeps the list of filters and contributors for a given type of node
	 *
	 * @param <T>
	 */
	private class ContributorHolder<T extends Tree> implements VisitorContributor<T, R, P, V> {
		private final List<VisitorFilterContributor<T, R, P, V>> filters = Lists.newArrayList();
		private final List<VisitorContributor<T, R, P, V>> contributors = Lists.newArrayList();

		public ContributorHolder() {
			//
		}

		public ContributorHolder(ContributorHolder<T> copy) {
			filters.addAll(copy.filters);
			contributors.addAll(copy.contributors);
		}

		public void addFilter(VisitorFilterContributor<T, R, P, V> f) {
			filters.add(f);
		}

		public void addContributor(VisitorContributor<T, R, P, V> c) {
			contributors.add(c);
		}

		public void setContributor(VisitorContributor<T, R, P, V> c) {
			contributors.clear();
			contributors.add(c);
		}

		public List<VisitorFilterContributor<T, R, P, V>> getFilters() {
			return filters;
		}

		@Override
		public R visit(V visitor, T tree, P p) {
			if (!filters.isEmpty()) {
				// create a chain if there is at least a filter
				return new FilterChain<T>(this).visit(visitor, tree, p);
			}
			return visitContributors(visitor, tree, p);
		}

		public R visitContributors(V visitor, T tree, P p) {
			// the contributors are called at the end, but only the result of the last one will be kept
			R lastR = null;
			for (VisitorContributor<T, R, P, V> vc : contributors) {
				lastR = vc.visit(visitor, tree, p);
			}
			return lastR;
		}

	}

	/**
	 *
	 * a filter is created for each visit in a node
	 *
	 * @param <T>
	 */
	private class FilterChain<T extends Tree> implements VisitorContributor<T, R, P, V> {
		private final ContributorHolder<T> holder;
		private int nextFilter;

		public FilterChain(ContributorHolder<T> holder) {
			this.holder = holder;
		}

		@Override
		public R visit(V visitor, T tree, P p) {
			// filters are called before, one by one
			if (nextFilter < holder.getFilters().size()) {
				VisitorFilterContributor<T, R, P, V> next = holder.getFilters().get(nextFilter);
				nextFilter++;
				return next.visit(visitor, tree, p, this);
			}
			return holder.visitContributors(visitor, tree, p);
		}
	}
}
