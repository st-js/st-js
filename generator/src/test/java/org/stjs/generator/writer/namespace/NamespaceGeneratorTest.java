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
		assertCodeContains(Namespace1.class, "class Namespace1 {\n" +
				"    instanceMethod(): void {}\n" +
				"    instanceField: string = null;\n" +
				"    static staticMethod(): void {}\n" +
				"    static staticField: string = null;\n" +
				"}");
	}

	@Test
	public void testExtends() {
		assertCodeContains(Namespace2.class, "class Namespace2_Child extends Namespace2 {}");
		// call super
		assertCodeContains(Namespace2.class, "class Namespace2 { static Child = Namespace2_Child; }");
	}

	@Test
	public void testExtends2() {
		assertCodeContains(Namespace2a.class, "class Namespace2a extends Namespace1 {");
		assertCodeDoesNotContain(Namespace2a.class, "let a.b");
	}

	@Test
	public void testConstructor() {
		assertCodeContains(Namespace3.class, "let n = new Namespace3()");
	}

	@Test
	public void testCallSuper() {
		assertCodeContains(Namespace4.class, "method(): void { super.method(); }");
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
		assertCodeContains(Namespace7.class, "field: Namespace7");
	}

	@Test
	public void testInlineConstruct() {
		assertCodeContains(Namespace8.class, "class Namespace8_Namespace8$1 extends Namespace8 { method(): void {} } ");
	}

	@Test(
			expected = IndexOutOfBoundsException.class)
	public void testReservedWordsInNamespace() {
		generate(Namespace9.class);
	}

	@Test()
	public void testAnnotationAtPackageLevel(){
		assertCodeDoesNotContain(PackageNamespace1.class, "a.b.PackageNamespace1 = function()");
		assertCodeContains(PackageNamespace1.class, "class PackageNamespace1 {");
	}

	@Test()
	public void testAnnotationAtPackageLevelRecursive(){
		assertCodeDoesNotContain(PackageNamespace2.class, "a.b.PackageNamespace2 = function()");
		assertCodeContains(PackageNamespace2.class, "class PackageNamespace2 {");
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
