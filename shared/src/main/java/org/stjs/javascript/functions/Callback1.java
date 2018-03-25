package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

/**
 * <p>Callback1 interface.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@JavascriptFunction
public interface Callback1<P1> extends Callback {
	/**
	 * <p>$invoke.</p>
	 *
	 * @param p1 a P1 object.
	 */
	@Template("invoke")
	public void $invoke(P1 p1);
}
