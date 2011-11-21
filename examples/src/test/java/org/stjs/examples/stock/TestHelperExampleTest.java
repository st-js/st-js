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

}
