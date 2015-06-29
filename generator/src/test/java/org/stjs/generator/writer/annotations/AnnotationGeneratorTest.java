package org.stjs.generator.writer.annotations;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.GeneratorConfigurationBuilder;

public class AnnotationGeneratorTest extends AbstractStjsTest {
	@Test
	public void testSimpleClassAnnotation() {
		assertCodeContains(Annotation1.class, "_:{\"Immutable\":{}}", new GeneratorConfigurationBuilder().annotations("Immutable").build());
	}

	@Test
	public void testClassAnnotationWithMultipleValue() {
		assertCodeContains(Annotation2.class, "_:{\"MyAnnotations.WithMultipleValues\":{n:2}}",
				new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithMultipleValues").build());
	}

	@Test
	public void testClassAnnotationWithSingleValue() {
		assertCodeContains(Annotation2b.class, "_:{\"MyAnnotations.WithSingleValue\":{value:4}}", new GeneratorConfigurationBuilder()
				.annotations("MyAnnotations.WithSingleValue").build());
	}

	@Test
	public void testFieldAnnotationWithValue() {
		assertCodeContains(Annotation3.class, "field:{\"MyAnnotations.WithMultipleValues\":{n:2, m:\"100\"}}",
				new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithMultipleValues").build());
	}

	@Test
	public void testMethodAnnotationWithValue() {
		assertCodeContains(Annotation4.class, "method:{\"MyAnnotations.WithArrayValue\":{value:[\"a\", \"b\"]}}",
				new GeneratorConfigurationBuilder().annotations("MyAnnotations.WithArrayValue").build());
	}

	@Test
	public void testMethodParamAnnotationWithValue() {
		assertCodeContains(Annotation5.class, "method$1:{\"MyAnnotations.WithMultipleValues\":{n:2+3}}", new GeneratorConfigurationBuilder()
				.annotations("MyAnnotations.WithMultipleValues").build());
	}

	@Test
	public void testSkipSourceAnnotation() {
		assertCodeDoesNotContain(Annotation6.class, "Override");
	}

}
