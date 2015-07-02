package org.stjs.generator.writer.innerTypes;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class InnerTypesGeneratorTest extends AbstractStjsTest {
	@Test
	public void testCreateInstanceInnerType() {
		assertCodeContains(InnerTypes1.class, "new InnerTypes1.InnerType()");
	}

	@Test
	public void testCreateStaticInnerType() {
		assertCodeContains(InnerTypes2.class, "new InnerTypes2.InnerType()");
	}

	@Test
	public void testDeclarationAndAccessInnerTypeInstanceMethod() {
		assertCodeContains(InnerTypes3.class,
				"stjs.extend(constructor.InnerType, null, [], function(constructor, prototype){prototype.innerMethod=function()");
		assertCodeContains(InnerTypes3.class, "new InnerTypes3.InnerType().innerMethod()");
		assertCodeContains(InnerTypes3.class, "var x = new InnerTypes3.InnerType()");
		assertCodeDoesNotContain(InnerTypes3.class, "function(constructor, prototype){InnerTypes3.InnerType=");
	}

	@Test
	public void testDeclarationAndAccessInnerTypeInstanceField() {
		assertCodeContains(InnerTypes4.class,
				"stjs.extend(constructor.InnerType, null, [], function(constructor, prototype){prototype.innerField=0");
		assertCodeContains(InnerTypes4.class, "new InnerTypes4.InnerType().innerField");
		assertCodeDoesNotContain(InnerTypes4.class, "function(constructor,prototype){InnerTypes4.InnerType=");
	}

	@Test
	public void testInheritance() {
		assertCodeContains(InnerTypes5.class, "stjs.extend(constructor.InnerType, MySuperClass, [], ");
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
		assertCodeContains(InnerTypes7.class, "new InnerTypes4.InnerType()");
	}

	@Test
	public void testExternalAndQualifiedAccessToInnerType() {
		assertCodeContains(InnerTypes8.class, "new InnerTypes4.InnerType()");
	}

	@Test
	public void testQualifiedFieldAccess() {
		assertCodeContains(InnerTypes9.class, "n = InnerTypes9.InnerType.innerField");
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
		assertCodeContains(code, "var InnerTypes15 = function(){};" + "InnerTypes15 = stjs.extend(InnerTypes15, null, [], function(constructor, prototype){");
		assertCodeContains(code, "var deep = new InnerTypes15.Inner.InnerDeep()");
		assertCodeContains(code, "constructor.Inner = function (){}; "
				+ "constructor.Inner = stjs.extend(constructor.Inner, null, [], function(constructor, prototype){");
		assertCodeContains(code, "constructor.InnerDeep = function (){};"
				+ "constructor.InnerDeep = stjs.extend(constructor.InnerDeep, null, [], function(constructor, prototype){");
	}

	@Test
	public void testInnerInsideAnonymous() {
		String code = generate(InnerTypes16.class);
		assertCodeContains(code, "var InnerTypes16 = function(){};" + "InnerTypes16 = stjs.extend(InnerTypes16, null, [], function(constructor, prototype){");
		assertCodeContains(code, "var o = new (stjs.extend(function InnerTypes16$1(){}, Object, [], function(constructor, prototype){");
		assertCodeContains(code, "};" + "constructor.InnerDeep = function(){}; constructor.InnerDeep = stjs.extend(constructor.InnerDeep, null, [], function(constructor, prototype){");
	}

	@Test
	public void testEnumInsideInner() {
		String code = generate(InnerTypes17.class);
		assertCodeContains(code, "var InnerTypes17 = function(){};" + "InnerTypes17 = stjs.extend(InnerTypes17, null, [], function(constructor, prototype){");
		assertCodeContains(code, "var deep = InnerTypes17.Inner.Enum.a;");
		assertCodeContains(code, "stjs.extend(constructor.Inner, null, [], function(constructor, prototype){");
		assertCodeContains(code, "constructor.Enum=stjs.enumeration(");
	}

	@Test
	public void testAnonymousInsideAnonymous() {
		String code = generate(InnerTypes18.class);
		assertCodeContains(code, "var o = new (stjs.extend(function InnerTypes18$1(){}, Object, [], function(constructor, prototype){");
		assertCodeContains(code, "var o2 = new (stjs.extend(function InnerTypes18$1$1(){}, Object, [], function(constructor, prototype){");
	}

	@Test
	public void testAnonymousInsideInner() {
		String code = generate(InnerTypes19.class);
		assertCodeContains(code, "constructor.Inner = function (){};"
				+ "constructor.Inner = stjs.extend(constructor.Inner, null, [], function(constructor, prototype){");
		assertCodeContains(code, "return new (stjs.extend(function InnerTypes19$Inner$1(){}, Object, [], function(constructor, prototype){");
	}

	@Test
	public void testInnerConstantAssignment() {
		Object result = execute(InnerTypes20.class);
		assertNotNull(result);
		assertEquals(2, ((Number) result).intValue());

		assertCodeContains(InnerTypes20.class, "constructor.Holder = function(){};"
				+ "constructor.Holder = stjs.extend(constructor.Holder, null, [], function(constructor, prototype){" + "       constructor.VALUE = 2;"
				+ "}, {}, {});");
	}

	@Test
	public void testAnonymousTypeMethodExecution() {
		Object result = execute(InnerTypes21.class);
		assertNotNull(result);
		assertEquals(5, ((Number) result).intValue());
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
