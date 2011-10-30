package org.stjs.generator.scope.declarations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.stjs.generator.scope.ScopeTestHelper.assertResolvedField;
import static org.stjs.generator.scope.ScopeTestHelper.assertResolvedName;
import static org.stjs.generator.scope.ScopeTestHelper.resolveName;
import japa.parser.ParseException;

import java.io.IOException;

import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.scope.BasicScope;
import org.stjs.generator.scope.ClassScope;
import org.stjs.generator.scope.CompilationUnitScope;

public class DeclarationsTest {
	@Test
	public void testScopeParam() throws ParseException, IOException {
		assertResolvedName(Declaration1.class, "param", BasicScope.class, 2);
	}

	@Test
	public void testScopeVariable() throws ParseException, IOException {
		assertResolvedName(Declaration1.class, "var2", BasicScope.class, 3);
	}

	@Test
	public void testScopeType() throws ParseException, IOException {
		assertResolvedName(Declaration1.class, "type", ClassScope.class, 1);
		assertResolvedName(Declaration1.class, "type", 2, ClassScope.class, 4);
		assertResolvedField(Declaration1.class, "type", Runnable.class);
	}

	@Test
	public void testScopeInnerOuter() throws ParseException, IOException {
		try {
			resolveName(DeclarationWithOuter1.class);
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(27, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterStatic() throws ParseException, IOException {
		// static is ok
		resolveName(DeclarationWithOuter1b.class);
	}

	@Test
	public void testScopeInnerOuterParent() throws ParseException, IOException {
		try {
			resolveName(DeclarationWithOuter2.class);
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(24, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterParentQualified() throws ParseException, IOException {
		try {
			resolveName(DeclarationWithOuter2b.class);
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(24, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterQualified() throws ParseException, IOException {
		try {
			resolveName(DeclarationWithOuter3.class);
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(28, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterMethod() throws ParseException, IOException {
		try {
			resolveName(DeclarationWithOuter4.class);
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(29, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterStaticMethod() throws ParseException, IOException {
		resolveName(DeclarationWithOuter4b.class);
		// static is ok
	}

	@Test
	public void testScopeInnerOuterParentMethod() throws ParseException, IOException {
		try {
			resolveName(DeclarationWithOuter5.class);
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(24, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeInnerOuterQualifiedMethod() throws ParseException, IOException {
		try {
			resolveName(DeclarationWithOuter6.class);
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(32, ex.getSourcePosition().getLine());
		}
	}

	@Test
	public void testScopeParent() throws ParseException, IOException {

		assertResolvedName(Declaration1.class, "parentProtected", ClassScope.class, 1);
		assertResolvedName(Declaration1.class, "parentPackage", ClassScope.class, 1);
		assertResolvedName(Declaration1.class, "parentPublic", ClassScope.class, 1);
	}

	@Test
	public void testScopeImport() throws ParseException, IOException {
		assertResolvedName(Declaration1.class, "stat", CompilationUnitScope.class, 0);
	}

	@Test
	public void testScopeFull() throws ParseException, IOException {
		assertResolvedField(Declaration1.class, "full", Bean1.class);
	}

}
