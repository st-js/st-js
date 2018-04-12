package org.stjs.generator.writer.innerTypes;

import static junit.framework.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stjs.generator.MultipleFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class InnerTypesGeneratorTest extends AbstractStjsTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void testCreateInstanceInnerType() {
		assertCodeContains(InnerTypes1.class, "new InnerTypes1_InnerType()");
	}

	@Test
	public void testCreateStaticInnerType() {
		assertCodeContains(InnerTypes2.class, "new InnerTypes2_InnerType()");
	}

	@Test
	public void testDeclarationAndAccessInnerTypeInstanceMethod() {
		assertCodeContains(InnerTypes3.class, "class InnerTypes3_InnerType { innerMethod() {} }");
		assertCodeContains(InnerTypes3.class, "new InnerTypes3_InnerType().innerMethod()");
		assertCodeContains(InnerTypes3.class, "let x = new InnerTypes3_InnerType()");
		assertCodeDoesNotContain(InnerTypes3.class, "function(constructor, prototype){InnerTypes3.InnerType=");
	}

	@Test
	public void testDeclarationAndAccessInnerTypeInstanceField() {
		assertCodeContains(InnerTypes4.class, "class InnerTypes4_InnerType { innerField: number = 0; }");
		assertCodeContains(InnerTypes4.class, "new InnerTypes4_InnerType().innerField");
	}

	@Test
	public void testInheritance() {
		assertCodeContains(InnerTypes5.class, "class InnerTypes5_InnerType extends MySuperClass {");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testCallToQualifiedOuterType() {
		generate(InnerTypes6.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testCallFieldToQualifiedOuterType() {
		generate(InnerTypes6a.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testCallMethodOuterType() {
		generate(InnerTypes6b.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testCallMethodToQualifiedOuterType() {
		generate(InnerTypes6c.class);
	}

	@Test
	public void testExternalAccessToInnerType() {
		assertCodeContains(InnerTypes7.class, "new InnerTypes4_InnerType()");
	}

	@Test
	public void testExternalAndQualifiedAccessToInnerType() {
		assertCodeContains(InnerTypes8.class, "new InnerTypes4_InnerType()");
	}

	@Test
	public void testQualifiedFieldAccess() {
		assertCodeContains(InnerTypes9.class, "n = InnerTypes9_InnerType.innerField");
	}

	@Test
	public void testNonStaticInnerType() {
		// for non-static inner classes the constructor contains as first parameter the type of the outer type
		generate(InnerTypes10.class);
	}

	@Test
	public void testNonStaticInnerEnum() {
		// for non-static inner classes the constructor contains as first parameter the type of the outer type
		// also enum has first two params name and ordinal
		generate(InnerTypes11.class);
	}

	@Test
	public void testDeadCode() {
		// the compiler will not generate the code inside the if (dead code), so the inner type may not be found
		generate(InnerTypes12.class);
	}

	@Test
	public void testDeadCode2() {
		// check bug where inner types where not correctly detected
		generate(InnerTypes13.class);
	}

	@Test
	public void testInnerInsideInner() {
		String code = generate(InnerTypes15.class);
		assertCodeContains(code, "class InnerTypes15 {");
		assertCodeContains(code, "let deep = new InnerTypes15_Inner_InnerDeep()");
		assertCodeContains(code, "class InnerTypes15_Inner_InnerDeep {\n" +
				"    b: string = \"b\";\n" +
				"}");
		assertCodeContains(code, "class InnerTypes15_Inner {\n" +
				"    a: string = \"a\";\n" +
				"    static InnerDeep = InnerTypes15_Inner_InnerDeep;\n" +
				"}");
		assertCodeContains(code, "static Inner = InnerTypes15_Inner;");
	}

	@Test
	public void testInnerInsideAnonymous() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("You cannot define an inner type inside an anonymous class. Please define it outside this class.");
		generate(InnerTypes16.class);
	}

	@Test
	public void testEnumInsideInner() {
		String code = generate(InnerTypes17.class);
		assertCodeContains(code, "class InnerTypes17 {");
		assertCodeContains(code, "let deep = InnerTypes17_Inner.Enum.a;");
		assertCodeContains(code, "class InnerTypes17_Inner {");
		assertCodeContains(code, "enum InnerTypes17_Inner_Enum { a, b, c }");
		assertCodeContains(code, "static Enum = InnerTypes17_Inner_Enum;");
	}

	@Test
	public void testAnonymousInsideAnonymous() {
		String code = generate(InnerTypes18.class);
		assertCodeContains(code, "let o = new (class InnerTypes18_InnerTypes18$1 extends Object {");
		assertCodeContains(code, "let o2 = new (class InnerTypes18_InnerTypes18$1_InnerTypes18$1$1 extends Object {");
	}

	@Test
	public void testAnonymousInsideInner() {
		String code = generate(InnerTypes19.class);
		assertCodeContains(code, "class InnerTypes19_Inner {");
		assertCodeContains(code, "return new (class InnerTypes19_Inner_InnerTypes19$Inner$1 extends Object {");
	}

	@Test
	public void testInnerConstantAssignment() {
		assertEquals(2.0, executeAndReturnNumber(InnerTypes20.class));

		assertCodeContains(InnerTypes20.class, "class InnerTypes20_Holder { static VALUE: number = 2; }");
	}

	@Test
	public void testAnonymousTypeMethodExecution() {
		assertEquals(5.0, executeAndReturnNumber(InnerTypes21.class));
	}

	@Test
	public void testCallPrivateMethodFromAnonymous() {
		assertCodeContains(InnerTypes22.class, "return this.privateMethod()");
	}

	@Test
	public void testCallOuterMethodFromAnonymousInit() {
		assertCodeContains(InnerTypes23.class, "x: this.outerMethod()");
	}
}
