package org.stjs.generator.scope;

public class NameScopeWalker {
	private final NameScope scope;
	private int currentChild = 0;

	public NameScopeWalker(NameScope scope) {
		this.scope = scope;
	}

	public NameScope getScope() {
		return scope;
	}

	public NameScopeWalker nextChild() {
		if (currentChild >= scope.getChildren().size()) {
			throw new IllegalStateException("The scope [" + scope.getPath() + "] does not have a child #"
					+ currentChild);
		}
		return new NameScopeWalker(scope.getChildren().get(currentChild++));
	}

}
