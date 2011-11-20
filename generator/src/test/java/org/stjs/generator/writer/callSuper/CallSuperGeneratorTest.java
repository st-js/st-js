package org.stjs.generator.writer.callSuper;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

public class CallSuperGeneratorTest {
	@Test
	public void testCallSuperConstructorObject() {
		// in fact the call to super should not be generated
		assertCodeContains(CallSuper1.class, "CallSuper1 = function(){}");
	}

	@Test
	public void testCallSuperConstructorParent() {
		assertCodeContains(CallSuper2.class, "CallSuper2 = function(arg){ this._super(null, arg);}");
	}

	@Test
	public void testOverrideAndCallSuper() {
		assertCodeContains(CallSuper3.class,
				"CallSuper3.prototype.instanceMethod = function(arg){ this._super(\"instanceMethod\", arg);}");
	}

	@Test
	public void testInstanceCallSuper() {
		assertCodeContains(CallSuper4.class,
				"CallSuper4.prototype.instanceMethod2 = function(arg){ this.instanceMethod(arg);}");
	}

	@Test
	public void testInstanceCallStaticSuperExplicit() {
		assertCodeContains(CallSuper5.class,
				"CallSuper5.prototype.instanceMethod = function(arg){ SuperClass.staticMethod(arg);}");
	}

	@Test
	public void testInstanceCallStaticSuperNotExplicit() {
		assertCodeContains(CallSuper6.class,
				"CallSuper6.prototype.instanceMethod = function(arg){ CallSuper6.staticMethod(arg);}");
	}

	@Test
	public void testStaticCallStaticSuperNotExplicit() {
		assertCodeContains(CallSuper7.class, "CallSuper7.staticMethod2 = function(arg){ CallSuper7.staticMethod(arg);}");
	}

	@Test
	public void testAddCallSuperConstructorDefined() {
		// call to super should be generated, when not defined explicitely
		assertCodeContains(CallSuper8.class, "CallSuper8 = function(x){this._super(null);var y = x;}");
	}

	@Test
	public void testAddCallSuperConstructorUndefined() {
		// call to super should be generated, when not defined explicitely
		assertCodeContains(CallSuper9.class, "CallSuper9 = function(){this._super(null);}");
	}

	@Test
	public void testCallSuperField() {
		// call to super should not be generated
		assertCodeContains(CallSuper10.class, "x = this.instanceField;");
	}

}
