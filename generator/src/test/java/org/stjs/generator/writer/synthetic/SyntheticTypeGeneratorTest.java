package org.stjs.generator.writer.synthetic;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class SyntheticTypeGeneratorTest extends AbstractStjsTest {
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
