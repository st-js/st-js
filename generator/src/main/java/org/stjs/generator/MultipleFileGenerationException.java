package org.stjs.generator;

import java.util.List;

public class MultipleFileGenerationException extends JavascriptFileGenerationException {
	private static final long serialVersionUID = 1L;
	private final List<JavascriptFileGenerationException> exceptions;

	public MultipleFileGenerationException(List<JavascriptFileGenerationException> exceptions) {
		super(exceptions.get(0).getSourcePosition(), exceptions.get(0).getMessage(), exceptions.get(0).getCause());
		this.exceptions = exceptions;
	}

	public List<JavascriptFileGenerationException> getExceptions() {
		return exceptions;
	}

}
