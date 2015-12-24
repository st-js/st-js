package org.stjs.generator.writer.inlineObjects;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class InlineObjectsGeneratorTest extends AbstractStjsTest {
	@Test
	public void testInlineObject() {
		assertCodeContains(InlineObjects1.class, "o = {a:1, b:\"x\"}");
	}

	@Test
	public void testInlineObjectPackageAnnotation() {
		assertCodeContains(InlineObjects1a.class, "o = {}");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testInlineObjectAndOtherStatements() {
		// other statements cannot be put inside the initializing blocks
		generate(InlineObjects2.class);
	}

	@Test
	public void testStatementsInsideInlineFunctions() {
		// make sure the prop:value syntax doesn't go further
		assertCodeContains(InlineObjects3.class, "x=2");
	}

	@Test
	public void testFieldsQualifiedWithThis() {
		// the "this." should be removed (otherwise is rhino who complains)
		assertCodeContains(InlineObjects4.class, "o={a:other.a}");
	}

	@Test
	public void testMockTypeConstructorCall() {
		// should call object constructor {} instead of new Pojo();
		assertCodeContains(InlineObjects5.class, "o={}");
		assertCodeDoesNotContain(InlineObjects5.class, "Pojo");
	}

	@Test
	public void testAssignmentWithToPropertyTemplate(){
		assertCodeContains(InlineObjects6.class, "o={x:\"hello\",x:12}");
	}


	@Test(expected = JavascriptFileGenerationException.class)
	public void testAssignmentWithToPropertyTemplateGetter(){
		generate(InlineObjects7.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testAssignmentWithToPropertyTemplateOnAnotherType(){
		generate(InlineObjects8.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testAssignmentThatLooksLikeToPropertyTemplateButIsnt(){
		generate(InlineObjects9.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testAssignmentThatLooksLikeToPropertyTemplateButIsnt2(){
		generate(InlineObjects10.class);
	}

	@Test
	public void testAnonymousClassCallingOuter() {
		assertCodeContains(InlineObjects11_AnonymousClass_calling_outer.class, "" +
				"    prototype.doIt = function() {\n" +
				"        var this$0 = this;\n" +
				"        return new (stjs.extend(function InlineObjects11_AnonymousClass_calling_outer$1() {}, null, [InlineObjects11_AnonymousClass_calling_outer.Dummy], function(constructor, prototype) {\n" +
				"            prototype.doIt = function() {\n" +
				"                return this$0.outerMethod();\n" +
				"            };\n" +
				"        }, {}, {}))().doIt();\n" +
				"    };\n");
		assertCodeContains(InlineObjects11_AnonymousClass_calling_outer.class, "" +
				"    prototype.doIt2 = function() {\n" +
				"        var this$0 = this;\n" +
				"        return new (stjs.extend(function InlineObjects11_AnonymousClass_calling_outer$2() {}, null, [InlineObjects11_AnonymousClass_calling_outer.SuperDummy], function(constructor, prototype) {\n" +
				"            prototype.doIt = function() {\n" +
				"                var this$1 = this;\n" +
				"                return new (stjs.extend(function InlineObjects11_AnonymousClass_calling_outer$2$1() {}, null, [InlineObjects11_AnonymousClass_calling_outer.Dummy], function(constructor, prototype) {\n" +
				"                    prototype.doIt = function() {\n" +
				"                        return this$0.outerMethod() + this$1.superDoIt();\n" +
				"                    };\n" +
				"                }, {}, {}))().doIt();\n" +
				"            };\n" +
				"            prototype.superDoIt = function() {\n" +
				"                return \"superDoIt() --> \" + this$0.outerMethod();\n" +
				"            };\n" +
				"        }, {}, {}))().doIt();\n" +
				"    };");
	}

	@Test
	public void testCallInnerClassCallingOuter() {
		Object result = execute(InlineObjects11b_InnerClass_calling_outer.class);
		Assert.assertEquals(true, result);
	}

	@Test
	public void testInnerClassInitializingFieldFromParent() {
		assertCodeContains(InlineObjects12a_InnerClass_initializingFieldFromParent.class, "" +
				"            this._abstractClassLevel1FieldCopy = this._outerClass$0._abstractClassLevel1Field;\n" +
				"            this._abstractClassLevel2FieldCopy = this._outerClass$0._abstractClassLevel2Field;\n" +
				"            this._concreteClassFieldCopy = this._outerClass$0._concreteClassField;");

	}

	@Test
	public void testInnerClassHierarchy() {
		assertCodeContains(InlineObjects11c_InnerClass_hierarchy.class,
				"InlineObjects11c_InnerClass_hierarchy.InnerClassA.call(this, this._outerClass$0, \"B-\" + id);");

		Object result = execute(InlineObjects11c_InnerClass_hierarchy.class);
		Assert.assertEquals("Received call from: A-B-InnerClass", result);
	}

}
