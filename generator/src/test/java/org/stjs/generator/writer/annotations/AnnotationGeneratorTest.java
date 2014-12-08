package org.stjs.generator.writer.annotations;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

public class AnnotationGeneratorTest {
	@Test
	public void testSimpleClassAnnotation() {
		assertCodeContains(Annotation1.class, "_:{\"Immutable\":{}}");
	}

	@Test
	public void testClassAnnotationWithMultipleValue() {
		assertCodeContains(Annotation2.class, "_:{\"MyAnnotations.WithMultipleValues\":{n:2}}");
	}

	@Test
	public void testClassAnnotationWithSingleValue() {
		assertCodeContains(Annotation2b.class, "_:{\"MyAnnotations.WithSingleValue\":{value:4}}");
	}

	@Test
	public void testFieldAnnotationWithValue() {
		assertCodeContains(Annotation3.class, "field:{\"MyAnnotations.WithMultipleValues\":{n:2, m:\"100\"}}");
	}

	@Test
	public void testMethodAnnotationWithValue() {
		assertCodeContains(Annotation4.class, "method:{\"MyAnnotations.WithArrayValue\":{value:[\"a\", \"b\"]}}");
	}

	@Test
	public void testMethodParamAnnotationWithValue() {
		assertCodeContains(Annotation5.class, "method$1:{\"MyAnnotations.WithMultipleValues\":{n:2+3}}");
	}

}
