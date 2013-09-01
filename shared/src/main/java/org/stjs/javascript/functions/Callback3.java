package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

@JavascriptFunction
public interface Callback3<P1, P2, P3> extends Callback {
	@Template("invoke")
	public void $invoke(P1 p1, P2 p2, P3 p3);
}