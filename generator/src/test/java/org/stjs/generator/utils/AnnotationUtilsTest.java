package org.stjs.generator.utils;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.stjs.generator.type.ClassWrapper;
import org.stjs.generator.type.TypeWrappers;

public class AnnotationUtilsTest {
	@Test
	public void testSimpleAnnotation() throws SecurityException, NoSuchMethodException {
		ClassWrapper cw = TypeWrappers.wrap(SimpleClass.class);
		assertNotNull(AnnotationUtils.getAnnotation(Thread.currentThread().getContextClassLoader(), SimpleClass.class,
				cw.findMethod("method", TypeWrappers.wrapMore(String.class, int.class)).getOrThrow(), Annotations.Annotation1.class));
	}

	@Test
	public void testAnnotationFromClass() throws SecurityException, NoSuchMethodException {
		ClassWrapper cw = TypeWrappers.wrap(SimpleClass.class);
		assertNotNull(AnnotationUtils.getAnnotation(Thread.currentThread().getContextClassLoader(), SimpleClass.class,
				cw.findMethod("method", TypeWrappers.wrapMore(String.class, int.class)).getOrThrow(), Annotations.Annotation2.class));
	}

	@Test
	public void testAnnotationFromClassMethod() throws SecurityException, NoSuchMethodException {
		ClassWrapper cw = TypeWrappers.wrap(SimpleClass.class);
		assertNotNull(AnnotationUtils.getAnnotation(Thread.currentThread().getContextClassLoader(), SimpleClass.class,
				cw.findMethod("method", TypeWrappers.wrapMore(String.class, int.class)).getOrThrow(), Annotations.Annotation3.class));
	}
}
