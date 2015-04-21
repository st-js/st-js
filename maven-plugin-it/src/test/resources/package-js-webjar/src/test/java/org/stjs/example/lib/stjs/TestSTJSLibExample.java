package org.stjs.example.lib.stjs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.stjs.testing.driver.STJSTestDriverRunner;

import static org.junit.Assert.assertEquals;

@RunWith(STJSTestDriverRunner.class)
public class TestSTJSLibExample {

	@Test
	public void testAdd() {
		STJSLibExample ex = new STJSLibExample();
		assertEquals(5, ex.add(2, 3));
	}
}
