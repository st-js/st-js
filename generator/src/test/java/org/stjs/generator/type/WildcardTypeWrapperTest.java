package org.stjs.generator.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.ParameterizedType;

import org.junit.Test;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.type.TypeWrappers;
import org.stjs.javascript.annotation.GlobalScope;

public class WildcardTypeWrapperTest {
	@Test
	public void testRegularClass() throws SecurityException, NoSuchMethodException {
		TypeWrapper wrapper =
				TypeWrappers.wrap(((ParameterizedType) WildcardTypeWrapper1.class.getDeclaredMethod("get")
						.getGenericReturnType()).getActualTypeArguments()[0]);
		assertEquals("?", wrapper.getSimpleName());
		assertEquals("?", wrapper.getExternalName());
		assertEquals("?", wrapper.getName());

		assertFalse(wrapper.isImportable());
		assertFalse(wrapper.isInnerType());
		assertTrue(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("field").isDefined());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
				.isDefined());
		assertFalse(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(Number.class))
				.isDefined());
	}

	@Test
	public void testInnerClass() throws SecurityException, NoSuchMethodException {
		TypeWrapper wrapper =
				TypeWrappers.wrap(((ParameterizedType) WildcardTypeWrapper2.class.getDeclaredMethod("get")
						.getGenericReturnType()).getActualTypeArguments()[0]);
		assertEquals("?", wrapper.getSimpleName());
		assertEquals("?", wrapper.getExternalName());
		assertEquals("?", wrapper.getName());

		assertFalse(wrapper.isImportable());
		// XXX not sure how to handle this as the variable type may be bound by several classes
		assertFalse(wrapper.isInnerType());
		assertTrue(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("field").isDefined());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
				.isDefined());
		assertFalse(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(Number.class))
				.isDefined());
	}

	@Test
	public void testGenericClass() throws SecurityException, NoSuchMethodException {
		TypeWrapper wrapper =
				TypeWrappers.wrap(((ParameterizedType) WildcardTypeWrapper3.class.getDeclaredMethod("get")
						.getGenericReturnType()).getActualTypeArguments()[0]);
		assertEquals("?", wrapper.getSimpleName());
		assertEquals("?", wrapper.getExternalName());
		assertEquals("?", wrapper.getName());

		assertFalse(wrapper.isImportable());
		assertFalse(wrapper.isInnerType());
		assertTrue(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("field").isDefined());
		assertEquals("V", wrapper.findField("field").getOrThrow().getType().getName());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
				.isDefined());
		assertFalse(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(Number.class))
				.isDefined());
	}
}
