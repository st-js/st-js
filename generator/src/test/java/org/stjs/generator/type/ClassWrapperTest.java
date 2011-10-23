package org.stjs.generator.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.type.TypeWrappers;
import org.stjs.javascript.annotation.GlobalScope;

public class ClassWrapperTest {
	@Test
	public void testRegularClass() {
		TypeWrapper wrapper = TypeWrappers.wrap(ClassWrapper1.class);
		assertEquals("ClassWrapper1", wrapper.getSimpleName());
		assertEquals("ClassWrapper1", wrapper.getExternalName());
		assertEquals("org.stjs.generator.type.ClassWrapper1", wrapper.getName());

		assertTrue(wrapper.isImportable());
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
	public void testInnerClass() {
		TypeWrapper wrapper = TypeWrappers.wrap(ClassWrapper2.InnerType.class);
		assertEquals("InnerType", wrapper.getSimpleName());
		assertEquals("ClassWrapper2.InnerType", wrapper.getExternalName());
		assertEquals("org.stjs.generator.type.ClassWrapper2$InnerType", wrapper.getName());

		assertTrue(wrapper.isImportable());
		assertTrue(wrapper.isInnerType());
		assertTrue(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("field").isDefined());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
				.isDefined());
		assertFalse(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(Number.class))
				.isDefined());
	}

	@Test
	public void testGenericClass() {
		TypeWrapper wrapper = TypeWrappers.wrap(ClassWrapper3.class);
		assertEquals("ClassWrapper3", wrapper.getSimpleName());
		assertEquals("ClassWrapper3", wrapper.getExternalName());
		assertEquals("org.stjs.generator.type.ClassWrapper3", wrapper.getName());

		assertTrue(wrapper.isImportable());
		assertFalse(wrapper.isInnerType());
		assertTrue(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("field").isDefined());
		assertEquals("T", wrapper.findField("field").getOrThrow().getType().getName());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(String.class))
				.isDefined());
		assertFalse(wrapper.findMethod("method", TypeWrappers.wrap(int.class), TypeWrappers.wrap(Number.class))
				.isDefined());
	}
}
