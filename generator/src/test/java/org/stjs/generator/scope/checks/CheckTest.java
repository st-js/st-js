package org.stjs.generator.scope.checks;

import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;

public class CheckTest {
	@Test(
			expected = JavascriptGenerationException.class)
	public void testFieldAndMethodTheSameName() {
		generate(Checks1.class);
	}

	@Test(
			expected = JavascriptGenerationException.class)
	public void testFieldTheSameInSuper() {
		generate(Checks2.class);
	}

	@Test(
			expected = JavascriptGenerationException.class)
	public void testFieldTheSameMethoInSuper() {
		generate(Checks3.class);
	}

	@Test(
			expected = JavascriptGenerationException.class)
	public void testMethodOverwritePrivate() {
		generate(Checks4.class);
	}

	@Test
	public void testMethodOverwritePublic() {
		generate(Checks5.class);
	}

	@Test(
			expected = JavascriptGenerationException.class)
	public void testMethodOverload() {
		generate(Checks6.class);
	}

	@Test
	public void testMethodOverwritePublicGenerics() {
		generate(Checks7.class);
	}

	@Test
	public void testFinalAtMethodLevel() {
		generate(Checks8a.class);
	}

	@Test(
			expected = JavascriptGenerationException.class)
	public void testFinalNotAtMethodLevel() {
		generate(Checks8b.class);
	}

	@Test
	public void testFinalField() {
		generate(Checks8c.class);
	}

	@Test
	public void testFinalParam() {
		generate(Checks8d.class);
	}
}
