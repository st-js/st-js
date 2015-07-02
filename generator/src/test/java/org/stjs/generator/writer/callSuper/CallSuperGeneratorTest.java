package org.stjs.generator.writer.callSuper;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class CallSuperGeneratorTest extends AbstractStjsTest {
	@Test
	public void testCallSuperConstructorObject() {
		// in fact the call to super should not be generated
		assertCodeContains(CallSuper1.class, "CallSuper1 = function(){}");
	}

	@Test
	public void testCallSuperConstructorParent() {
		assertCodeContains(CallSuper2.class, "CallSuper2 = function(arg){ SuperClass2.call(this, arg);}");
	}

	@Test
	public void testOverrideAndCallSuper() {
		assertCodeContains(CallSuper3.class, "prototype.instanceMethod = function(arg){ SuperClass.prototype.instanceMethod.call(this, arg);}");
	}

	@Test
	public void testInstanceCallSuper() {
		assertCodeContains(CallSuper4.class, "prototype.instanceMethod2 = function(arg){ this.instanceMethod(arg);}");
	}

	@Test
	public void testInstanceCallSuperImplementInterface() {
		assertCodeContains(CallSuper4b.class, "prototype.instanceMethod2 = function(arg){ this.instanceMethod(arg);}");
	}

	@Test
	public void testInstanceCallSuperIFromAnonymous() {
		assertCodeContains(CallSuper4c.class, "prototype.someMethod = function(){ SuperClass3.prototype.instanceMethod.call(this, arg);}");
	}

	@Test
	public void testInstanceCallStaticSuperExplicit() {
		assertCodeContains(CallSuper5.class, "prototype.instanceMethod = function(arg){ SuperClass.staticMethod(arg);}");
	}

	@Test
	public void testInstanceCallStaticSuperNotExplicit() {
		assertCodeContains(CallSuper6.class, "prototype.instanceMethod = function(arg){ SuperClass.staticMethod(arg);}");
	}

	@Test
	public void testStaticCallStaticSuperNotExplicit() {
		assertCodeContains(CallSuper7.class, //
				"constructor.staticMethod2 = function(arg){ SuperClass.staticMethod(arg);}");
	}

	@Test
	public void testAddCallSuperConstructorDefined() {
		// call to super should be generated, when not defined explicitely
		assertCodeContains(CallSuper8.class, "CallSuper8 = function(x){SuperClass.call(this);var y = x;}");
	}

	@Test
	public void testAddCallSuperConstructorUndefined() {
		// call to super should be generated, when not defined explicitely
		assertCodeContains(CallSuper9.class, "CallSuper9 = function(){SuperClass.call(this);}");
	}

	@Test
	public void testCallSuperField() {
		// call to super should not be generated
		assertCodeContains(CallSuper10.class, "x = this.instanceField;");
	}

}
