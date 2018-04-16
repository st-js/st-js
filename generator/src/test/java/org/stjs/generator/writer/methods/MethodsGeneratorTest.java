package org.stjs.generator.writer.methods;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

public class MethodsGeneratorTest extends AbstractStjsTest {
	@Test
	public void testPublicInstanceMethod() {
		assertCodeContains(Methods1.class, //
				"class Methods1 {" + //
						"method(arg1: string, arg2: string): void {return 0;}");
	}

	@Test
	public void testPrivateInstanceMethod() {
		// same as public
		assertCodeContains(Methods2.class, "class Methods2 { private method(arg1: string, arg2: string): void {");
	}

	@Test
	public void testPublicStaticMethod() {
		assertCodeContains(Methods3.class, //
				"class Methods3 {" + //
						"static method(arg1: string, arg2: string): void{");
	}

	@Test
	public void testPrivateStaticMethod() {
		assertCodeContains(Methods4.class, //
				"class Methods4 {" + //
						"private static method(arg1: string, arg2: string): void {");
	}

	@Test
	public void testMainMethod() {
		// should generate the call to the main method
		assertCodeContains(Methods5.class, "if (!stjs.mainCallDisabled) Methods5.main();");
	}

	@Test
	public void testConstructor() {
		assertCodeContains(Methods6.class, "class Methods6 { constructor(arg: string){} }");
	}

	@Test
	public void testSpecialThis() {
		// the special parameter THIS should not be added
		assertCodeContains(Methods7.class, "method(THIS: string, arg2: string): void{");
	}

	@Test
	public void testAdapter() {
		assertCodeContains(Methods8.class, "(10).toFixed(2)");
	}

	@Test
	public void testAdapterForStatic() {
		assertCodeContains(Methods14.class, "let x = (String).fromCharCode(65,66,67)");
	}

	@Test
	public void testVarArgsMethod3() {
		// only one var arg argument is allowed and the name should be "arguments" -> like the js variable
		assertCodeContains(Methods11.class, "method(..._arguments: Array<any>): void {}");
	}

	@Test
	public void testVarArgsMethod4Native() {
		assertCodeContains(Methods11_b.class, "class Methods11_b { test(props: {[key: string]: any}): void{}");

		assertCodeDoesNotContain(Methods11_b.class, "prototype.method=function");
	}

	@Test
	public void testInterfaceImplResolution() {
		assertCodeContains(Methods12.class, "method(c);");
	}

	@Test
	public void testWildcardResolution() {
		assertCodeContains(Methods13.class, "m.parent().parent()");
	}

	@Test
	public void testAbstractMethod() {
		// the class only contains abstract methods, therefore nothing must be generated
		assertCodeContains(Methods15.class, "abstract class Methods15 {" //
				+ "abstract doSomething(): void;" //
				+ "abstract doSomethingElse(): void;" //
				+ "}");
	}

	@Test
	public void testInterfaceMethods() {
		// the class only contains interface methods, therefore nothing must be generated
		assertCodeContains(Methods15b.class, "interface Methods15b {" //
				+ "doSomething(): void;" //
				+ "doSomethingElse(): void;" //
				+ "}");
	}

	@Test
	public void testMethodsType() {
		String code = generate(Methods18.class);

		// the class only contains interface methods, therefore nothing must be generated
		assertCodeContains(code, "class Methods18<T extends Methods18<any>> {");
		assertCodeContains(code, "parent(): T {");
		assertCodeContains(code, "someArray(arr: Array<number>): number {");
		assertCodeContains(code, "somePrimitiveArray(arr: Array<number>): number {");
		assertCodeContains(code, "someMap(a: {[key: string]: any}): string {");
		assertCodeContains(code, "main(args: Array<string>): number {");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testSynchronizedMethod() {
		// synchronized is forbidden
		generate(Methods16.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testWrongName() {
		// keywords are forbidden
		generate(Methods17.class);
	}
}
