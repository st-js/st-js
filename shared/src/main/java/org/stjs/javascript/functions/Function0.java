package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

@JavascriptFunction
public interface Function0<R> extends Function<R> {
	@Template("invoke")
	public R $invoke();
}
