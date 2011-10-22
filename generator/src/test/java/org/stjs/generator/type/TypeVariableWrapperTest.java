package org.stjs.generator.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.stjs.generator.scope.classloader.TypeWrapper;
import org.stjs.generator.scope.classloader.TypeWrappers;
import org.stjs.javascript.annotation.GlobalScope;

public class TypeVariableWrapperTest {
	@Test
	public void testRegularClass() throws SecurityException, NoSuchMethodException {
		TypeWrapper wrapper = TypeWrappers.wrap(TypeVariableWrapper1.class.getDeclaredMethod("get")
				.getGenericReturnType());
		assertEquals("T", wrapper.getSimpleName());
		assertEquals("T", wrapper.getExternalName());
		assertEquals("T", wrapper.getName());

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
		TypeWrapper wrapper = TypeWrappers.wrap(TypeVariableWrapper2.class.getDeclaredMethod("get")
				.getGenericReturnType());
		assertEquals("T", wrapper.getSimpleName());
		assertEquals("T", wrapper.getExternalName());
		assertEquals("T", wrapper.getName());

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
		TypeWrapper wrapper = TypeWrappers.wrap(TypeVariableWrapper3.class.getDeclaredMethod("get")
				.getGenericReturnType());
		assertEquals("T", wrapper.getSimpleName());
		assertEquals("T", wrapper.getExternalName());
		assertEquals("T", wrapper.getName());

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
