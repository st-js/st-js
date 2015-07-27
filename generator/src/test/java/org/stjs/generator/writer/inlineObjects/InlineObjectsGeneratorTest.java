package org.stjs.generator.writer.inlineObjects;

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
}
