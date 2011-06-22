package org.stjs.javascript.jquery;

import org.stjs.javascript.jquery.plugins.DatePickerOptions;
import org.stjs.javascript.jquery.plugins.SliderOptions;

/**
 * jquery intreface for all jquery method + all used plugins in a web app. it can be mocked easily for testing purposes
 */
public interface JQuery {
	public void change(ChangeListener changeListener);

	public void val(Object d);

	public void show();

	public void html(Object data);

	public void attr(String name, String value);

	public String val();

	public void hide();

	public void trigger(String string);

	public void datepicker(DatePickerOptions options);

	public boolean hasClass(String css);

	public void submit();

	public void click(ClickListener clickListener);

	public void focus(FocusListener focusListener);

	public void keyup(KeyupListener keyupListener);

	public void addClass(String string);

	public void removeClass(String string);

	public void slider(String string, String string2, double p);

	public String slider(String string, String string2);

	public void text(Object txt);

	public void slider(SliderOptions sliderOptions);

	public String attr(String attrName);
}