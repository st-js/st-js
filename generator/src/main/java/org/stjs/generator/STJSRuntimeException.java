package org.stjs.generator;

/**
 * this class is used to wrap checked exceptions that occur within the STJS code
 * 
 * @author acraciun
 * 
 */
public class STJSRuntimeException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public STJSRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public STJSRuntimeException(String message) {
		super(message);
	}

	public STJSRuntimeException(Throwable cause) {
		super(cause);
	}

}
