package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;

public class DraggableOptions<FullJQuery extends JQuery<?>> {
	public boolean disabled = false;

	public boolean addClasses = true;

	public Object appendTo = "parent";

	public String axis = "false";

	public String cancel = ":input,option";

	public String connectToSortable = "false";

	public Object containment = false;

	public String cursor = "auto";

	public Object cursorAt = false;

	public int delay = 0;

	public int distance = 1;

	public Object grid = false;

	public Object handle = false;

	public Object helper = "original";

	public Object iframeFix = false;

	public float opacity = 0;

	public boolean refreshPositions = false;

	public Object revert = false;

	public int revertDuration = 500;

	public String scope = "default";

	public boolean scroll = true;

	public int scrollSensitivity = 20;

	public int scrollSpeed = 20;

	public Object snap = false;

	public String snapMode = "both";

	public int snapTolerance = 20;

	public String stack = "false";

	public int zIndex = 0;

	public UIEventHandler<DraggableUI<FullJQuery>> create;
	public UIEventHandler<DraggableUI<FullJQuery>> start;

	public UIEventHandler<DraggableUI<FullJQuery>> drag;
	public UIEventHandler<DraggableUI<FullJQuery>> stop;

}
