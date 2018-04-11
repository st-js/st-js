package org.stjs.generator.writer.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stjs.generator.MultipleFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class EnumGeneratorTest extends AbstractStjsTest {
	@Test
	public void testSimpleEnumDeclaration() {
		assertCodeContains(Enums1.class, "enum Enums1 {a, b, c}");
	}

	@Test
	public void testEnumReference() {
		assertCodeContains(Enums2.class, "Enums2.Value.a");
		assertCodeContains(Enums2.class, "constructor.Value = Enums2_Value;");
		assertCodeContains(Enums2.class, "enum Enums2_Value {a, b, c}");
	}

	@Test(
			expected = JavascriptFileGenerationException.class)
	public void testEnumWithFieldsDeclaration() {
		// fields are not supported
		generate(Enums9.class);
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void testEnumOrdinal() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("In TypeScript you cannot call methods on enums. Called '.ordinal()'");

		generate(Enums5.class);
	}

	@Test
	public void testEnumName() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("In TypeScript you cannot call methods on enums. Called '.name()'");

		generate(Enums10.class);
	}

	@Test
	public void testEnumValuesMethod() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("In TypeScript you cannot call methods on enums. Called '.values()'");

		generate(Enums11.class);
	}

	@Test
	public void testEnumValueOf() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("In TypeScript you cannot call methods on enums. Called '.valueOf()'");

		generate(Enums12.class);
	}

	@Test
	public void testEnumValues() {
		assertEquals("a:0b:1c:2", execute(Enums6.class));
	}

	@Test
	public void testEnumDeclarationInInterface() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("You cannot define an inner type inside an interface. Please define it outside this interface.");
		generate(Enums8.class);
	}

	@Test
	public void testEnumsNamespace() {
		assertCodeDoesNotContain(EnumsNamespace.class, "stjs.ns(\"my.enums\");");
		assertCodeContains(EnumsNamespace.class, "enum EnumsNamespace {A, B, C}");
	}
}
