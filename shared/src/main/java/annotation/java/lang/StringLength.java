package annotation.java.lang;

import org.stjs.javascript.annotation.Template;

/**
 * this class is used to be able to annotate the {@link String#length()} method.
 * 
 * @author acraciun
 * 
 */
abstract public class StringLength {
	@Template("toProperty")
	abstract public int length();
}
