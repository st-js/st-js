package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

/**
 * <p>Function3 interface.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@JavascriptFunction
public interface Function3<P1, P2, P3, R> extends Function<R> {
	/**
	 * <p>$invoke.</p>
	 *
	 * @param p1 a P1 object.
	 * @param p2 a P2 object.
	 * @param p3 a P3 object.
	 * @return a R object.
	 */
	@Template("invoke")
	public R $invoke(P1 p1, P2 p2, P3 p3);
}
