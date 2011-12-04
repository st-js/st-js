package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;

@JavascriptFunction
public interface Callback2<P1, P2> {
	public void $invoke(P1 p1, P2 p2);
}