package org.stjs.generator.javac;

/**
 * An implementation of the ErrorHandler interface can be registered with the ErrorReporter class to change the default
 * behavior on errors.
 */
public interface ErrorHandler {

	/**
	 * Log an error message and abort processing.
	 * 
	 * @param msg
	 *            The error message to log.
	 */
	void errorAbort(String msg);

	void errorAbort(String msg, Throwable cause);
}
