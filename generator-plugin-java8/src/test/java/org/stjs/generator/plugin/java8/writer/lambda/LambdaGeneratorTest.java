package org.stjs.generator.plugin.java8.writer.lambda;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

public class LambdaGeneratorTest {
	@Test
	public void testLambdaParamExpression() {
		assertCodeContains(Lambda1.class, "method(function(x){return x+1;})");
	}

	@Test
	public void testLambdaNoParamExpression() {
		assertCodeContains(Lambda2.class, "method(function(){return 1;})");
	}

	@Test
	public void testLambdaNoReturnExpression() {
		assertCodeContains(Lambda3.class, "method(function(x){var y = x;})");
	}

	@Test
	public void testLambdaTypeResolution() {
		assertCodeContains(Lambda4.class, "method(function(x){return x.length + 1;})");
	}

}
