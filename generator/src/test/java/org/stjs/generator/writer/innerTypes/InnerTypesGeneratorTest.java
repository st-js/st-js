package org.stjs.generator.writer.innerTypes;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;


public class InnerTypesGeneratorTest {
	@Test
	public void testCreateInstanceInnerType() {
		// TODO here should be sure the inner type can access the outer type with this
		assertCodeContains(InnerTypes1.class, "new InnerTypes1.InnerType()");
	}

	@Test
	public void testCreateStaticInnerType() {
		assertCodeContains(InnerTypes2.class, "new InnerTypes2.InnerType()");
	}

	@Test
	public void testDeclarationAndAccessInnerTypeInstanceMethod() {
		assertCodeContains(InnerTypes3.class, "InnerTypes3.InnerType.prototype.innerMethod=function()");
		assertCodeContains(InnerTypes3.class, "new InnerTypes3.InnerType().innerMethod()");
		assertCodeContains(InnerTypes3.class, "var x = new InnerTypes3.InnerType()");
	}

	@Test
	public void testDeclarationAndAccessInnerTypeInstanceField() {
		assertCodeContains(InnerTypes4.class, "InnerTypes4.InnerType.prototype.innerField = null");
		assertCodeContains(InnerTypes4.class, "new InnerTypes4.InnerType().innerField");
	}

	@Test
	public void testInheritance() {
		assertCodeContains(InnerTypes5.class, "stjs.extend(InnerTypes5.InnerType, MySuperClass);");
	}

	@Test(expected = JavascriptGenerationException.class)
	public void testCallToQualifiedOuterType() {
		generate(InnerTypes6.class);
	}

	@Test
	public void testExternalAccessToInnerType() {
		assertCodeContains(InnerTypes7.class, "new InnerTypes4.InnerType()");
	}

	@Test
	public void testExternalAndQualifiedAccessToInnerType() {
		assertCodeContains(InnerTypes8.class, "new InnerTypes4.InnerType()");
	}

	@Test
	public void testQualifiedFieldAccess() {
		assertCodeContains(InnerTypes9.class, "n = InnerTypes9.InnerType.innerField");
	}
}
