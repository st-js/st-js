package org.stjs.generator.plugin.java8.writer.lambda;

import static org.junit.Assert.assertEquals;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.execute;
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

	@Test
	public void testLambaAccessFieldOuterScope() {
		assertCodeContains(Lambda5.class, "var c = stjs.bind(this, function() {return this.field + 1;});");
	}

	@Test
	public void testLambaAccessQualifiedFieldOuterScope() {
		assertCodeContains(Lambda5b.class, "var c = stjs.bind(this, function() {return this.field + 1;});");
	}

	@Test
	public void testLambaAccessMethodOuterScope() {
		assertCodeContains(Lambda6.class, "var c = stjs.bind(this, function() {return this.outerMethod() + 1;});");
	}

	@Test
	public void testLambaAccessMethodOuterScopeExecute() {
		assertEquals(Integer.valueOf(4), execute(Lambda6b.class));
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testLambaAccessLoopFinal() {
		generate(Lambda7.class);
	}

	@Test
	public void testLambaAccessOutsideLoop() {
		//this should not raise an exception
		generate(Lambda8.class);
	}

	@Test
	public void testLambaAccessInsideLambda() {
		//this should not raise an exception
		generate(Lambda9.class);
	}
}
