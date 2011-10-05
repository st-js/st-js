package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.statements.Statements1;
import test.generator.statements.Statements10;
import test.generator.statements.Statements2;
import test.generator.statements.Statements3;
import test.generator.statements.Statements4;
import test.generator.statements.Statements5;
import test.generator.statements.Statements6;
import test.generator.statements.Statements7;
import test.generator.statements.Statements8;
import test.generator.statements.Statements9;

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

	@Test
	public void testArray1() {
		assertCodeContains(Statements5.class, "x = [1,2,3]");
	}

	@Test
	public void testArray2() {
		assertCodeContains(Statements6.class, "method([1,2,3])");
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
}
