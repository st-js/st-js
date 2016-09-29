package org.stjs.javascript.html5.dom;

import org.stjs.javascript.dom.DOMEvent;
import org.stjs.javascript.functions.Function1;

public abstract class Html extends org.stjs.javascript.dom.Html {
	// events
	public Function1<DOMEvent, Boolean> onafterprint;
	public Function1<DOMEvent, Boolean>onbeforeprint;
	public Function1<DOMEvent, Boolean>onbeforeunload;
	public Function1<DOMEvent, Boolean>onhashchange;
	public Function1<DOMEvent, Boolean>onload;
	public Function1<DOMEvent, Boolean>onmessage;
	public Function1<DOMEvent, Boolean>onoffline;
	public Function1<DOMEvent, Boolean>ononline;
	public Function1<DOMEvent, Boolean>onpopstate;
	public Function1<DOMEvent, Boolean>onredo;
	public Function1<DOMEvent, Boolean>onresize;
	public Function1<DOMEvent, Boolean>onstorage;
	public Function1<DOMEvent, Boolean>onundo;
	public Function1<DOMEvent, Boolean>onunload;
}
