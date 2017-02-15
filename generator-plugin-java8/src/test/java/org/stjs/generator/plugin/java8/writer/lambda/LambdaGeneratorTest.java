package org.stjs.generator.plugin.java8.writer.lambda;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

public class LambdaGeneratorTest extends AbstractStjsTest {
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
		assertEquals(4, ((Number) execute(Lambda6b.class)).intValue());
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testLambdaAccessLoopFinal() {
		generate(Lambda7.class);
	}

	@Test
	public void testLambdaAccessLoopFinalBug() {
		// b was wrongly reported
		generate(Lambda7b.class);
	}

	@Test
	public void testLambdaAccessLoopFinalBug2() {
		// b was wrongly reported
		generate(Lambda7c.class);
	}

	@Test
	public void testLambaAccessOutsideLoop() {
		// this should not raise an exception
		generate(Lambda8.class);
	}

	@Test
	public void testLambaAccessInsideLambda() {
		// this should not raise an exception
		generate(Lambda9.class);
	}

	@Test
	public void testGenerateBindOuterScope() {
		assertCodeContains(Lambda10.class, "stjs.bind");
	}

	@Test
	public void testDoNotGenerateBindStaticOuterScope() {
		assertCodeDoesNotContain(Lambda11.class, "stjs.bind");
	}

	@Test
	public void testDoNotGenerateBindLambdaParam() {
		assertCodeDoesNotContain(Lambda12.class, "stjs.bind");
	}

	@Test
	public void testDoNotGenerateBindLambdaAnnnonInit() {
		assertCodeDoesNotContain(Lambda13.class, "stjs.bind");
	}

	@Test
	public void testUsingTHISParamAndOuterScope() {
		//		assertCodeContains(Lambda14.class, "method(function(){})");

		double n = executeAndReturnNumber(Lambda14.class);
		assertEquals(15.0, n, 0);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testOneMethodInterfaceNonLambda() {
		generate(Lambda15.class);
	}

}
