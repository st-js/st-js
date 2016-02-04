package org.stjs.generator.writer.inlineObjects;

import org.junit.Assert;
import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

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
				"        return new (stjs.extend(function InlineObjects11_AnonymousClass_calling_outer$1() {}, stjs.Java.Object, [InlineObjects11_AnonymousClass_calling_outer.Dummy],");

		assertCodeContains(InlineObjects11_AnonymousClass_calling_outer.class, "" +
				"            prototype.doIt = function() {\n" +
				"                return \"doIt()_Dummy.doIt()_outerMethod-\" + this$0.outerMethod();\n" +
				"            };\n");

		assertCodeContains(InlineObjects11_AnonymousClass_calling_outer.class, "" +
				"    prototype.doIt2 = function() {\n" +
				"        var this$0 = this;\n" +
				"        return new (stjs.extend(function InlineObjects11_AnonymousClass_calling_outer$2() {}, stjs.Java.Object, [InlineObjects11_AnonymousClass_calling_outer.SuperDummy], function(constructor, prototype) {\n" +
				"            prototype.doIt = function() {\n" +
				"                var this$1 = this;\n" +
				"                return new (stjs.extend(function InlineObjects11_AnonymousClass_calling_outer$2$1() {}, stjs.Java.Object, [InlineObjects11_AnonymousClass_calling_outer.Dummy], function(constructor, prototype) {\n" +
				"                    prototype.doIt = function() {\n" +
				"                        return \"doIt2()_SuperDummy.doIt()_Dummy.doIt()_outerMethod-\" + this$0.outerMethod() + \"_superDoIt-\" + this$1.superDoIt();\n" +
				"                    };\n" +
				"                }, {}, {}, \"InlineObjects11_AnonymousClass_calling_outer.InlineObjects11_AnonymousClass_calling_outer$2.InlineObjects11_AnonymousClass_calling_outer$2$1\"))().doIt();\n" +
				"            };\n" +
				"            prototype.superDoIt = function() {\n" +
				"                return \"inSuperDoIt_outerMethod-\" + this$0.outerMethod();\n" +
				"            };\n" +
				"        }, {}, {}, \"InlineObjects11_AnonymousClass_calling_outer.InlineObjects11_AnonymousClass_calling_outer$2\"))().doIt();\n" +
				"    };\n");

		Assert.assertEquals("" +
						"#1- doIt()_Dummy.doIt()_outerMethod-InsideOuterMethod!\n" +
						"#2- doIt2()_SuperDummy.doIt()_Dummy.doIt()_outerMethod-InsideOuterMethod!_superDoIt-inSuperDoIt_outerMethod-InsideOuterMethod!",
				execute(InlineObjects11_AnonymousClass_calling_outer.class));
	}

	@Test
	public void testAnonymousClass_using_final_variable_from_calling_method() {
		assertCodeContains(InlineObjects11d_AnonymousClass_using_variables_from_outerClass.class, "" +
				"        prototype.doIt = function() {\n" +
				"            var this$0 = this;\n");

		assertCodeContains(InlineObjects11d_AnonymousClass_using_variables_from_outerClass.class, "" +
				"return variableDefinedInMethod + \"-\" + this$0._fieldDefinedInOuterClass + \"-\" + this$0._fieldDefinedInOuterBaseClass;");


		Assert.assertEquals("variableDefinedInMethod-fieldDefinedInOuterClass-fieldDefinedInOuterBaseClass", execute(InlineObjects11d_AnonymousClass_using_variables_from_outerClass.class));
	}

	@Test
	public void testAnonymousClass_2LevelDepth_using_variables_from_outerClass() {
		Assert.assertEquals("variableDefinedInLevel2AnonymousClass-variableDefinedInLevel1AnonymousClass-variableDefinedInMethod-fieldDefinedInOuterClass-fieldDefinedInOuterBaseClass", execute(InlineObjects11e_AnonymousClass_2LevelDepth_using_variables_from_outerClass.class));
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
				"InlineObjects11c_InnerClass_hierarchy.InnerClassA.prototype._constructor$String.call(this, \"B-\" + id);");

		Object result = execute(InlineObjects11c_InnerClass_hierarchy.class);
		Assert.assertEquals("Received call from: A-B-InnerClass", result);
	}

}
