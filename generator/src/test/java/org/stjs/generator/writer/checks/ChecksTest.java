package org.stjs.generator.writer.checks;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stjs.generator.MultipleFileGenerationException;
import org.stjs.generator.exec.aserts.Asserts2;
import org.stjs.generator.lib.string.String4;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.writer.fields.Fields1;

/**
 * (c) Swissquote 19.04.18
 *
 * @author sgoetz
 */
public class ChecksTest extends AbstractStjsTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();


	@Test
	public void checkEqualsOverride() {
		assertCodeContains(PointUsage.class, "let result = first.equals(second) ? 2 : 1;");
		assertEquals(2.0, executeAndReturnNumber(PointUsage.class), 0);
	}

	@Test
	public void checkEqualsOverrideForbidden() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("Methods inherited from Object can't be used unless they're implemented. Called 'equals()'");
		assertEquals(true, executeAndReturnBoolean(EqualsUsage.class));
	}
}
