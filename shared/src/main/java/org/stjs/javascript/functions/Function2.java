package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;

@JavascriptFunction
public interface Function2<P1, P2, R> {
	public R $invoke(P1 p1, P2 p2);
}
