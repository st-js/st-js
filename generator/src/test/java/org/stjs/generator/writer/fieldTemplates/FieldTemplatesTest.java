package org.stjs.generator.writer.fieldTemplates;

import org.junit.Test;
import org.stjs.generator.MultipleFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

public class FieldTemplatesTest extends AbstractStjsTest {
	@Test
	public void testTemplateDeepProp() {
		assertCodeContains(FieldTemplates1.class, "this.method(\"inner.field\")");
	}

	@Test
	public void testTemplateDeepPropDirectAccess() {
		assertCodeContains(FieldTemplates1a.class, "this.method(\"inner.field\")");
	}

	@Test
	public void testTemplateDeepPropWithMethods() {
		assertCodeContains(FieldTemplates2.class, "this.me().method(\"inner.field\").substring(0, 2)");
	}

	@Test(expected = MultipleFileGenerationException.class)
	public void testTemplateDeepPropInvalid() {
		generate(FieldTemplates3.class);
	}
}
