package org.stjs.generator.writer.typeDesc;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

public class TypeDescTest extends AbstractStjsTest {
	@Test
	public void testBasicField() {
		assertCodeContains(TypeDesc1.class, "{}, \"TypeDesc1\");");
	}

	@Test
	public void testArrayOfBasicField() {
		assertCodeContains(TypeDesc2.class, "{x:{name:\"Array\", arguments:[null]}}");
	}

	@Test
	public void testNonBasicField() {
		assertCodeContains(TypeDesc3.class, "{x:\"Date\"}");
	}

	@Test
	public void testArrayOfNonBasicField() {
		assertCodeContains(TypeDesc4.class, "{x:{name:\"Array\", arguments:[\"Date\"]}}");
	}

	@Test
	public void testMapOfNonBasicField() {
		assertCodeContains(TypeDesc5.class, "{x:{name:\"Map\", arguments:[null,\"Date\"]}}");
	}

	@Test
	public void testEnum() {
		assertCodeContains(TypeDesc6.class, "{x:{name:\"Enum\", arguments:[\"TypeDesc6.Type\"]}}");
	}

	@Test
	public void testWildcards() {
		assertCodeContains(TypeDesc7.class, "{field:{name:\"MyType1\", arguments:[\"Object\"]}}");
	}

	@Test
	public void testClassGenerationContainsClassName() {
		String result = (String) execute(TypeDesc8_get_simpleClassName.class);
		Assert.assertEquals("TypeDesc8_get_simpleClassName", result);
	}

	@Test
	public void testInnerClassClassName() {
		assertCodeContains(TypeDesc9_innerClass_simpleClassName.class,
				"constructor.InnerClass = stjs.extend(constructor.InnerClass, null, [], null, {}, {}, \"TypeDesc9_innerClass_simpleClassName.InnerClass\");");
	}
}
