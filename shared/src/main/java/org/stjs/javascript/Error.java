package org.stjs.javascript;

public class Error extends RuntimeException {

	public String name = "Error";
	public String message = "";

	public Error(String message) {
		super(message);
		this.message = message;
	}

	/**
	 * Creates a native error
	 */
	Error(String type, String message) {
		this.message = message;
		this.name = type;
	}

	Error(String type, String message, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.name = type;
	}
}
