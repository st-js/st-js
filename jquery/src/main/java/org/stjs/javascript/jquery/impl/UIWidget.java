package org.stjs.javascript.jquery.impl;

import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.jquery.JQueryAndPlugins;

@SyntheticType
abstract public class UIWidget<FullJQuery extends JQueryAndPlugins<?>> {
	protected void _trigger(String event) {
		throw new UnsupportedOperationException();
	}

	protected void _init() {
		throw new UnsupportedOperationException();
	}

	@Override
	public FullJQuery clone() {
		throw new UnsupportedOperationException();
	}
}
