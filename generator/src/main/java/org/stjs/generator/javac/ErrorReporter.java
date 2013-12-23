package org.stjs.generator.javac;

/**
 * Handle errors detected in utility classes. By default, the error reporter throws a RuntimeException, but clients of
 * the utility library may register a handler to change the behavior. For example, type checkers can direct errors to
 * the checkers.source.SourceChecker class.
 */
@SuppressWarnings("PMD")
public final class ErrorReporter {

	private static ErrorHandler handler;

	private ErrorReporter() {
		//
	}

	/**
	 * Register a handler to customize error reporting.
	 */
	public static void setHandler(ErrorHandler h) {
		handler = h;
	}

	/**
	 * Log an error message and abort processing. Call this method instead of raising an exception.
	 * 
	 * @param msg
	 *            The error message to log.
	 */
	public static void errorAbort(String msg) {
		if (handler != null) {
			handler.errorAbort(msg);
		} else {
			throw new RuntimeException(msg, new Throwable());
		}
	}

	public static void errorAbort(String msg, Throwable cause) {
		if (handler != null) {
			handler.errorAbort(msg, cause);
		} else {
			throw new RuntimeException(msg, cause);
		}
	}
}
