package org.stjs.generator.writer.fields;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.execute;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;

public class FieldsGeneratorTest {
	@Test
	public void testInstanceField() {
		assertCodeContains(Fields1.class, "prototype.x = null;");
	}

	@Test
	public void testInstanceFieldAssigned() {
		assertCodeContains(Fields2.class, "prototype.x = 2;");
	}

	@Test
	public void testInstanceFieldAssignedNegative() {
		assertCodeContains(Fields2b.class, "prototype.x = -2;");
	}

	@Test
	public void testMultipleInstanceField() {
		assertCodeContains(Fields3.class, "prototype.x = 2; prototype.y = 3;");
	}

	@Test
	public void testStaticField() {
		assertCodeContains(Fields4.class, "constructor.x = 2;");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testForbidInstanceFieldInit() {
		generate(Fields6.class);
	}

	@Test
	public void testAllowStaticFieldInit() {
		assertCodeContains(Fields7.class, "constructor.x = {};");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testForbidInstanceFieldInitWithNonLiterals() {
		generate(Fields8.class);
	}

	@Test
	public void testParameterizedType() {
		assertCodeContains(Fields9.class, "prototype.field = null;");
	}

	@Test
	public void testGeneric() {
		assertCodeContains(Fields10.class, "prototype.field = null;");
	}

	@Test
	public void testAccessOuterStaticField() {
		assertCodeContains(Fields11.class, "a = Fields11.FIELD;");
	}

	@Test
	public void testPrototypeProperty() {
		assertCodeContains(Fields12.class, "clazz=(String).prototype;");
	}

	@Test
	public void testPrivateFinalBooleanBug() {
		assertCodeContains(Fields13.class, "prototype.value = false;");
	}

	@Test
	public void testStaticFieldsDependencies() {
		Object result = execute(Fields14.class);
		assertNotNull(result);
		assertEquals(2, ((Number) result).intValue());
	}
}
