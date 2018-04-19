package org.stjs.generator.writer.generics;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

/**
 * (c) Swissquote 19.04.18
 *
 * @author sgoetz
 */
public class GenericsTest extends AbstractStjsTest {

	@Test
	public void testInterfaceGenerics() {
		assertCodeContains(Indicator.class, "let field=null");
	}

	@Test
	public void testComplexGenerics() {
		assertCodeContains(DefaultIndicator.class, "let field=null");
	}
}
