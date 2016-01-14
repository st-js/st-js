package org.stjs.generator.lib.string;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

import static org.junit.Assert.assertEquals;

public class StringMethodsTest extends AbstractStjsTest {

	@Test
	public void testStartsWith() {
		assertEquals(true, execute(String1.class));
	}

	@Test
	public void testStartsWithOffset() {
		assertEquals(true, execute(String2.class));
	}

	@Test
	public void testEndsWith() {
		assertEquals(true, execute(String3.class));
	}

	@Test
	public void testMatches() {
		assertEquals(true, execute(String4.class));
	}

	@Test
	public void testNotMatches() {
		assertEquals(false, execute(String5.class));
	}

	@Test
	public void testCompareTo() {
		assertEquals(-1.0, executeAndReturnNumber(String6.class), 0);
	}

	@Test
	public void testCompareToIgnoreCase() {
		assertEquals(-1.0, executeAndReturnNumber(String7.class), 0);
	}

	@Test
	public void testEqualsIgnoreCase() {
		assertEquals(true, execute(String8.class));
	}

	@Test
	public void testCodePointAt() {
		assertEquals(98.0, executeAndReturnNumber(String9.class), 0);
	}

	@Test
	public void testReplaceAll() {
		assertEquals("xbcx", execute(String10.class));
	}

	@Test
	public void testReplaceFirst() {
		assertEquals("xbca", execute(String11.class));
	}

	@Test
	public void testRegionMatches() {
		assertEquals(true, execute(String12.class));
	}

	@Test
	public void testRegionMatchesIgnoreCase() {
		assertEquals(true, execute(String13.class));
	}

	@Test
	public void testStringOverloadMethodNames() {
		assertCodeContains(String14_overloaded_methods.class, "" +
				"        test.regionMatches$boolean_int_String_int_int(false, 1, \"bc\", 0, 2);\n" +
				"        test.regionMatches$int_String_int_int(1, \"bc\", 0, 2);\n" +
				"        test.indexOf$String(\"ab\");\n" +
				"        test.indexOf$String_int(\"ab\", 0);\n" +
				"        test.indexOf$int(1);\n" +
				"        test.indexOf$int_int(1, 0);");
	}

    @Test
    public void testValueOf() throws Exception {
        String result = (String) execute(String15_valueOf.class);
        Assert.assertEquals("" +
                        "boolean: true\n" +
                        "int: 1\n" +
                        "long: 1\n" +
                        "char: a\n" +
                        "Object: an object\n" +
                        "float: 1.2\n" +
                        "double: 1.3",
                result);
    }

}
