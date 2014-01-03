package org.stjs.generator;

import java.util.List;

public class MultipleFileGenerationException extends STJSRuntimeException {
	private static final long serialVersionUID = 1L;
	private final List<JavascriptFileGenerationException> exceptions;

	public MultipleFileGenerationException(List<JavascriptFileGenerationException> exceptions) {
		super("Multiple exception");
		this.exceptions = exceptions;
	}

	public List<JavascriptFileGenerationException> getExceptions() {
		return exceptions;
	}

}
