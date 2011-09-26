package org.stjs.examples.stock;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.stjs.testing.jstestdriver.JSTestDriverRunner;

@RunWith(JSTestDriverRunner.class)
public class EnumExampleTest {

	@Test
	public void shouldSwitchOnEnums() throws Exception {
		assertEquals(2, new EnumExample().getNum(EnumExample.MyEnum.B));
		assertEquals(1, new EnumExample().getNum(EnumExample.MyEnum.A));
	}

}
