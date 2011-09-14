package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public interface JQueryUI<FullJQuery extends JQuery<?>> {
	<UI> FullJQuery bind(String path, UIEventHandler<UI> uiEventHandler);
}
