package org.stjs.generator.writer.synthetic;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

public class SyntheticTypeGeneratorTest {
	@Test(expected = JavascriptFileGenerationException.class)
	public void testForbidCallSuperConstructor() {
		generate(Synthetic1.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testForbidCallSuperMethodExplicitely() {
		generate(Synthetic2.class);
	}

	@Test
	public void testAllowCallSuperMethod() {
		assertCodeContains(Synthetic3.class, "method2=function(){this.method();}");
	}
}
