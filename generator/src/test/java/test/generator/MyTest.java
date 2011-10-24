package test.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;

public class MyTest {
	@Test
	public void testBean() {
		generate(MyTestBean.class);
	}
}
