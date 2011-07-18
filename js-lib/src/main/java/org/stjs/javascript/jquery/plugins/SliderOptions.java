package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.Array;
import org.stjs.javascript.jquery.JQuery;

public class SliderOptions<FullJQuery extends JQuery<?>> {
	public boolean disabled = false;
	public double min = 0;
	public double max = 100;
	public double step = 1;
	public double value = 0;
	public String orientation = "horizontal";

	public Object animate = false;

	public Object range = false;

	public Array<Double> values;

	public UIEventHandler<SliderUI<FullJQuery>> create;
	public UIEventHandler<SliderUI<FullJQuery>> start;

	public UIEventHandler<SliderUI<FullJQuery>> slide;
	public UIEventHandler<SliderUI<FullJQuery>> change;
	public UIEventHandler<SliderUI<FullJQuery>> stop;
}
