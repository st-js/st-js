package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

@JavascriptFunction
public interface Callback0 extends Callback {
	@Template("invoke")
	public void $invoke();
}
