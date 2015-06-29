package org.stjs.generator.writer.namespace;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.writer.namespace.packageLevel.PackageNamespace1;
import org.stjs.generator.writer.namespace.packageLevel.empty.deep.PackageNamespace2;

public class NamespaceGeneratorTest extends AbstractStjsTest {
	@Test
	public void testDecl() {
		assertCodeContains(Namespace1.class, "stjs.ns(\"a.b\");");
		assertCodeDoesNotContain(Namespace1.class, "var a.b");
		assertCodeContains(Namespace1.class, "a.b.Namespace1=function()");
		assertCodeContains(Namespace1.class, "prototype.instanceMethod=function()");
		assertCodeContains(Namespace1.class, "prototype.instanceField=null");
		assertCodeContains(Namespace1.class, "constructor.staticMethod=function()");
		assertCodeContains(Namespace1.class, "constructor.staticField=null");
	}

	@Test
	public void testExtends() {
		assertCodeContains(Namespace2.class, "constructor.Child = stjs.extend(constructor.Child, a.b.Namespace2, [],");
		// call super
		assertCodeContains(Namespace2.class, "a.b.Namespace2.call(this)");
	}

	@Test
	public void testExtends2() {
		assertCodeContains(Namespace2a.class, "stjs.extend(a.b.Namespace2a, a.b.Namespace1, [],");
		assertCodeDoesNotContain(Namespace2a.class, "var a.b");
	}

	@Test
	public void testConstructor() {
		assertCodeContains(Namespace3.class, "var n = new a.b.Namespace3()");
	}

	@Test
	public void testCallSuper() {
		assertCodeContains(Namespace4.class, "a.b.Namespace4.prototype.method.call(this)");
	}

	@Test
	public void testCallStatic() {
		assertCodeContains(Namespace5.class, "a.b.Namespace5.staticMethod()");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testWrongNs() {
		generate(Namespace6.class);
	}

	@Test
	public void testTypeDesc() {
		assertCodeContains(Namespace7.class, "field:\"a.b.Namespace7\"");
	}

	@Test
	public void testInlineConstruct() {
		assertCodeContains(Namespace8.class, "stjs.extend(function Namespace8$1(){a.b.Namespace8.call(this);}, a.b.Namespace8, [], ");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testReservedWordsInNamespace() {
		generate(Namespace9.class);
	}

	@Test()
	public void testAnnotationAtPackageLevel(){
		assertCodeContains(PackageNamespace1.class, "a.b.PackageNamespace1 = function()");
	}

	@Test()
	public void testAnnotationAtPackageLevelRecursive(){
		assertCodeContains(PackageNamespace2.class, "a.b.PackageNamespace2 = function()");
	}

	@Test()
	public void testNamespaceWithFullyQualifiedStaticMethodName(){
		assertCodeContains(Namespace10.class, "a.b.Namespace1.staticMethod()");
	}

	@Test()
	public void testNamespaceWithStaticMethodInAnotherClass(){
		assertCodeContains(Namespace11.class, "a.b.Namespace1.staticMethod()");
	}

	@Test()
	public void testNamespaceWithStaticMethodInAnotherClassAndStaticImport(){
		assertCodeContains(Namespace12.class, "a.b.Namespace1.staticMethod()");
	}
}
