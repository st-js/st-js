package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

@JavascriptFunction
public interface Callback5<P1, P2, P3, P4, P5> extends Callback {
	@Template("invoke")
	void $invoke(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5);
}
