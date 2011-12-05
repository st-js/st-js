package org.stjs.examples.inheritance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.stjs.testing.driver.STJSTestDriverRunner;

@RunWith(STJSTestDriverRunner.class)
public class InheritanceTest {
	public static class A {
		public int method1(int n) {
			return n;
		}

		public int method2(int n) {
			return method1(n + 1);
		}
	}

	public static class B extends A {
		@Override
		public int method1(int n) {
			return super.method1(n + 1);
		}

		@Override
		public int method2(int n) {
			return super.method2(n + 1);
		}
	}

	public static class C extends B {//
	}

	@Test
	public void testReenteringSuper() {
		B b = new B();
		assertEquals(4, b.method2(1));
	}

	@Test
	public void testCopyPrototype() {
		C c = new C();
		assertEquals(2, c.method1(1));
	}

}
