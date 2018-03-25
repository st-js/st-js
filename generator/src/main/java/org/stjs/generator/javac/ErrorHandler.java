package org.stjs.generator.javac;

/**
 * An implementation of the ErrorHandler interface can be registered with the ErrorReporter class to change the default
 * behavior on errors.
 *
 * @author acraciun
 * @version $Id: $Id
 */
public interface ErrorHandler {

	/**
	 * Log an error message and abort processing.
	 *
	 * @param msg
	 *            The error message to log.
	 */
	void errorAbort(String msg);

	/**
	 * <p>errorAbort.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	void errorAbort(String msg, Throwable cause);
}
