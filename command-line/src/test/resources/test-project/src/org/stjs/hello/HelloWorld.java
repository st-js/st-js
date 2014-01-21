package org.stjs.hello;

import static org.stjs.javascript.Global.alert;
import static org.stjs.javascript.Global.window;

import org.stjs.javascript.dom.DOMEvent;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.dom.Form;
import org.stjs.javascript.dom.Input;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Function1;

public class HelloWorld {

	public static void main(String[] args) {
		window.onload = new Callback1<DOMEvent>() {
			public void $invoke(DOMEvent ev) {
				Form form = window.document.forms.$get(0);
				Element button = form.elements.$get("say");
				final Input text = form.elements.$get("to");
				button.onclick = new Function1<DOMEvent, Boolean>() {
					public Boolean $invoke(DOMEvent ev) {
						alert("Hello me to " + text.value);
						return true;
					}
				};
			}
		};
	}

}
