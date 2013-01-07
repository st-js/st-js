package org.stjs.examples.stock;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.stjs.testing.driver.STJSMultiTestDriverRunner;

@RunWith(STJSMultiTestDriverRunner.class)
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

}
