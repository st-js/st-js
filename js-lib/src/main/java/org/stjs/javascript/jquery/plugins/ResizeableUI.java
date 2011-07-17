package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.jquery.JQuery;
import org.stjs.javascript.jquery.Position;
import org.stjs.javascript.jquery.Size;

public class ResizeableUI<FullJQuery extends JQuery<?>> {
	public FullJQuery helper;
	public Position originalPosition;
	public Size originalSize;
	public Position position;
	public Size size;

}
