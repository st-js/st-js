package org.stjs.generator.scope.declarations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.stjs.generator.scope.ScopeTestHelper.assertResolvedField;
import static org.stjs.generator.scope.ScopeTestHelper.assertResolvedName;
import static org.stjs.generator.scope.ScopeTestHelper.resolveName;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.scope.BasicScope;
import org.stjs.generator.scope.ClassScope;
import org.stjs.generator.scope.CompilationUnitScope;

public class DeclarationsTest {
	@Test
	public void testScopeParam() {
		assertResolvedName(Declaration1.class, "param", BasicScope.class, 2);
	}

	@Test
	public void testScopeVariable() {
		assertResolvedName(Declaration1.class, "var2", BasicScope.class, 3);
	}

	@Test
	public void testScopeType() {
		assertResolvedName(Declaration1.class, "type", ClassScope.class, 1);
		assertResolvedName(Declaration1.class, "type", 2, ClassScope.class, 4);
		assertResolvedField(Declaration1.class, "type", MyCallback.class);
	}

	@Test
	public void testScopeInnerOuter() {
		try {
			resolveName(DeclarationWithOuter1.class);
			fail("Expected " + JavascriptFileGenerationException.class);
		}
		catch (JavascriptFileGenerationException ex) {
			assertEquals(29, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterStatic() {
		// static is ok
		resolveName(DeclarationWithOuter1b.class);
	}

	@Test
	public void testScopeInnerOuterParent() {
		try {
			resolveName(DeclarationWithOuter2.class);
			fail("Expected " + JavascriptFileGenerationException.class);
		}
		catch (JavascriptFileGenerationException ex) {
			assertEquals(26, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterParentQualified() {
		try {
			resolveName(DeclarationWithOuter2b.class);
			fail("Expected " + JavascriptFileGenerationException.class);
		}
		catch (JavascriptFileGenerationException ex) {
			assertEquals(26, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterQualified() {
		try {
			resolveName(DeclarationWithOuter3.class);
			fail("Expected " + JavascriptFileGenerationException.class);
		}
		catch (JavascriptFileGenerationException ex) {
			assertEquals(28, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterMethod() {
		try {
			resolveName(DeclarationWithOuter4.class);
			fail("Expected " + JavascriptFileGenerationException.class);
		}
		catch (JavascriptFileGenerationException ex) {
			assertEquals(31, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterStaticMethod() {
		resolveName(DeclarationWithOuter4b.class);
		// static is ok
	}

	@Test
	public void testScopeInnerOuterParentMethod() {
		try {
			resolveName(DeclarationWithOuter5.class);
			fail("Expected " + JavascriptFileGenerationException.class);
		}
		catch (JavascriptFileGenerationException ex) {
			assertEquals(26, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterQualifiedMethod() {
		try {
			resolveName(DeclarationWithOuter6.class);
			fail("Expected " + JavascriptFileGenerationException.class);
		}
		catch (JavascriptFileGenerationException ex) {
			assertEquals(32, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeParent() {

		assertResolvedName(Declaration1.class, "parentProtected", ClassScope.class, 1);
		assertResolvedName(Declaration1.class, "parentPackage", ClassScope.class, 1);
		assertResolvedName(Declaration1.class, "parentPublic", ClassScope.class, 1);
	}

	@Test
	public void testScopeImport() {
		assertResolvedName(Declaration1.class, "stat", CompilationUnitScope.class, 0);
	}

	@Test
	public void testScopeFull() {
		assertResolvedField(Declaration1.class, "full", Bean1.class);
	}

}
