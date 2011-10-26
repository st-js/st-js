package org.stjs.generator.writer.fields;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;


public class FieldsGeneratorTest {
	@Test
	public void testInstanceField() {
		assertCodeContains(Fields1.class, "Fields1.prototype.x = null;");
	}

	@Test
	public void testInstanceFieldAssigned() {
		assertCodeContains(Fields2.class, "Fields2.prototype.x = 2;");
	}

	@Test
	public void testMultipleInstanceField() {
		assertCodeContains(Fields3.class, "Fields3.prototype.x = 2; Fields3.prototype.y = 3;");
	}

	@Test
	public void testStaticField() {
		assertCodeContains(Fields4.class, "Fields4.x = 2;");
	}

	@Test(expected = JavascriptGenerationException.class)
	public void testFieldAndMethodTheSameName() {
		generate(Fields5.class);
	}

	@Test(expected = JavascriptGenerationException.class)
	public void testForbidInstanceFieldInit() {
		generate(Fields6.class);
	}

	@Test
	public void testAllowStaticFieldInit() {
		assertCodeContains(Fields7.class, "Fields7.x = {};");
	}

	@Test(expected = JavascriptGenerationException.class)
	public void testForbidInstanceFieldInitWithNonLiterals() {
		generate(Fields8.class);
	}

	@Test
	public void testParameterizedType() {
		assertCodeContains(Fields9.class, "Fields9.prototype.field = null;");
	}

	@Test
	public void testGeneric() {
		assertCodeContains(Fields10.class, "Fields10.prototype.field = null;");
	}

	@Test
	public void testAccessOuterStaticField() {
		assertCodeContains(Fields11.class, "a = Fields11.FIELD;");
	}
}
