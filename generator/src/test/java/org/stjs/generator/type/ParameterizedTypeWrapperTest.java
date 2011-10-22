package org.stjs.generator.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.stjs.generator.scope.classloader.TypeWrapper;
import org.stjs.generator.scope.classloader.TypeWrappers;
import org.stjs.javascript.annotation.GlobalScope;

public class ParameterizedTypeWrapperTest {
	@Test
	public void testRegularClass() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		TypeWrapper wrapper = TypeWrappers.wrap(ParameterizedTypeWrapper1.class.getDeclaredField("get")
				.getGenericType());
		assertEquals("ParameterizedTypeWrapper1", wrapper.getSimpleName());
		assertEquals("ParameterizedTypeWrapper1", wrapper.getExternalName());
		assertEquals("org.stjs.generator.type.ParameterizedTypeWrapper1", wrapper.getName());

		assertTrue(wrapper.isImportable());
		assertFalse(wrapper.isInnerType());
		assertTrue(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("field").isDefined());
		assertEquals("java.lang.String", wrapper.findField("field").getOrThrow().getType().getName());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
				.isDefined());
		assertEquals("java.lang.String",
				wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
						.getOrThrow().getReturnType().getName());
		assertFalse(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(Number.class))
				.isDefined());
	}

	@Test
	public void testInnerClass() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		TypeWrapper wrapper = TypeWrappers.wrap(ParameterizedTypeWrapper2.class.getDeclaredField("get")
				.getGenericType());
		assertEquals("InnerType", wrapper.getSimpleName());
		assertEquals("ParameterizedTypeWrapper2.InnerType", wrapper.getExternalName());
		assertEquals("org.stjs.generator.type.ParameterizedTypeWrapper2$InnerType", wrapper.getName());

		assertTrue(wrapper.isImportable());
		assertTrue(wrapper.isInnerType());
		assertTrue(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("field").isDefined());
		assertEquals("java.lang.String", wrapper.findField("field").getOrThrow().getType().getName());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
				.isDefined());
		assertEquals("java.lang.String",
				wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
						.getOrThrow().getReturnType().getName());
		assertFalse(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(Number.class))
				.isDefined());
	}

	@Test
	public void testGenericClass() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		TypeWrapper wrapper = TypeWrappers.wrap(ParameterizedTypeWrapper3.class.getDeclaredMethod("get")
				.getGenericReturnType());
		assertEquals("ParameterizedTypeWrapper3", wrapper.getSimpleName());
		assertEquals("ParameterizedTypeWrapper3", wrapper.getExternalName());
		assertEquals("org.stjs.generator.type.ParameterizedTypeWrapper3", wrapper.getName());

		assertTrue(wrapper.isImportable());
		assertFalse(wrapper.isInnerType());
		assertTrue(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("field").isDefined());
		assertEquals("java.lang.String", wrapper.findField("field").getOrThrow().getType().getName());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findField("field2").isDefined());
		assertEquals("V", wrapper.findField("field2").getOrThrow().getType().getName());

		assertTrue(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
				.isDefined());
		assertEquals("java.lang.String",
				wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
						.getOrThrow().getReturnType().getName());
		assertFalse(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(Number.class))
				.isDefined());
	}

	@Test
	public void testNormalClassInheritsGenericClass() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		TypeWrapper wrapper = TypeWrappers.wrap(ParameterizedTypeWrapper4.class.getDeclaredField("get")
				.getGenericType());
		assertEquals("InnerType", wrapper.getSimpleName());
		assertEquals("ParameterizedTypeWrapper4.InnerType", wrapper.getExternalName());
		assertEquals("org.stjs.generator.type.ParameterizedTypeWrapper4$InnerType", wrapper.getName());

		assertTrue(wrapper.isImportable());
		assertTrue(wrapper.isInnerType());
		assertTrue(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("field").isDefined());
		assertEquals("java.lang.String", wrapper.findField("field").getOrThrow().getType().getName());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
				.isDefined());
		assertEquals("java.lang.String",
				wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
						.getOrThrow().getReturnType().getName());
		assertFalse(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(Number.class))
				.isDefined());
	}

	@Test
	public void testGenericClassInheritsGenericClass() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		TypeWrapper wrapper = TypeWrappers.wrap(ParameterizedTypeWrapper5.class.getDeclaredField("get")
				.getGenericType());
		assertEquals("InnerType", wrapper.getSimpleName());
		assertEquals("ParameterizedTypeWrapper5.InnerType", wrapper.getExternalName());
		assertEquals("org.stjs.generator.type.ParameterizedTypeWrapper5$InnerType", wrapper.getName());

		assertTrue(wrapper.isImportable());
		assertTrue(wrapper.isInnerType());
		assertTrue(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("field").isDefined());
		assertEquals("java.lang.String", wrapper.findField("field").getOrThrow().getType().getName());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
				.isDefined());
		assertEquals("java.lang.String",
				wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
						.getOrThrow().getReturnType().getName());
		assertFalse(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(Number.class))
				.isDefined());
	}
}
