package org.stjs.generator.scope.methods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.type.TypeWrappers;
import org.stjs.generator.utils.Option;

public class MethodResolutionTest {
	@Test
	public void testMethodOverload1() {
		TypeWrapper type = TypeWrappers.wrap(MethodResolution1.class);
		Option<MethodWrapper> w = type.findMethod("method", TypeWrappers.wrap(Double.class));
		assertTrue(w.isDefined());
		assertEquals(Number.class, w.getOrThrow().getParameterTypes()[0].getType());
	}

	@Test
	public void testVararg() {
		TypeWrapper type = TypeWrappers.wrap(MethodResolution2.class);
		Option<MethodWrapper> w = type.findMethod("method", TypeWrappers.wrap(Double.class));
		assertTrue(w.isDefined());
	}

	@Test
	public void testGenericVarargNoParam() {
		TypeWrapper type = TypeWrappers.wrap(MethodResolution3.class);
		Option<MethodWrapper> w = type.findMethod("method");
		assertTrue(w.isDefined());
	}

	@Test
	public void testGenericVarargTwoParam() {
		TypeWrapper type = TypeWrappers.wrap(MethodResolution3.class);
		Option<MethodWrapper> w = type.findMethod("method", TypeWrappers.wrap(Double.class),
				TypeWrappers.wrap(Double.class));
		assertTrue(w.isDefined());
	}

	@Test
	public void testNumbers1() {
		TypeWrapper type = TypeWrappers.wrap(MethodResolution4.class);
		Option<MethodWrapper> w = type.findMethod("method", TypeWrappers.wrap(Integer.class));
		assertTrue(w.isDefined());
	}

	@Test
	public void testNumbers2() {
		TypeWrapper type = TypeWrappers.wrap(MethodResolution5.class);
		Option<MethodWrapper> w = type.findMethod("method", TypeWrappers.wrap(int.class));
		assertTrue(w.isDefined());
	}

	@Test
	public void testGenericTypeParam() {
		TypeWrapper type = TypeWrappers.wrap(MethodResolution6.class);
		Option<MethodWrapper> w = type.findMethod("method", TypeWrappers.wrap(int.class));
		assertTrue(w.isDefined());
		assertEquals(int.class, w.getOrThrow().getReturnType().getType());
	}
}
