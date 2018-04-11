package org.stjs.generator.exec.annotations;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stjs.generator.MultipleFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.utils.Annotations;

public class AnnotationsTest extends AbstractStjsTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void testAnnotationClass() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("In TypeScript annotations aren't supported . Called 'stjs.getTypeAnnotation()'");

		generate(Annotation1.class);
		  /*
		assertEquals(
				10.0,
				executeAndReturnNumber(Annotation1.class,
						new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithMultipleValues").build()), 0);         */
	}

	@Test
	public void testAnnotationParam() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("In TypeScript annotations aren't supported . Called 'stjs.getParameterAnnotation()'");

		generate(Annotation5.class);
		/*assertEquals(
				5.0,
				executeAndReturnNumber(Annotation5.class,
						new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithMultipleValues").build()), 0); */
	}
}
