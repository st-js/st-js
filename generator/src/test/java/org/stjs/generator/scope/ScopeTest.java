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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;

public class ScopeTest {

	private NameResolverVisitor getNameResolver(String clazz) throws ParseException, IOException {
		return getNameResolver(clazz, Collections.<String> emptyList());
	}

	private NameResolverVisitor getNameResolver(String clazz, Collection<String> allowedPackages)
			throws ParseException, IOException {
		CompilationUnit cu = null;
		// parse the file
		cu = JavaParser.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(clazz));
		Collection<String> packages = new HashSet<String>(allowedPackages);
		packages.add("test");
		packages.add("java.lang");
		Set<String> javaLangClasses = new HashSet<String>();
		javaLangClasses.add("String");
		javaLangClasses.add("Number");
		javaLangClasses.add("Runnable");
		javaLangClasses.add("Object");
		ScopeVisitor scopes = new ScopeVisitor(new File(clazz), Thread.currentThread().getContextClassLoader(),
				packages);
		NameScope rootScope = new FullyQualifiedScope(new File(clazz), Thread.currentThread().getContextClassLoader());
		scopes.visit(cu, rootScope);
		// rootScope.dump("");

		NameResolverVisitor resolver = new NameResolverVisitor(rootScope, packages, javaLangClasses);
		resolver.visit(cu, new NameScopeWalker(rootScope));

		// dumpXML(cu);
		System.out.println(resolver.getResolvedIdentifiers());
		return resolver;
	}

	@Test
	public void testScopeParam() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");

		assertScope(resolver).line(17).column(20, 2).assertName("param")
				.assertScopePath("root.import.parent-ParentDeclaration1.type-Declaration1.param-15");
	}

	@Test
	public void testScopeVariable() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScope(resolver).line(17).column(28, 2).assertName("var2")
				.assertScopePath("root.import.parent-ParentDeclaration1.type-Declaration1.param-15.block-15");
	}

	@Test
	public void testScopeType() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScope(resolver).line(17).column(35, 2).assertName("type")
				.assertScopePath("root.import.parent-ParentDeclaration1.type-Declaration1");
	}

	@Test
	public void testScopeInnerOuter1() throws ParseException, IOException {
		try {
			getNameResolver("test/DeclarationWithOuter1.java");
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(15, ex.getSourcePosition().getLine());
			assertEquals(44, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testScopeInnerOuter2() throws ParseException, IOException {
		try {
			getNameResolver("test/DeclarationWithOuter2.java");
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(11, ex.getSourcePosition().getLine());
			assertEquals(60, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testScopeParent() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		// 27:int exp6 = parentPrivate + parentProtected + parentPackage + parentPublic;
		// parentPrivate resolves to the import not the private field
		assertScope(resolver).line(28).column(20, 2).assertName("parentPrivate").assertScopePath("root.import");
		assertScope(resolver).line(28).column(36, 2).assertName("parentProtected")
				.assertScopePath("root.import.parent-ParentDeclaration1");
		assertScope(resolver).line(28).column(54, 2).assertName("parentPackage")
				.assertScopePath("root.import.parent-ParentDeclaration1");
		assertScope(resolver).line(28).column(70, 2).assertName("parentPublic")
				.assertScopePath("root.import.parent-ParentDeclaration1");
	}

	@Test
	public void testScopeImport() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScope(resolver).line(17).column(54, 2).assertName("stat").assertScopePath("root.import");
	}

	@Test
	public void testScopeFull() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScope(resolver).line(18).column(20, 2).assertName("full").assertScopePath("root");
	}

	@Test
	public void testIllegalImport1() throws ParseException, IOException {
		try {
			getNameResolver("test/CheckPackages1.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(4, ex.getSourcePosition().getLine());
			assertEquals(1, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testIllegalImport2() throws ParseException, IOException {
		try {
			getNameResolver("test/CheckPackages2.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(9, ex.getSourcePosition().getLine());
			assertEquals(17, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testIllegalImport3() throws ParseException, IOException {
		try {
			getNameResolver("test/CheckPackages3.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(6, ex.getSourcePosition().getLine());
			assertEquals(28, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testIllegalImport4() throws ParseException, IOException {
		try {
			getNameResolver("test/CheckPackages4.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(6, ex.getSourcePosition().getLine());
			assertEquals(32, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testIllegalImport5() throws ParseException, IOException {
		try {
			getNameResolver("test/CheckPackages5.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(6, ex.getSourcePosition().getLine());
			assertEquals(32, ex.getSourcePosition().getColumn());
		}
	}

	// TODO the same for methods with Declaration2
	@Test
	public void testMethodsSameName() throws ParseException, IOException {
		try {
			getNameResolver("test/SameNameMethods.java", Collections.singleton("java.text"));
			fail("Expected " + JavascriptGenerationException.class);
		} catch (JavascriptGenerationException ex) {
			assertEquals(8, ex.getSourcePosition().getLine());
			assertEquals(9, ex.getSourcePosition().getColumn());
		}
	}
}
