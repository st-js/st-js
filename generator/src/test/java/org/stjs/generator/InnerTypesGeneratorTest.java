package org.stjs.generator;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.innerTypes.InnerTypes1;
import test.generator.innerTypes.InnerTypes2;
import test.generator.innerTypes.InnerTypes3;
import test.generator.innerTypes.InnerTypes4;
import test.generator.innerTypes.InnerTypes5;

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

}
