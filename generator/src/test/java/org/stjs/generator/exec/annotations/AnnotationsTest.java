package org.stjs.generator.exec.annotations;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stjs.generator.MultipleFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

public class AnnotationsTest extends AbstractStjsTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void testAnnotationClass() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("In TypeScript annotations aren't supported . Called 'stjs.getTypeAnnotation()'");

		generate(Annotation1.class);
	}

	@Test
	public void testAnnotationParam() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("In TypeScript annotations aren't supported . Called 'stjs.getParameterAnnotation()'");

		generate(Annotation5.class);
	}
}
