/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.javascript.jquery;

import org.stjs.javascript.Array;
import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Callback2;
import org.stjs.javascript.functions.Callback3;
import org.stjs.javascript.functions.Callback4;
import org.stjs.javascript.functions.Function2;
import org.stjs.javascript.utils.NameValue;

/**
 * jquery interface for all jquery method + all used plugins in a web app. it can be mocked easily for testing purposes. Compatible with jquery
 * 1.6
 */
@SyntheticType
public interface JQueryCore<FullJQuery extends JQueryCore<?>> {
	/************* ajax ***************/
	//since 1.5
	public JQueryXHR ajax(String url);

	//since 1.5
	public JQueryXHR ajax(String url, AjaxParams ajaxParams);

	//since 1.0
	public JQueryXHR ajax(AjaxParams ajaxParams);

	public FullJQuery ajaxStart(Callback3<Event, JQueryXHR, AjaxParams> handler);

	public FullJQuery ajaxStop(Callback3<Event, JQueryXHR, AjaxParams> handler);

	public FullJQuery ajaxSend(Callback3<Event, JQueryXHR, AjaxParams> handler);

	public FullJQuery ajaxComplete(Callback3<Event, JQueryXHR, AjaxParams> handler);

	public FullJQuery ajaxSuccess(Callback3<Event, JQueryXHR, AjaxParams> handler);

	public FullJQuery ajaxError(Callback4<Event, JQueryXHR, AjaxParams, String> handler);

	public FullJQuery load(String url, Map<String, String> data, Callback3<Object, String, JQueryXHR> handler);

	/************* effects ***************/
	public FullJQuery animate(Map<String, String> properties);

	public FullJQuery animate(Map<String, String> properties, Object duration, String easing, Callback1<Element> complete);

	public FullJQuery animate(Map<String, String> properties, AnimateOptions options);

	public FullJQuery animate(Map<String, String> properties, Object duration);

	public FullJQuery clearQueue(String queueName);

	public FullJQuery delay(int duration, String queueName);

	public FullJQuery dequeue(String queueName);

	//since 1.0
	public FullJQuery fadeIn();

	//since 1.0
	public FullJQuery fadeIn(Object duration);

	//since 1.0
	public FullJQuery fadeIn(Object duration, Callback1<Element> complete);

	//since 1.4.3
	public FullJQuery fadeIn(Object duration, String easing);

	//since 1.4.3
	public FullJQuery fadeIn(Object duration, String easing, Callback1<Element> complete);

	//since 1.0
	public FullJQuery fadeOut();

	//since 1.0
	public FullJQuery fadeOut(Object duration);

	//since 1.0
	public FullJQuery fadeOut(Object duration, Callback1<Element> complete);

	//since 1.4.3
	public FullJQuery fadeOut(Object duration, String easing);

	//since 1.4.3
	public FullJQuery fadeOut(Object duration, String easing, Callback1<Element> complete);

	public FullJQuery fadeTo(Object duration, double opacity);

	public FullJQuery fadeTo(Object duration, double opacity, Callback1<Element> complete);

	public FullJQuery fadeToggle(Object duration);

	public FullJQuery fadeToggle(Object duration, String easing);

	public FullJQuery fadeToggle(Object duration, String easing, Callback1<Element> complete);

	public FullJQuery hide(Object duration);

	public FullJQuery hide(Object duration, String easing, Callback1<Element> complete);

	public FullJQuery show(Object duration);

	public FullJQuery show(Object duration, Callback1<Element> complete);

	public FullJQuery show(Object duration, String easing, Callback1<Element> complete);

	public FullJQuery queue(String queueName);

	//since 1.0
	public FullJQuery slideDown();

	//since 1.0
	public FullJQuery slideDown(Object duration);

	//since 1.0
	public FullJQuery slideDown(Object duration, Callback1<Element> complete);

	//since 1.4.3
	public FullJQuery slideDown(Object duration, String easing);

	//since 1.4.3
	public FullJQuery slideDown(Object duration, String easing, Callback1<Element> complete);

	//since 1.0
	public FullJQuery slideToggle();

	//since 1.0
	public FullJQuery slideToggle(Object duration);

	//since 1.0
	public FullJQuery slideToggle(Object duration, Callback1<Element> complete);

	//since 1.4.3
	public FullJQuery slideToggle(Object duration, String easing);

	//since 1.4.3
	public FullJQuery slideToggle(Object duration, String easing, Callback1<Element> complete);

	//since 1.0
	public FullJQuery slideUp();

	//since 1.0
	public FullJQuery slideUp(Object duration);

	//since 1.0
	public FullJQuery slideUp(Object duration, Callback1<Element> complete);

	//since 1.4.3
	public FullJQuery slideUp(Object duration, String easing);

	//since 1.4.3
	public FullJQuery slideUp(Object duration, String easing, Callback1<Element> complete);

	public FullJQuery stop();

	public FullJQuery stop(boolean clearQueue, boolean jumpToEnd);

	public FullJQuery toggle(boolean showOrHide);

	public FullJQuery toggle(Object duration);

	public FullJQuery toggle(Object duration, String easing, Callback1<Element> complete);

	/************* attributes ***************/
	public FullJQuery addClass(String className);

	public boolean hasClass(String className);

	public FullJQuery removeClass();

	public FullJQuery removeClass(String className);

	public FullJQuery toggleClass(String className);

	public FullJQuery toggleClass(String className, boolean on);

	public Object val();

	public FullJQuery val(Object d);

	//since 1.0
	public Object attr(String attrName);

	//since 1.0
	public FullJQuery attr(String name, Object value);

	//since 1.0
	public FullJQuery attr(Map<String, String> $map);

	//since 1.1
	//public FullJQuery attr(String attrName, Function2<Integer, Object, Object>);

	public FullJQuery removeAttr(String name);

	public FullJQuery prop(String name, Object value);

	public FullJQuery removeProp(String name);

	public Object prop(String attrName);

	public FullJQuery html(String html);

	public String html();

	public FullJQuery show();

	public FullJQuery hide();

	/************* core ***************/
	public Element get(int index);

	public Element[] get();

	public int index(String selector);

	public int index(JQueryCore<?> jq);

	public int index(Element element);

	public int index();

	public int size();

	public Array<Element> toArray();

	/************* css ***************/
	public Object css(String propertyName);

	public FullJQuery css(String propertyName, Object value);

	public FullJQuery css(Map<String, ? extends Object> propertyMap);

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

	public FullJQuery offset(Position placement);

	public FullJQuery offset(Function2<Integer, Position, Position> offsetFunction);

	public Position position();

	public int scrollHeight();

	public FullJQuery scrollHeight(int height);

	public int scrollWidth();

	public FullJQuery scrollWidth(int width);

	public int scrollLeft();

	public FullJQuery scrollLeft(int left);

	public int scrollTop();

	public FullJQuery scrollTop(int top);

	/************* data ***************/
	public Object data(String key);

	public FullJQuery data(String key, Object value);

	public FullJQuery data(Map<String, Object> map);

	public FullJQuery removeData(String key);

	public FullJQuery removeData();

	/************* events ***************/
	public FullJQuery bind(String eventType, EventHandler handler);

	public FullJQuery bind(String eventType, Map<String, Object> eventData, EventHandler handler);

	public FullJQuery bind(Map<String, EventHandler> handlers);

	public FullJQuery blur(EventHandler handler);

	public FullJQuery blur();

	public FullJQuery change(EventHandler handler);

	public FullJQuery change();

	public FullJQuery click(EventHandler handler);

	public FullJQuery click();

	public FullJQuery dblclick(EventHandler handler);

	public FullJQuery dblclick();

	public FullJQuery delegate(String selector, String eventType, EventHandler handler);

	public FullJQuery delegate(String selector, String eventType, Map<String, Object> eventData, EventHandler handler);

	public FullJQuery delegate(String selector, Map<String, EventHandler> handlers);

	public FullJQuery die();

	public FullJQuery die(String eventType);

	public FullJQuery die(Map<String, EventHandler> handlers);

	public FullJQuery error(EventHandler handler);

	public FullJQuery focus(EventHandler handler);

	public FullJQuery focus();

	public FullJQuery focusin(EventHandler handler);

	public FullJQuery focusout(EventHandler handler);

	public FullJQuery hover(EventHandler handlerIn, EventHandler handlerOut);

	public FullJQuery hover(EventHandler handler);

	public FullJQuery keydown(EventHandler handler);

	public FullJQuery keydown();

	public FullJQuery keypress();

	public FullJQuery keypress(EventHandler handler);

	public FullJQuery keyup();

	public FullJQuery keyup(EventHandler handler);

	public FullJQuery live(String eventType, EventHandler handler);

	public FullJQuery live(String eventType, Map<String, Object> eventData, EventHandler handler);

	public FullJQuery live(Map<String, EventHandler> handlers);

	public FullJQuery load(EventHandler handler);

	public FullJQuery mousedown(EventHandler handler);

	public FullJQuery mousedown();

	public FullJQuery mouseenter(EventHandler handler);

	public FullJQuery mouseenter();

	public FullJQuery mouseleave(EventHandler handler);

	public FullJQuery mouseleave();

	public FullJQuery mousemove(EventHandler handler);

	public FullJQuery mousemove();

	public FullJQuery mouseout(EventHandler handler);

	public FullJQuery mouseout();

	public FullJQuery mouseover(EventHandler handler);

	public FullJQuery mouseover();

	public FullJQuery mouseup(EventHandler handler);

	public FullJQuery mouseup();

	public FullJQuery one(String eventType, EventHandler handler);

	public FullJQuery one(String eventType, Map<String, Object> eventData, EventHandler handler);

	public FullJQuery ready(EventHandler handler);

	public FullJQuery resize(EventHandler handler);

	public FullJQuery resize();

	public FullJQuery scroll(EventHandler handler);

	public FullJQuery scroll();

	public FullJQuery select(EventHandler handler);

	public FullJQuery select();

	public FullJQuery submit(EventHandler handler);

	public FullJQuery submit();

	public FullJQuery toggle(EventHandler handlerIn, EventHandler handlerOut);

	public FullJQuery trigger(String eventType);

	public FullJQuery trigger(String eventType, Map<String, String> extraParams);

	public FullJQuery trigger(Event event);

	public FullJQuery triggerHandler(String eventType, Map<String, String> extraParams);

	public FullJQuery unbind();

	public FullJQuery unbind(String eventType);

	public FullJQuery unbind(String eventType, EventHandler handler);

	public FullJQuery unbind(Event event);

	//since 1.4.2
	public FullJQuery undelegate();

	//since 1.6
	public FullJQuery undelegate(String namespace);

	//since 1.4.2
	public FullJQuery undelegate(String selector, String eventType);

	//since 1.4.2
	public FullJQuery undelegate(String selector, String eventType, EventHandler handler);

	//since 1.4.2
	public FullJQuery undelegate(String selector, String eventType, Map<String, Object> eventData, EventHandler handler);

	//since 1.4.3
	public FullJQuery undelegate(String selector, Map<String, EventHandler> handlers);

	public FullJQuery unload(EventHandler handler);

	/************* manipulation ***************/
	public FullJQuery after(String selector);

	public FullJQuery after(JQueryCore<?> jq);

	public FullJQuery after(Element element);

	public FullJQuery append(Object content);

	public FullJQuery append(JQueryCore<?> jq);

	public FullJQuery append(Element element);

	public FullJQuery appendTo(String selector);

	public FullJQuery appendTo(JQueryCore<?> jq);

	public FullJQuery appendTo(Element element);

	public FullJQuery before(String selector);

	public FullJQuery before(JQueryCore<?> jq);

	public FullJQuery before(Element element);

	public FullJQuery clone();

	public FullJQuery clone(boolean withDataAndEvents, boolean deepWithDataAndEvents);

	public FullJQuery detach(String selector);

	public FullJQuery empty();

	public FullJQuery insertAfter(String selector);

	public FullJQuery insertAfter(JQueryCore<?> jq);

	public FullJQuery insertAfter(Element element);

	public FullJQuery insertBefore(String selector);

	public FullJQuery insertBefore(JQueryCore<?> jq);

	public FullJQuery insertBefore(Element element);

	public FullJQuery prepend(String selector);

	public FullJQuery prepend(JQueryCore<?> jq);

	public FullJQuery prepend(Element element);

	public FullJQuery prependTo(String selector);

	public FullJQuery prependTo(JQueryCore<?> jq);

	public FullJQuery prependTo(Element element);

	public FullJQuery remove();

	public FullJQuery detach();

	public FullJQuery remove(String selector);

	public FullJQuery replaceAll(String selector);

	public FullJQuery replaceWith(Element element);

	public FullJQuery replaceWith(String selector);

	public FullJQuery replaceWith(JQueryCore<?> jq);

	public String serialize();

	public Array<NameValue> serializeArray();

	public FullJQuery text(Object txt);

	public String text();

	public FullJQuery unwrap();

	public FullJQuery wrap(Element element);

	public FullJQuery wrap(String selector);

	public FullJQuery wrap(JQueryCore<?> jq);

	public FullJQuery wrapAll(Element element);

	public FullJQuery wrapAll(String selector);

	public FullJQuery wrapAll(JQueryCore<?> jq);

	public FullJQuery wrapInner(Element element);

	public FullJQuery wrapInner(String selector);

	public FullJQuery wrapInner(JQueryCore<?> jq);

	/************* traversing ***************/
	public FullJQuery andSelf();

	public FullJQuery add(JQueryCore<?> selector);

	public FullJQuery add(String selector);

	public FullJQuery children();

	public FullJQuery children(String selector);

	public FullJQuery closest(String selector);

	public FullJQuery closest(JQueryCore<?> jq);

	public FullJQuery closest(Element element);

	public FullJQuery contents();

	public FullJQuery each(Callback2<Integer, Element> function);

	public <T> FullJQuery each(Function2<Integer, Element, T> function);

	public FullJQuery end();

	public FullJQuery eq(int index);

	public FullJQuery filter(String selector);

	public FullJQuery filter(JQueryCore<?> jq);

	public FullJQuery filter(Element element);

	public FullJQuery find(String selector);

	public FullJQuery find(JQueryCore<?> jq);

	public FullJQuery find(Element element);

	public FullJQuery first();

	public FullJQuery has(String selector);

	public FullJQuery has(Element element);

	public boolean is(String selector);

	public boolean is(JQueryCore<?> jq);

	public boolean is(Element element);

	public FullJQuery last();

	public FullJQuery map(Function2<Integer, Element, Element> callback);

	public FullJQuery next();

	public FullJQuery next(String selector);

	public FullJQuery nextAll();

	public FullJQuery nextAll(String selector);

	public FullJQuery nextUntil(String selector);

	public FullJQuery nextUntil(JQueryCore<?> jq);

	public FullJQuery nextUntil(Element element);

	public FullJQuery not(String selector);

	public FullJQuery not(JQueryCore<?> jq);

	public FullJQuery parent();

	public FullJQuery parent(String selector);

	public FullJQuery parents();

	public FullJQuery parents(String selector);

	//since 1.4
	public FullJQuery parentsUntil(String selector);

	//since 1.4
	public FullJQuery parentsUntil(String selector, String filter);

	//since 1.6
	public FullJQuery parentsUntil(JQueryCore<?> jq);

	//since 1.6
	public FullJQuery parentsUntil(JQueryCore<?> jq, String filter);

	//since 1.6
	public FullJQuery parentsUntil(Element element);

	//since 1.6
	public FullJQuery parentsUntil(Element element, String filter);

	public FullJQuery prev();

	public FullJQuery prev(String selector);

	public FullJQuery prevAll();

	public FullJQuery prevAll(String selector);

	public FullJQuery prevUntil(String selector);

	public FullJQuery prevUntil(JQueryCore<?> jq);

	public FullJQuery prevUntil(Element element);

	public FullJQuery offsetParent();

	public FullJQuery siblings();

	public FullJQuery siblings(String selector);

	public FullJQuery slice(int start);

	public FullJQuery slice(int start, int end);

	/************* properties ***************/

	//since 1.3
	public Element context = null;

	//since 1.0
	public int length = 0;

	//since 1.3
	public String selector = "";

	//since 1.0
	public String version = "";

}