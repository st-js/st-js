package org.stjs.generator.writer.switches;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.writer.enums.Enums4_switch;
import org.stjs.generator.writer.enums.Enums7_switch_case;

public class SwitchGeneratorTest extends AbstractStjsTest {

	@Test
	public void testSwitchEnums() {
		assertCodeContains(Enums4_switch.class, "case Enums4_switch.Enums4.a");
	}

	@Test
	public void testSwitchEnumsFromParam() {
		assertCodeContains(Enums7_switch_case.class, "case SimpleEnum.FIRST");
	}

	@Test
	public void testSwitchInteger() {
		assertCodeContains(NativeTypeSwith.class, "case 1");
	}
}
