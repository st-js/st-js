package org.stjs.generator.writer.conversions;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

/**
 * (c) Swissquote 20.04.18
 *
 * @author sgoetz
 */
public class ConversionTest extends AbstractStjsTest {

	@Test
	public void testValueOfConversion() {
		String code = generate(Conversion1.class);

		assertCodeContains(code, "result1 = !!\"0\";");
		assertCodeContains(code, "result2 = \"\" + 25;");
		assertCodeContains(code, "result3 = new Number(\"25\").valueOf();");
	}
}
