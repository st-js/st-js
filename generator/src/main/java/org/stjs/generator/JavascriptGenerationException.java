package org.stjs.generator;

import java.io.File;

/**
 * This is the exception thrown by the Generator.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class JavascriptGenerationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final File inputFile;

	public JavascriptGenerationException(File inputFile, String message, Throwable cause) {
		super(message, cause);
		this.inputFile = inputFile;
	}

	public JavascriptGenerationException(File inputFile, String message) {
		super(message);
		this.inputFile = inputFile;
	}

	public JavascriptGenerationException(File inputFile, Throwable cause) {
		super(cause);
		this.inputFile = inputFile;
	}

	public File getInputFile() {
		return inputFile;
	}

}
