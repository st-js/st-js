package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.enums.Enums3;
import test.generator.enums.Enums4Switch;
import test.generator.switches.NativeTypeSwith;

public class SwitchGeneratorTest {

	@Test
	public void testSwitchEnumsInnerClass() {
		assertCodeContains(Enums3.class, "case Enums3.Value.a");
	}

	@Test
	public void testSwitchEnums() {
		assertCodeContains(Enums4Switch.class, "case Enums4.a");
	}
	
	@Test
	public void testSwitchInteger() {
		assertCodeContains(NativeTypeSwith.class, "case 1");
	}
}
