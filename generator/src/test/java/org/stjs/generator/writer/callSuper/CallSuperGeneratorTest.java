package org.stjs.generator.writer.callSuper;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class CallSuperGeneratorTest extends AbstractStjsTest {
	@Test
	public void testCallSuperConstructorObject() {
		// in fact the call to super should not be generated
		assertCodeContains(CallSuper1.class, "class CallSuper1 {}");
	}

	@Test
	public void testCallSuperConstructorParent() {
		assertCodeContains(CallSuper2.class, "class CallSuper2 extends SuperClass2 { constructor(arg){ super(arg); } }");
	}

	@Test
	public void testOverrideAndCallSuper() {
		assertCodeContains(CallSuper3.class, "instanceMethod(arg){ super.instanceMethod(arg);}");
	}

	@Test
	public void testInstanceCallSuper() {
		assertCodeContains(CallSuper4.class, "instanceMethod2(arg){ this.instanceMethod(arg);}");
	}

	@Test
	public void testInstanceCallSuperImplementInterface() {
		assertCodeContains(CallSuper4b.class, "instanceMethod2(arg){ this.instanceMethod(arg);}");
	}

	@Test
	public void testInstanceCallSuperIFromAnonymous() {
		assertCodeContains(CallSuper4c.class, "someMethod(){ super.instanceMethod(arg);}");
	}

	@Test
	public void testInstanceCallStaticSuperExplicit() {
		assertCodeContains(CallSuper5.class, "instanceMethod(arg){ SuperClass.staticMethod(arg);}");
	}

	@Test
	public void testInstanceCallStaticSuperNotExplicit() {
		assertCodeContains(CallSuper6.class, "instanceMethod(arg){ SuperClass.staticMethod(arg);}");
	}

	@Test
	public void testStaticCallStaticSuperNotExplicit() {
		assertCodeContains(CallSuper7.class, //
				"static staticMethod2(arg){ SuperClass.staticMethod(arg);}");
	}

	@Test
	public void testAddCallSuperConstructorDefined() {
		// call to super should be generated, when not defined explicitely
		assertCodeContains(CallSuper8.class, "class CallSuper8 extends SuperClass { constructor(x){ super(); let y = x; } }");
	}

	@Test
	public void testAddCallSuperConstructorUndefined() {
		// call to super should be generated, when not defined explicitely
		assertCodeContains(CallSuper9.class, "class CallSuper9 extends SuperClass { x: number = 0; }");
	}

	@Test
	public void testCallSuperField() {
		// call to super should not be generated
		assertCodeContains(CallSuper10.class, "x = this.instanceField;");
	}

}
