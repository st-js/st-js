package org.stjs.generator.writer.namespace;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.writer.namespace.packageLevel.PackageNamespace1;
import org.stjs.generator.writer.namespace.packageLevel.empty.deep.PackageNamespace2;

public class NamespaceGeneratorTest extends AbstractStjsTest {
	@Test
	public void testDecl() {
		assertCodeDoesNotContain(Namespace1.class, "stjs.ns(\"a.b\");");
		assertCodeDoesNotContain(Namespace1.class, "let a.b");
		assertCodeContains(Namespace1.class, "Namespace1=function()");
		assertCodeContains(Namespace1.class, "prototype.instanceMethod=function()");
		assertCodeContains(Namespace1.class, "prototype.instanceField=null");
		assertCodeContains(Namespace1.class, "constructor.staticMethod=function()");
		assertCodeContains(Namespace1.class, "constructor.staticField=null");
	}

	@Test
	public void testExtends() {
		assertCodeContains(Namespace2.class, "constructor.Child = stjs.extend(constructor.Child, Namespace2, [],");
		// call super
		assertCodeContains(Namespace2.class, "Namespace2.call(this)");
	}

	@Test
	public void testExtends2() {
		assertCodeContains(Namespace2a.class, "stjs.extend(Namespace2a, Namespace1, [],");
		assertCodeDoesNotContain(Namespace2a.class, "let a.b");
	}

	@Test
	public void testConstructor() {
		assertCodeContains(Namespace3.class, "let n = new Namespace3()");
	}

	@Test
	public void testCallSuper() {
		assertCodeContains(Namespace4.class, "{ Namespace4.prototype.method.call(this); }");
	}

	@Test
	public void testCallStatic() {
		assertCodeDoesNotContain(Namespace5.class, "a.b.Namespace5.staticMethod()");
		assertCodeContains(Namespace5.class, "Namespace5.staticMethod()");
	}

	@Test(
			expected = IndexOutOfBoundsException.class)
	public void testWrongNs() {
		generate(Namespace6.class);
	}

	@Test
	public void testTypeDesc() {
		assertCodeContains(Namespace7.class, "field:\"Namespace7\"");
	}

	@Test
	public void testInlineConstruct() {
		assertCodeContains(Namespace8.class, "stjs.extend(function Namespace8$1(){Namespace8.call(this);}, Namespace8, [], ");
	}

	@Test(
			expected = IndexOutOfBoundsException.class)
	public void testReservedWordsInNamespace() {
		generate(Namespace9.class);
	}

	@Test()
	public void testAnnotationAtPackageLevel(){
		assertCodeDoesNotContain(PackageNamespace1.class, "a.b.PackageNamespace1 = function()");
		assertCodeContains(PackageNamespace1.class, "PackageNamespace1 = function()");
	}

	@Test()
	public void testAnnotationAtPackageLevelRecursive(){
		assertCodeDoesNotContain(PackageNamespace2.class, "a.b.PackageNamespace2 = function()");
		assertCodeContains(PackageNamespace2.class, "PackageNamespace2 = function()");
	}

	@Test()
	public void testNamespaceWithFullyQualifiedStaticMethodName(){
		assertCodeDoesNotContain(Namespace10.class, "a.b.Namespace1.staticMethod()");
		assertCodeContains(Namespace10.class, "{ Namespace1.staticMethod(); }");
	}

	@Test()
	public void testNamespaceWithStaticMethodInAnotherClass(){
		assertCodeContains(Namespace11.class, " { Namespace1.staticMethod(); }");
	}

	@Test()
	public void testNamespaceWithStaticMethodInAnotherClassAndStaticImport(){
		assertCodeContains(Namespace12.class, "Namespace1.staticMethod()");
	}
}
