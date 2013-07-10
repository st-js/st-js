package org.stjs.generator.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.type.TypeWrappers;
import org.stjs.javascript.annotation.GlobalScope;

public class GenericArrayTypeWrapperTest {
	@Test
	public void testWithTypeVariable() throws SecurityException, NoSuchMethodException {
		TypeWrapper wrapper =
				TypeWrappers.wrap(GenericArrayTypeWrapper1.class.getDeclaredMethod("get").getGenericReturnType());
		assertEquals("T[]", wrapper.getSimpleName());
		assertEquals("T[]", wrapper.getExternalName());
		assertEquals("T[]", wrapper.getName());

		assertFalse(wrapper.isImportable());
		assertFalse(wrapper.isInnerType());
		assertFalse(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("length").isDefined());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findMethod("equals", TypeWrappers.wrap(Object.class)).isDefined());
	}

	@Test
	public void testWithParameterizedType() throws SecurityException, NoSuchMethodException {
		TypeWrapper wrapper =
				TypeWrappers.wrap(GenericArrayTypeWrapper2.class.getDeclaredMethod("get").getGenericReturnType());
		assertEquals("GenericArrayTypeWrapper2[]", wrapper.getSimpleName());
		assertEquals("GenericArrayTypeWrapper2[]", wrapper.getExternalName());
		assertEquals("org.stjs.generator.type.GenericArrayTypeWrapper2[]", wrapper.getName());

		assertFalse(wrapper.isImportable());
		assertFalse(wrapper.isInnerType());
		assertFalse(wrapper.hasAnnotation(GlobalScope.class));

		assertTrue(wrapper.findField("length").isDefined());
		assertFalse(wrapper.findField("_field").isDefined());

		assertTrue(wrapper.findMethod("equals", TypeWrappers.wrap(Object.class)).isDefined());
	}
}
