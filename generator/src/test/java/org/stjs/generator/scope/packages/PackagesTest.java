package org.stjs.generator.scope.packages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import japa.parser.ParseException;

import java.io.IOException;
import java.util.Collections;

import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.scope.ScopeTestHelper;

public class PackagesTest {
	@Test
	public void testIllegalImport1() throws ParseException, IOException {
		try {
			ScopeTestHelper.resolveName(CheckPackages1.class, Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(19, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testIllegalImport2() throws ParseException, IOException {
		try {
			ScopeTestHelper.resolveName(CheckPackages2.class, Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(24, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testIllegalImport3() throws ParseException, IOException {
		try {
			ScopeTestHelper.resolveName(CheckPackages3.class, Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(21, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testIllegalImport4() throws ParseException, IOException {
		try {
			ScopeTestHelper.resolveName(CheckPackages4.class, Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(21, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testIllegalImport5() throws ParseException, IOException {
		try {
			ScopeTestHelper.resolveName(CheckPackages5.class, Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(26, ex.getSourcePosition().getLine());
		}
	}
}
