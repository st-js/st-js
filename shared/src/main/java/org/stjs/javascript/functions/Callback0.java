package org.stjs.javascript.functions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

/**
 * <p>Callback0 interface.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@JavascriptFunction
public interface Callback0 extends Callback {
	/**
	 * <p>$invoke.</p>
	 */
	@Template("invoke")
	public void $invoke();
}
