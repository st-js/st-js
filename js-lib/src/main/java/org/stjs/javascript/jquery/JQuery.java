package org.stjs.javascript.jquery;

import java.util.Map;

/**
 * jquery interface for all jquery method + all used plugins in a web app. it can be mocked easily for testing purposes.
 * Compatible with jquery 1.6
 */
public interface JQuery<FullJQuery extends JQuery<?>> {
	/************* ajax ***************/

	/************* effects ***************/

	/************* attributes ***************/
	public FullJQuery addClass(String className);

	public boolean hasClass(String className);

	public FullJQuery removeClass(String className);

	public FullJQuery toggleClass(String className);

	public FullJQuery toggleClass(String className, boolean on);

	public FullJQuery val(Object d);

	public String val();

	public FullJQuery attr(String name, String value);

	public String attr(String attrName);

	public FullJQuery removeAttr(String name);

	public FullJQuery prop(String name, String value);

	public FullJQuery removeProp(String name);

	public String prop(String attrName);

	public FullJQuery html(String html);

	public String html();

	public void show();

	public void hide();

	public void trigger(String string);

	public void submit();

	/************* core ***************/
	public DOMElement get(int index);

	public DOMElement[] get();

	public int index(String selector);

	public int index(FullJQuery jq);

	public int index(DOMElement element);

	public int index();

	public int size();

	public DOMElement[] toArray();

	/************* css ***************/
	public Object css(String propertyName);

	public FullJQuery css(String propertyName, Object value);

	public FullJQuery css(Map<String, Object> propertyMap);

	public int height();

	public FullJQuery height(int height);

	public int width();

	public FullJQuery width(int width);

	public int innerHeight();

	public int innerWidth();

	public int outerHeight();

	public int outerHeight(boolean withMargin);

	public int outerWidth();

	public int outerWidth(boolean withMargin);

	public Position offset();

	public Position position();

	public int scrollHeight();

	public FullJQuery scrollHeight(int height);

	public int scrollWidth();

	public FullJQuery scrollWidth(int width);

	/************* data ***************/
	public Object data(String key);

	public FullJQuery data(String key, Object value);

	public FullJQuery data(Map<String, Object> map);

	public FullJQuery removeData(String key);

	public FullJQuery removeData();

	/************* events ***************/
	public void change(ChangeListener changeListener);

	public void click(ClickListener clickListener);

	public void focus(FocusListener focusListener);

	public void keyup(KeyupListener keyupListener);

	public void text(Object txt);

	public FullJQuery appendTo(String target);

	/************* manipulation ***************/
	public void remove();

	/************* traversing ***************/
	public FullJQuery andSelf();

	public FullJQuery children();

	public FullJQuery children(String selector);

	public FullJQuery closest(String selector);

	public FullJQuery closest(FullJQuery jq);

	public FullJQuery closest(DOMElement element);

	public FullJQuery contents();

	public FullJQuery each(ElementIterationFunction function);

	public FullJQuery end();

	public FullJQuery eq(int index);

	public FullJQuery filter(String selector);

	public FullJQuery filter(FullJQuery jq);

	public FullJQuery filter(DOMElement element);

	public FullJQuery find(String selector);

	public FullJQuery find(FullJQuery jq);

	public FullJQuery find(DOMElement element);

	public FullJQuery first();

	public FullJQuery has(String selector);

	public FullJQuery has(DOMElement element);

	public FullJQuery is(String selector);

	public FullJQuery is(FullJQuery jq);

	public FullJQuery is(DOMElement element);

	public FullJQuery last();

	public FullJQuery next();

	public FullJQuery next(String selector);

	public FullJQuery nextAll();

	public FullJQuery nextAll(String selector);

	public FullJQuery nextUntil(String selector);

	public FullJQuery nextUntil(FullJQuery jq);

	public FullJQuery nextUntil(DOMElement element);

	public FullJQuery not(String selector);

	public FullJQuery not(FullJQuery jq);

	public FullJQuery parent();

	public FullJQuery parent(String selector);

	public FullJQuery parents();

	public FullJQuery parents(String selector);

	public FullJQuery parentsUntil(String selector);

	public FullJQuery parentsUntil(FullJQuery jq);

	public FullJQuery parentsUntil(DOMElement element);

	public FullJQuery prev();

	public FullJQuery prev(String selector);

	public FullJQuery prevAll();

	public FullJQuery prevAll(String selector);

	public FullJQuery prevUntil(String selector);

	public FullJQuery prevUntil(FullJQuery jq);

	public FullJQuery prevUntil(DOMElement element);

	public FullJQuery offsetParent();

	public FullJQuery siblings();

	public FullJQuery siblings(String selector);

	public FullJQuery slice(int start);

	public FullJQuery slice(int start, int end);
}