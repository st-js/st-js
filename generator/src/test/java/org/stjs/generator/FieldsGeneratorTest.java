package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;

import test.generator.fields.Fields1;
import test.generator.fields.Fields2;
import test.generator.fields.Fields3;
import test.generator.fields.Fields4;
import test.generator.fields.Fields5;
import test.generator.fields.Fields6;
import test.generator.fields.Fields7;
import test.generator.fields.Fields8;

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
}
