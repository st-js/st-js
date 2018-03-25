package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

/**
 * <p>Function2 interface.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@JavascriptFunction
public interface Function2<P1, P2, R> extends Function<R> {
	/**
	 * <p>$invoke.</p>
	 *
	 * @param p1 a P1 object.
	 * @param p2 a P2 object.
	 * @return a R object.
	 */
	@Template("invoke")
	public R $invoke(P1 p1, P2 p2);
}
