package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public class DialogOptions<FullJQuery extends JQuery<?>> {
	public boolean disabled = false;

	public boolean autoOpen = true;

	public Object buttons;// ={ } or Array

	public boolean closeOnEscape = true;

	public String closeText = "close";

	public String dialogClass = "";

	public boolean draggable = true;

	public String height = "auto";

	public Object hide = null;

	public Integer maxHeight = null;

	public Integer maxWidth = null;

	public int minHeight = 150;

	public int minWidth = 150;

	public boolean modal = false;

	public Object position = "center";

	public boolean resizable = true;

	public Object show = null;

	public boolean stack = true;

	public String title = "";

	public int width = 300;

	public int zIndex = 1000;

	public UIEventHandler<DialogUI<FullJQuery>> create;
	public UIEventHandler<DialogUI<FullJQuery>> beforeClose;

	public UIEventHandler<DialogUI<FullJQuery>> open;
	public UIEventHandler<DialogUI<FullJQuery>> focus;

	public UIEventHandler<DialogUI<FullJQuery>> dragStart;
	public UIEventHandler<DialogUI<FullJQuery>> drag;
	public UIEventHandler<DialogUI<FullJQuery>> dragStop;

	public UIEventHandler<DialogUI<FullJQuery>> resizeStart;
	public UIEventHandler<DialogUI<FullJQuery>> resize;
	public UIEventHandler<DialogUI<FullJQuery>> resizeSop;

	public UIEventHandler<DialogUI<FullJQuery>> close;

}
