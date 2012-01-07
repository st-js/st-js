package org.stjs.javascript.jquery.impl;

import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.JQueryAndPlugins;

/**
 * This class is used when you want to develop your own widgets
 * 
 * @author acraciun
 * 
 */
@SyntheticType
abstract public class UIMousePlugin<FullJQuery extends JQueryAndPlugins<?>> extends UIWidget<FullJQuery> {
	protected FullJQuery helper;

	protected boolean _mouseCapture(Event ev) {
		throw new UnsupportedOperationException();
	}

	protected void _mouseInit() {
		throw new UnsupportedOperationException();
	}

	protected void _mouseDrag(Event ev) {
		throw new UnsupportedOperationException();
	}

	protected void _mouseStart(Event ev) {
		throw new UnsupportedOperationException();
	}

	protected void _mouseStop(Event ev) {
		throw new UnsupportedOperationException();
	}
}
