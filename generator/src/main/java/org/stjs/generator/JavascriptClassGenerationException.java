package org.stjs.generator;

/**
 * throw this exception when you have the name of the class for which you generate the exception
 * @author acraciun
 */
public class JavascriptClassGenerationException extends STJSRuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final String className;

	public JavascriptClassGenerationException(String className, String message, Throwable cause) {
		super(message, cause);
		this.className = className;
	}

	public JavascriptClassGenerationException(String className, String message) {
		super(message);
		this.className = className;
	}

	public JavascriptClassGenerationException(String className, Throwable cause) {
		super(cause);
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	@Override
	public String toString() {
		return "JavascriptClassGenerationException [className=" + className + ", toString()=" + super.toString() + "]";
	}

}
