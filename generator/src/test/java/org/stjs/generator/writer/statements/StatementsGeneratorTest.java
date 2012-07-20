package org.stjs.generator.writer.statements;

import static org.junit.Assert.assertEquals;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeDoesNotContain;
import static org.stjs.generator.utils.GeneratorTestHelper.execute;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.utils.GeneratorTestHelper;

public class StatementsGeneratorTest {
	@Test
	public void testFor() {
		assertCodeContains(Statements1.class, "for (var i = 0; i < 10; i++) {");
	}

	@Test
	public void testForEach() {
		// XXX this is not exactly correct as arg here is the index not the value
		assertCodeContains(Statements2.class, "for (var arg in args) {");
	}

	@Test
	public void testWhile() {
		assertCodeContains(Statements3.class, "while(i < 5) {");
	}

	@Test
	public void testSwitch() {
		assertCodeContains(Statements4.class, "switch(i)");
		assertCodeContains(Statements4.class, "case 1: break;");
		assertCodeContains(Statements4.class, "default: break");
	}

	@Ignore
	// arrays are not allowed
	public void testArray1() {
		assertCodeContains(Statements5.class, "x = [1,2,3]");
	}

	@Test
	public void testArray2() {
		assertCodeContains(Statements6.class, "method(([1,2,3]))");
	}

	@Test
	public void testLineComment() {
		assertCodeContains(Statements7.class, "//line comment");
	}

	@Test
	public void testBlockComment() {
		assertCodeContains(Statements8.class, "/* * block comment */");
	}

	@Test
	public void testLiterals() {
		// "abc", "\"", "'", 'a', '\'', 1D, 2f, 1l);
		assertCodeContains(Statements9.class, "\"abc\", \"\\\"\", \"'\", 'a', '\\'', 1, 2, 1");
	}

	@Test
	public void testInstanceof() {
		assertCodeContains(Statements10.class, "arg.constructor == Statements10");
	}

	@Test
	public void testForEachArrayOneLine() {
		assertCodeContains(Statements11.class, "if (!(a).hasOwnProperty(i)) continue;");
	}

	@Test
	public void testForEachArrayBlock() {
		assertCodeContains(Statements12.class, "if (!(a).hasOwnProperty(i)) continue;");
	}

	@Test
	public void testForEachMapBlock() {
		assertCodeDoesNotContain(Statements13.class, "hasOwnProperty");
	}

	@Test
	public void testStaticInitializer() {
		assertCodeContains(Statements14.class, "{" + //
				"Statements14.instance = new Statements14();" + //
				"var n = Statements14.instance.method();" + //
				"}");
	}

	@Test(expected = JavascriptGenerationException.class)
	public void testInstanceInitializer() {
		generate(Statements15.class);
	}
	
	@Test
	public void testStaticInitializerContainment(){
		// We must do the weird (Number).intValue() because for some reason the execution returns the
		// integer 2 when run from eclipse, but return the double 2.0 when run from maven...
		assertEquals(2, ((Number)execute(Statements16.class)).intValue());
	}
}
