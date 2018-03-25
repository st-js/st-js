package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

/**
 * <p>Callback2 interface.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@JavascriptFunction
public interface Callback2<P1, P2> extends Callback {
	/**
	 * <p>$invoke.</p>
	 *
	 * @param p1 a P1 object.
	 * @param p2 a P2 object.
	 */
	@Template("invoke")
	public void $invoke(P1 p1, P2 p2);
}
