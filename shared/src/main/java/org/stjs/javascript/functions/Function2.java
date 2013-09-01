package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

@JavascriptFunction
public interface Function2<P1, P2, R> extends Function<R> {
	@Template("invoke")
	public R $invoke(P1 p1, P2 p2);
}
