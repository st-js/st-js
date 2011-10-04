package org.stjs.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.stjs.testing.driver.STJSTestDriverRunner;

@RunWith(STJSTestDriverRunner.class)
public class JsTestDriverUnitTestExample {

	@Test
	public void goodTest() {
		MyPojo pojo = new MyPojo("Foo");
		assertEquals("Foo", pojo.y);
	}

	@Test
	public void failedTest() {
		MyPojo pojo = new MyPojo("Foo");
		assertEquals("xFoo", pojo.y);
	}

}
