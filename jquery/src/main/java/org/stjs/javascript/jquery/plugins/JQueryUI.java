package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.jquery.JQueryCore;

@SyntheticType
public interface JQueryUI<FullJQuery extends JQueryCore<?>> {
	<UI> FullJQuery bind(String path, UIEventHandler<UI> uiEventHandler);

	void enableSelection();

	void disableSelection();

	void scrollParent();
}
