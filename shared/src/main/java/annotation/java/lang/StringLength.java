package annotation.java.lang;

import org.stjs.javascript.annotation.Template;

/**
 * this class is used to be able to annotate the {@link java.lang.String#length()} method.
 *
 * @author acraciun
 * @version $Id: $Id
 */
abstract public class StringLength {
	/**
	 * <p>length.</p>
	 *
	 * @return a int.
	 */
	@Template("toProperty")
	abstract public int length();
}
