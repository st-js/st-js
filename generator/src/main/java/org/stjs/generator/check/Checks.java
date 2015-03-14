package org.stjs.generator.check;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.MultipleFileGenerationException;

/**
 * keeps the list of errors found during the check of a Java AST.
 * 
 * @author acraciun
 * 
 */
public class Checks {
	private final List<JavascriptFileGenerationException> errors = new ArrayList<JavascriptFileGenerationException>();

	public void addError(JavascriptFileGenerationException javascriptFileGenerationException) {
		errors.add(javascriptFileGenerationException);
	}

	public void check() {
		// TODO tmp
		if (!errors.isEmpty()) {
			throw new MultipleFileGenerationException(errors);
		}
	}
}
