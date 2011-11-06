package org.stjs.generator.scope.methodDeclarations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.stjs.generator.scope.ScopeTestHelper.assertResolvedMethod;
import static org.stjs.generator.scope.ScopeTestHelper.resolveName;
import japa.parser.ParseException;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.javascript.JSStringAdapter;

public class MethodDeclarationsTest {

	@Test
	public void resolvesMethods() throws ParseException, IOException {
		assertResolvedMethod(ClassUsingStaticMethod.class, "doSth", 1, ClassDefiningStaticMethod.class);
		assertResolvedMethod(ClassUsingStaticMethod.class, "doSth", 2, ClassDefiningStaticMethod.class);
		assertResolvedMethod(ClassUsingStaticMethod.class, "doSth", 3, ClassDefiningStaticMethod.class);
	}

	// TODO the same for methods with Declaration2
	@Test
	public void testMethodsSameName() throws ParseException, IOException {
		try {
			resolveName(SameNameMethods.class);
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(23, ex.getSourcePosition().getLine());
			assertEquals(9, ex.getSourcePosition().getColumn());
		}
	}

	@Ignore
	public void testInlineTypeOrder() {

		assertResolvedMethod(InlineTypesOrder.class, "method1", 1, InlineTypesOrder.class);
		assertResolvedMethod(InlineTypesOrder.class, "method2", 1, InlineTypesOrder.class);
	}

	@Test
	public void testInlineTypeOrder2() {

		assertResolvedMethod(InlineTypesOrder2.class, "replace", 1, JSStringAdapter.class);
	}
}
