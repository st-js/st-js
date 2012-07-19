package org.stjs.generator.writer.innerTypes;

import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;

import org.junit.Test;
import org.stjs.generator.JavascriptGenerationException;

public class InnerTypesGeneratorTest {
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
		assertCodeContains(InnerTypes3.class, "stjs.extend(InnerTypes3.InnerType, null, [], {innerMethod:function()");
		assertCodeContains(InnerTypes3.class, "new InnerTypes3.InnerType().innerMethod()");
		assertCodeContains(InnerTypes3.class, "var x = new InnerTypes3.InnerType()");
	}

	@Test
	public void testDeclarationAndAccessInnerTypeInstanceField() {
		assertCodeContains(InnerTypes4.class, "stjs.extend(InnerTypes4.InnerType, null, [], {innerField:null");
		assertCodeContains(InnerTypes4.class, "new InnerTypes4.InnerType().innerField");
	}

	@Test
	public void testInheritance() {
		assertCodeContains(InnerTypes5.class, "stjs.extend(InnerTypes5.InnerType, MySuperClass, [], ");
	}

	@Test(expected = JavascriptGenerationException.class)
	public void testCallToQualifiedOuterType() {
		generate(InnerTypes6.class);
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

	@Test(expected = JavascriptGenerationException.class)
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
	public void testInnerInsideInner(){
		String code = generate(InnerTypes15.class);
		assertCodeContains(code, 
				"var InnerTypes15 = function(){};" +
				"stjs.extend(InnerTypes15, null, [], {}, {");
		assertCodeContains(code, 
				"var deep = new InnerTypes15.Inner.InnerDeep()");
		assertCodeContains(code, 
				";InnerTypes15.Inner = function(){}; " + 
				"stjs.extend(InnerTypes15.Inner, null, [], {");
		assertCodeContains(code, 
				"{InnerDeep : stjs.extend(function(){}, null, [], {");
	}
	
	@Test
	public void testInnerInsideAnonymous(){
		generate(InnerTypes16.class);
	}
	
	@Test
	public void testEnumInsideInner(){
		String code = generate(InnerTypes17.class);
		assertCodeContains(code,
				"var InnerTypes17 = function(){};" +
				"stjs.extend(InnerTypes17, null, [], {}, {");
		assertCodeContains(code,
				"var deep = InnerTypes17.Inner.Enum.a;");
		assertCodeContains(code, 
				"stjs.extend(InnerTypes17.Inner, null, [], {}, {");
		assertCodeContains(code, 
				"{Enum:stjs.enumeration(");
	}
	
}
