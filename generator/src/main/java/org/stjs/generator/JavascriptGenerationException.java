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

	private final SourcePosition sourcePosition;

	public JavascriptGenerationException(File inputFile, SourcePosition sourcePosition, String message, Throwable cause) {
		super(message, cause);
		this.inputFile = inputFile;
		this.sourcePosition = sourcePosition;
	}

	public JavascriptGenerationException(File inputFile, SourcePosition sourcePosition, String message) {
		super(message);
		this.inputFile = inputFile;
		this.sourcePosition = sourcePosition;
	}

	public JavascriptGenerationException(File inputFile, SourcePosition sourcePosition, Throwable cause) {
		super(cause);
		this.inputFile = inputFile;
		this.sourcePosition = sourcePosition;
	}

	public File getInputFile() {
		return inputFile;
	}

	public SourcePosition getSourcePosition() {
		return sourcePosition;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("(").append(inputFile.getName());
		if (sourcePosition != null) {
			sb.append(":").append(sourcePosition.getLine());
		}
		sb.append(")");

		return sb.toString();
	}
}
