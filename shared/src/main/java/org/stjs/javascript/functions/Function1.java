package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;

@JavascriptFunction
public interface Function1<P1, R> {
	public R $invoke(P1 p1);
}
