package org.stjs.javascript.jquery;

/**
 * jquery interface for all jquery method + all used plugins in a web app. it can be mocked easily for testing purposes.
 * Compatible with jquery 1.6
 */
public interface JQuery<FullJQuery extends JQuery<?>> {
	public void change(ChangeListener changeListener);

	public void val(Object d);

	public void show();

	public void html(Object data);

	public void attr(String name, String value);

	public String attr(String attrName);

	public String val();

	public void hide();

	public void trigger(String string);

	public boolean hasClass(String css);

	public void submit();

	/************* events ***************/
	public void click(ClickListener clickListener);

	public void focus(FocusListener focusListener);

	public void keyup(KeyupListener keyupListener);

	public void addClass(String string);

	public void removeClass(String string);

	public void text(Object txt);

	public FullJQuery appendTo(String target);

	public FullJQuery parent(String selector);

	public FullJQuery parents(String selector);

	public void remove();

	public FullJQuery find(String selector);
}