package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public class ResizableOptions<FullJQuery extends JQuery<?>> {
	public boolean disabled = false;

	public Object alsoResize = false;

	public boolean animate = false;

	public Object animateDuration = "slow";

	public String animateEasing = "swing";

	public Object aspectRatio = false;

	public boolean autoHide = false;

	public String cancel = ":input,option";

	public Object containment = false;

	public int delay = 0;

	public int distance = 1;

	public boolean ghost = false;

	public Object grid = false;

	public String handles = "e, s, se";

	public String helper = "false";

	public Integer maxHeight = null;

	public Integer maxWidth = null;

	public int minHeight = 10;

	public int minWidth = 10;

	public UIEventHandler<ResizeableUI<FullJQuery>> create;
	public UIEventHandler<ResizeableUI<FullJQuery>> start;

	public UIEventHandler<ResizeableUI<FullJQuery>> resize;
	public UIEventHandler<ResizeableUI<FullJQuery>> stop;
}
