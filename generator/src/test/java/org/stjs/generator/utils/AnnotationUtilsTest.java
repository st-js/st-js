package org.stjs.generator.utils;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AnnotationUtilsTest {
	@Test
	public void testSimpleAnnotation() throws SecurityException, NoSuchMethodException {
		assertNotNull(AnnotationUtils.getAnnotation(SimpleClass.class,
				SimpleClass.class.getMethod("method", String.class, int.class), Annotations.Annotation1.class));
	}

	@Test
	public void testAnnotationFromClass() throws SecurityException, NoSuchMethodException {
		assertNotNull(AnnotationUtils.getAnnotation(SimpleClass.class,
				SimpleClass.class.getMethod("method", String.class, int.class), Annotations.Annotation2.class));
	}

	@Test
	public void testAnnotationFromClassMethod() throws SecurityException, NoSuchMethodException {
		assertNotNull(AnnotationUtils.getAnnotation(SimpleClass.class,
				SimpleClass.class.getMethod("method", String.class, int.class), Annotations.Annotation3.class));
	}
}
