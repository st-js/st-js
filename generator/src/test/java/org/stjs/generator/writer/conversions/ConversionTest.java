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

	@Test
	public void testParseNumberConversion() {
		String code = generate(Conversion2.class);

		assertCodeContains(code, "result1 = parseInt(\"1\");");
		assertCodeContains(code, "result2 = parseInt(\"2\");");
		assertCodeContains(code, "result3 = parseInt(\"3\");");
		assertCodeContains(code, "result4 = parseInt(\"4\");");
		assertCodeContains(code, "result5 = parseFloat(\"5.0\");");
		assertCodeContains(code, "result6 = parseFloat(\"2.0\");");
		assertCodeContains(code, "result7 = isNaN(400);");
	}

	@Test
	public void testXXValueConversion() {
		String code = generate(Conversion3.class);
		assertCodeContains(code, "result1 = parseInt(entry1);");
		assertCodeContains(code, "result2 = parseInt(entry2);");
		assertCodeContains(code, "result3 = parseInt(entry3);");
		assertCodeContains(code, "result4 = parseFloat(entry4);");
		assertCodeContains(code, "result5 = parseFloat(entry5);");
		assertCodeContains(code, "result6 = parseInt(entry6);");
		assertCodeContains(code, "result7 = isNaN(entry6);");
	}
}
