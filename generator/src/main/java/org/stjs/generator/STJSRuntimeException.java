package org.stjs.generator;

/**
 * this class is used to wrap checked exceptions that occur within the STJS code
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class STJSRuntimeException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for STJSRuntimeException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public STJSRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * <p>Constructor for STJSRuntimeException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public STJSRuntimeException(String message) {
		super(message);
	}

	/**
	 * <p>Constructor for STJSRuntimeException.</p>
	 *
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public STJSRuntimeException(Throwable cause) {
		super(cause);
	}

}
