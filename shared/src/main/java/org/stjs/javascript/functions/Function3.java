package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

@JavascriptFunction
public interface Function3<P1, P2, P3, R> extends Function<R> {
	@Template("invoke")
	public R $invoke(P1 p1, P2 p2, P3 p3);
}
