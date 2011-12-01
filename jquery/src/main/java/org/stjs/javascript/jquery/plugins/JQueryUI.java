package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQueryCore;

public interface JQueryUI<FullJQuery extends JQueryCore<?>> {
	<UI> FullJQuery bind(String path, UIEventHandler<UI> uiEventHandler);
}
