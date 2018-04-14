package org.stjs.generator.writer.globalScope;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class GlobalScopeGeneratorTest extends AbstractStjsTest {
	@Test
	public void testGlobalScopeGeneration() {
		assertCodeDoesNotContain(Globals.class, "stjs.extends");
		assertCodeDoesNotContain(Globals.class, "Globals");
		assertCodeContains(Globals.class, "const hey = \"you\";");
		assertCodeContains(Globals.class, "let field=null");
		assertCodeContains(Globals.class, "const method=function(");
		assertCodeContains(Globals.class, "let one=0;let two=0;");
		assertCodeContains(Globals.class, "main()");
		assertCodeContains(Globals.class, "(function(){let n = method();})()");
	}

	@Test
	public void testQualifiedCall() {
		assertCodeContains(GlobalScope1.class, "test(): void {method();}");
	}

	@Test
	public void testCallWithStaticImport() {
		assertCodeContains(GlobalScope2.class, "test(): void {method();}");
	}

	@Test
	public void testQualifiedField() {
		assertCodeContains(GlobalScope3.class, "s = field");
	}

	@Test
	public void testFieldWithStaticImport() {
		assertCodeContains(GlobalScope4.class, "s = field");
	}

	@Test
	public void testForAdapters() {
		assertCodeContains(GlobalScope5.class, "replace");
	}

	@Test
	public void testMethodWithStarImport() {
		assertCodeContains(GlobalScope7.class, "n = method()");
	}

	@Test
	public void testGlobalMainMethod() {
		assertEquals(2, executeAndReturnNumber(GlobalScope8.class), 0);
	}

	@Test
	public void testFullyQualifiedField() {
		assertCodeContains(GlobalScope9.class, "s = field");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testInstanceMembersNotAllowded1() {
		generate(GlobalScope10.class);
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testInstanceMembersNotAllowded2() {
		generate(GlobalScope11.class);
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testInner() {
		generate(GlobalScope12.class);
	}
}
