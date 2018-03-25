package org.stjs.generator;

/**
 * throw this exception when you have the name of the class for which you generate the exception
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class JavascriptClassGenerationException extends STJSRuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final String className;

	/**
	 * <p>Constructor for JavascriptClassGenerationException.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public JavascriptClassGenerationException(String className, String message, Throwable cause) {
		super(message, cause);
		this.className = className;
	}

	/**
	 * <p>Constructor for JavascriptClassGenerationException.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @param message a {@link java.lang.String} object.
	 */
	public JavascriptClassGenerationException(String className, String message) {
		super(message);
		this.className = className;
	}

	/**
	 * <p>Constructor for JavascriptClassGenerationException.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public JavascriptClassGenerationException(String className, Throwable cause) {
		super(cause);
		this.className = className;
	}

	/**
	 * <p>Getter for the field <code>className</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getClassName() {
		return className;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "JavascriptClassGenerationException [className=" + className + ", toString()=" + super.toString() + "]";
	}

}
