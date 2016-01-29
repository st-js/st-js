package org.stjs.generator.writer.annotations;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.GeneratorConfigurationBuilder;

public class AnnotationGeneratorTest extends AbstractStjsTest {
	@Test
	public void testSimpleClassAnnotation() {
		assertCodeContains(Annotation1.class,
				new GeneratorConfigurationBuilder().annotations("Immutable").build(),
				"_:{\"Immutable\":{}}");
	}

	@Test
	public void testClassAnnotationWithMultipleValue() {
		assertCodeContains(Annotation2.class,
				new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithMultipleValues").build(),
				"_:{\"MyAnnotations.WithMultipleValues\":{n:2}}"
				);
	}

	@Test
	public void testClassAnnotationWithSingleValue() {
		assertCodeContains(Annotation2b.class,
				new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithSingleValue").build(),
				"_:{\"MyAnnotations.WithSingleValue\":{value:4}}");
	}

	@Test
	public void testFieldAnnotationWithValue() {
		assertCodeContains(Annotation3.class,
				new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithMultipleValues").build(),
				"field:{\"MyAnnotations.WithMultipleValues\":{n:2, m:\"100\"}}");
	}

	@Test
	public void testMethodAnnotationWithValue() {
		assertCodeContains(Annotation4.class,
				new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithArrayValue").build(),
				"method:{\"MyAnnotations.WithArrayValue\":{value:[\"a\", \"b\"]}}");
	}

	@Test
	public void testMethodParamAnnotationWithValue() {
		assertCodeContains(Annotation5.class,
				new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithMultipleValues").build(),
				"method$1:{\"MyAnnotations.WithMultipleValues\":{n:2+3}}");
	}

	@Test
	public void testSkipSourceAnnotation() {
		assertCodeDoesNotContain(Annotation6.class, "Override");
	}

}
