package org.stjs.generator;

import java.util.List;

/**
 * <p>MultipleFileGenerationException class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class MultipleFileGenerationException extends JavascriptFileGenerationException {
	private static final long serialVersionUID = 1L;
	private final List<JavascriptFileGenerationException> exceptions;

	/**
	 * <p>Constructor for MultipleFileGenerationException.</p>
	 *
	 * @param exceptions a {@link java.util.List} object.
	 */
	public MultipleFileGenerationException(List<JavascriptFileGenerationException> exceptions) {
		super(exceptions.get(0).getSourcePosition(), exceptions.get(0).getMessage(), exceptions.get(0).getCause());
		this.exceptions = exceptions;
	}

	/**
	 * <p>Getter for the field <code>exceptions</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<JavascriptFileGenerationException> getExceptions() {
		return exceptions;
	}

}
