package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

@JavascriptFunction
public interface Function1<P1, R> {
	@Template("invoke")
	public R $invoke(P1 p1);
}
