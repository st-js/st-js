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
	
	@Test
	public void testInnerInsideInner(){
		generate(InnerTypes12.class);
	}
	
	@Test
	public void testInnerInsideAnonymous(){
		generate(InnerTypes13.class);
	}
	
	@Test
	public void testEnumInsideInner(){
		assertCodeContains(InnerTypes14.class, 
				"stjs.extend(InnerTypes14.Inner, null, [], {}, {Enum:stjs.enumeration(");
	}
	
}
