package org.stjs.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.stjs.testing.jstestdriver.JSTestDriverRunner;

@RunWith(JSTestDriverRunner.class)
public class JsTestDriverUnitTestExample {

	@Test
	public void shouldRetreiveString() {
		MyPojo pojo = new MyPojo("Foo");
		assertEquals("Foo", pojo.y);
	}

}
