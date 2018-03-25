package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

/**
 * <p>Function0 interface.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@JavascriptFunction
public interface Function0<R> extends Function<R> {
	/**
	 * <p>$invoke.</p>
	 *
	 * @return a R object.
	 */
	@Template("invoke")
	public R $invoke();
}
