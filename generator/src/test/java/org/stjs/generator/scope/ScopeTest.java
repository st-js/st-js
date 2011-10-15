/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator.scope;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.stjs.generator.scope.ScopeAssert.assertScope;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.scope.classloader.ClassLoaderWrapper;
import org.stjs.generator.scope.simple.CompilationUnitScope;
import org.stjs.generator.scope.simple.FieldVariable;
import org.stjs.generator.scope.simple.SimpleScopeBuilder;

public class ScopeTest {

	static CompilationUnitScope resolveName2(CompilationUnit cu, String clazz) throws ParseException, IOException {
		return resolveName2(cu, clazz, Collections.<String> emptyList());
	}

	static CompilationUnitScope resolveName2(CompilationUnit cu, String clazz, Collection<String> packages)
			throws ParseException, IOException {

		ClassLoaderWrapper classLoader = new ClassLoaderWrapper(Thread.currentThread().getContextClassLoader());
		SimpleScopeBuilder builder = new SimpleScopeBuilder(classLoader);
		CompilationUnitScope scope = new CompilationUnitScope(classLoader);

		builder.visit(cu, scope);

		packages = new HashSet<String>(packages);
		packages.add("test");
		packages.add("java.lang");
		Set<String> javaLangClasses = new HashSet<String>();
		javaLangClasses.add("String");
		javaLangClasses.add("Number");
		javaLangClasses.add("Runnable");
		javaLangClasses.add("Object");

		return scope;
	}

	static CompilationUnit compilationUnit(String fileName) throws ParseException {
		return JavaParser.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
	}

	private void resolveName(String fileName) throws ParseException, IOException {
		resolveName(fileName, Collections.<String> emptyList());
	}

	private void resolveName(String fileName, Collection<String> packages) throws ParseException, IOException {
		resolveName2(compilationUnit(fileName), fileName, packages);
	}

	@Test
	public void testScopeParam() throws ParseException, IOException {
		String fileName = "test/Declaration1.java";
		CompilationUnit cu = compilationUnit(fileName);
		CompilationUnitScope scope = resolveName2(cu, fileName);
		assertScope(scope, cu).line(32).column(20, 2).assertName("param")
				.assertScopePath("root.import.parent-ParentDeclaration1.type-Declaration1.param-30");
	}

	public void fieldVsInnerClass() throws ParseException, IOException {
		String fileName = "test/FieldsVsInnerClass.java";
		CompilationUnit cu = compilationUnit(fileName);
		CompilationUnitScope scope = resolveName2(cu, fileName);
		assertScope(scope, cu).line(13).column(9, 2).assertName("MyInnerClass")
				.assertScopePath("root.import.type-FieldsVsInnerClass").assertType(FieldVariable.class);

		assertScope(scope, cu).line(14).column(9, 2).assertName("MyInnerClass2")
				.assertScopePath("root.import.type-FieldsVsInnerClass").assertType(null);
	}

	@Test
	public void testScopeVariable() throws ParseException, IOException {
		String fileName = "test/Declaration1.java";
		CompilationUnit cu = compilationUnit(fileName);
		CompilationUnitScope scope = resolveName2(cu, fileName);
		assertScope(scope, cu).line(32).column(28, 2).assertName("var2")
				.assertScopePath("root.import.parent-ParentDeclaration1.type-Declaration1.param-30.block-30");
	}

	@Test
	public void testScopeType() throws ParseException, IOException {
		String fileName = "test/Declaration1.java";
		CompilationUnit cu = compilationUnit(fileName);
		CompilationUnitScope scope = resolveName2(cu, fileName);
		assertScope(scope, cu).line(32).column(35, 2).assertName("type")
				.assertScopePath("root.import.parent-ParentDeclaration1.type-Declaration1");
	}

	@Test
	public void testScopeInnerOuter1() throws ParseException, IOException {
		try {
			resolveName("test/DeclarationWithOuter1.java");
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(29, ex.getSourcePosition().getLine());
			assertEquals(44, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testScopeInnerOuter2() throws ParseException, IOException {
		try {
			resolveName("test/DeclarationWithOuter2.java");
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(26, ex.getSourcePosition().getLine());
			assertEquals(60, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testScopeParent() throws ParseException, IOException {
		String fileName = "test/Declaration1.java";
		CompilationUnit cu = compilationUnit(fileName);
		CompilationUnitScope scope = resolveName2(cu, fileName);

		// 27:int exp6 = parentPrivate + parentProtected + parentPackage + parentPublic;
		// parentPrivate resolves to the import not the private field
		assertScope(scope, cu).line(43).column(20, 2).assertName("parentPrivate").assertScopePath("root.import");
		assertScope(scope, cu).line(43).column(36, 2).assertName("parentProtected")
				.assertScopePath("root.import.parent-ParentDeclaration1");
		assertScope(scope, cu).line(43).column(54, 2).assertName("parentPackage")
				.assertScopePath("root.import.parent-ParentDeclaration1");
		assertScope(scope, cu).line(43).column(70, 2).assertName("parentPublic")
				.assertScopePath("root.import.parent-ParentDeclaration1");
	}

	@Test
	public void testScopeImport() throws ParseException, IOException {
		String fileName = "test/Declaration1.java";
		CompilationUnit cu = compilationUnit(fileName);
		CompilationUnitScope scope = resolveName2(cu, fileName);
		assertScope(scope, cu).line(32).column(54, 2).assertName("stat").assertScopePath("root.import");
	}

	@Test
	public void testScopeFull() throws ParseException, IOException {
		String fileName = "test/Declaration1.java";
		CompilationUnit cu = compilationUnit(fileName);
		CompilationUnitScope scope = resolveName2(cu, fileName);
		assertScope(scope, cu).line(33).column(20, 2).assertName("full").assertScopePath("root");
		// assertScope(scope, cu).line(5).column(20, 2).assertName("full").assertScopePath("root");
	}

	@Test
	public void testIllegalImport1() throws ParseException, IOException {
		try {
			resolveName("test/CheckPackages1.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(19, ex.getSourcePosition().getLine());
			assertEquals(1, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testIllegalImport2() throws ParseException, IOException {
		try {
			resolveName("test/CheckPackages2.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(24, ex.getSourcePosition().getLine());
			assertEquals(17, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testIllegalImport3() throws ParseException, IOException {
		try {
			resolveName("test/CheckPackages3.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(21, ex.getSourcePosition().getLine());
			assertEquals(28, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testIllegalImport4() throws ParseException, IOException {
		try {
			resolveName("test/CheckPackages4.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(21, ex.getSourcePosition().getLine());
			assertEquals(32, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testIllegalImport5() throws ParseException, IOException {
		try {
			resolveName("test/CheckPackages5.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(26, ex.getSourcePosition().getLine());
			assertEquals(32, ex.getSourcePosition().getColumn());
		}
	}

	// TODO the same for methods with Declaration2
	@Test
	public void testMethodsSameName() throws ParseException, IOException {
		try {
			resolveName("test/SameNameMethods.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(23, ex.getSourcePosition().getLine());
			assertEquals(9, ex.getSourcePosition().getColumn());
		}
	}
}
