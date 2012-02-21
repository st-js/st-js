package org.stjs.generator.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import org.junit.Test;
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
	public void testNormalClassInheritsGenericClassWithArray() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		TypeWrapper wrapper = TypeWrappers.wrap(ParameterizedTypeWrapper8a.class.getDeclaredField("get")
				.getGenericType());
		assertEquals("ParameterizedTypeWrapper8a", wrapper.getSimpleName());

		assertTrue(wrapper.findField("field").isDefined());
		TypeWrapper fieldType = wrapper.findField("field").getOrThrow().getType();
		assertTrue(fieldType instanceof ParameterizedTypeWrapper);
		assertEquals(1, ((ParameterizedTypeWrapper) fieldType).getActualTypeArguments().length);
		assertEquals("java.lang.String", ((ParameterizedTypeWrapper) fieldType).getActualTypeArguments()[0].getName());
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

	@Test
	public void testCopyWildcardBound() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		TypeWrapper wrapper = TypeWrappers.wrap(ParameterizedTypeWrapper6.class.getDeclaredField("get")
				.getGenericType());
		assertEquals("ParameterizedTypeWrapper6", wrapper.getSimpleName());
		assertEquals("ParameterizedTypeWrapper6", wrapper.getExternalName());
		assertEquals("org.stjs.generator.type.ParameterizedTypeWrapper6", wrapper.getName());

		assertTrue(wrapper.findField("field").isDefined());
		assertBoundedWildcard(wrapper.findField("field").getOrThrow().getType().getType(),
				"class org.stjs.generator.type.ParameterizedTypeWrapper6");

		assertTrue(wrapper.findMethod("method").isDefined());
		assertBoundedWildcard(wrapper.findMethod("method").getOrThrow().getReturnType().getType(),
				"class org.stjs.generator.type.ParameterizedTypeWrapper6");

	}

	@Test
	public void testAddWildcardBoundwhenRawCall() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		TypeWrapper wrapper = TypeWrappers.wrap(ParameterizedTypeWrapper7.class.getDeclaredField("get")
				.getGenericType());
		assertEquals("ParameterizedTypeWrapper7", wrapper.getSimpleName());
		assertEquals("ParameterizedTypeWrapper7", wrapper.getExternalName());
		assertEquals("org.stjs.generator.type.ParameterizedTypeWrapper7", wrapper.getName());

		assertTrue(wrapper.findField("field").isDefined());
		assertBoundedWildcard(wrapper.findField("field").getOrThrow().getType().getType(),
				"class org.stjs.generator.type.ParameterizedTypeWrapper7");

		assertTrue(wrapper.findMethod("method").isDefined());
		assertBoundedWildcard(wrapper.findMethod("method").getOrThrow().getReturnType().getType(),
				"class org.stjs.generator.type.ParameterizedTypeWrapper7");

	}

	private void assertBoundedWildcard(Type type, String boundTypeName) {
		WildcardType argType = (WildcardType) type;

		// the wildcard arg must be bound with a parameterized type with the same raw type
		assertNotNull(argType.getUpperBounds());
		assertEquals(1, argType.getUpperBounds().length);
		assertTrue(argType.getUpperBounds()[0] instanceof ParameterizedType);
		assertEquals(boundTypeName, ((ParameterizedType) argType.getUpperBounds()[0]).getRawType().toString());
	}
}
