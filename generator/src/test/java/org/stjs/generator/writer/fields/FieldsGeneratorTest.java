package org.stjs.generator.writer.fields;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class FieldsGeneratorTest extends AbstractStjsTest {
	@Test
	public void testPrimitiveIntInstanceField() {
		assertCodeContains(Fields1.class, "prototype.x = 0;");
	}

	@Test
	public void testWrapperIntInstanceField() {
		assertCodeContains(Fields1a.class, "prototype.x = null;");
	}

	@Test
	public void testPrimitiveDoubleField() {
		assertCodeContains(Fields1b.class, "prototype.x = 0.0;");
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
		assertEquals(2.0, executeAndReturnNumber(Fields14.class), 0);
	}

	@Test
	public void testTemplateSetter1() {
		assertCodeContains(Fields15.class, "this.set(\"field\", n)");
	}

	@Test
	public void testTemplateSetter2() {
		assertCodeContains(Fields16.class, "f.set(\"field\", n)");
	}

	@Test
	public void testTemplateSetter3() {
		assertCodeContains(Fields15a.class, "Fields15a.set(\"field\", n)");
	}

	@Test
	public void testTemplateGlobalSetter1() {
		assertCodeContains(Fields17.class, "this[\"field\"] = n;");
	}

	@Test
	public void testTemplateGlobalSetter2() {
		assertCodeContains(Fields18.class, "f[\"field\"] = n;");
	}

	@Test
	public void testTemplatePostIncrement() {
		assertCodeContains(Fields19.class, "this.set(\"field\", this.get(\"field\") + 1, true)");
	}

	@Test
	public void testTemplatePreIncrement() {
		assertCodeContains(Fields20.class, "this.set(\"field\", this.get(\"field\") + 1)");
	}

	@Test
	public void testTemplateGlobalPostIncrement() {
		assertCodeContains(Fields21.class, "this[\"field\"] = this[\"field\"] + 1");
	}

	@Test
	public void testTemplateAddAssign() {
		assertCodeContains(Fields22.class, "this.set(\"field\", this.get(\"field\")  | (2))");
	}

	@Test
	public void testTemplateGlobalDivideAssign() {
		assertCodeContains(Fields23.class, "this[\"field\"] = stjs.trunc(this[\"field\"] / (2))");
	}

	@Test
	public void testTemplateIdentifier() {
		assertCodeContains(Fields24.class, "this.get(\"field\")");
	}

	@Test
	public void testTemplateMemberSelect() {
		assertCodeContains(Fields25.class, "obj.get(\"field\")");
	}
}
