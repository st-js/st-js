package org.stjs.generator;

import static org.stjs.generator.GeneratorTestHelper.assertCodeContains;

import org.junit.Test;

import test.generator.innerTypes.InnerTypes1;
import test.generator.innerTypes.InnerTypes2;

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

}
