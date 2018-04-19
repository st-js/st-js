package org.stjs.generator.writer.checks;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;

/**
 * (c) Swissquote 19.04.18
 *
 * @author sgoetz
 */
public class ChecksTest extends AbstractStjsTest {

	@Test
	public void checkEqualsOverride() {
		 assertEquals(true, executeAndReturnBoolean(PointUsage.class));
	}
}
