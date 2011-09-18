package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.statements.Statements1;
import test.generator.statements.Statements2;
import test.generator.statements.Statements3;
import test.generator.statements.Statements4;
import test.generator.statements.Statements5;
import test.generator.statements.Statements6;

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

}
