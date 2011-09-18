package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.callSuper.CallSuper1;
import test.generator.callSuper.CallSuper2;
import test.generator.callSuper.CallSuper3;
import test.generator.callSuper.CallSuper4;
import test.generator.callSuper.CallSuper5;
import test.generator.callSuper.CallSuper6;
import test.generator.callSuper.CallSuper7;

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
				"CallSuper4.prototype.instanceMethod2 = function(arg){ this._super(\"instanceMethod\", arg);}");
	}

	@Test
	public void testInstanceCallStaticSuperExplicit() {
		assertCodeContains(CallSuper5.class,
				"CallSuper5.prototype.instanceMethod = function(arg){ CallSuper5.staticMethod(arg);}");
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
}
