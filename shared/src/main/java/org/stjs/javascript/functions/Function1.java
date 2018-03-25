package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

/**
 * <p>Function1 interface.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@JavascriptFunction
public interface Function1<P1, R> extends Function<R> {
	/**
	 * <p>$invoke.</p>
	 *
	 * @param p1 a P1 object.
	 * @return a R object.
	 */
	@Template("invoke")
	public R $invoke(P1 p1);
}
