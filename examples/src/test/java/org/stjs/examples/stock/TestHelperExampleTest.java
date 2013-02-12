package org.stjs.examples.stock;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.stjs.testing.driver.STJSTestDriverRunner;

@RunWith(STJSTestDriverRunner.class)
public class TestHelperExampleTest {

	@Test
	public void goodTest() {
		MyPojo pojo = new MyPojo("Foo");
		assertEquals("Foo", pojo.y);
	}

	@Ignore
	// switch too @Test to see how failure works
	public void failedTest() {
		MyPojo pojo = new MyPojo("Foo");
		assertEquals("xFoo", pojo.y);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void doubleTest() {
		assertEquals(1.0, 1.0);
		assertEquals("should be equals", 1.0, 1.0);
		assertEquals(1.0, 1.0, 0.01);
		assertEquals("shoud be equals", 1.0, 1.0, 0.01);
	}

	@Test(expected = RuntimeException.class)
	public void exceptionTest() {
		throw new RuntimeException("abc");
	}

	@Test(expected = Exception.class)
	public void exceptionTestParent() {
		throw new RuntimeException("abc");
	}

}
