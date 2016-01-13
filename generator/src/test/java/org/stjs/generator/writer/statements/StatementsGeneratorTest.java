package org.stjs.generator.writer.statements;

import org.junit.Ignore;
import org.junit.Test;
import org.stjs.generator.GeneratorConfiguration;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;

import static org.junit.Assert.assertEquals;

public class StatementsGeneratorTest extends AbstractStjsTest {
	@Test
	public void testFor() {
		assertCodeContains(Statements1.class, "for (var i = 0; i < 10; i++) {");
	}

	@Test
	public void testForEach() {
		assertCodeContains(Statements2.class, "" +
				"        for (var index$arg = 0; index$arg < args.length; index$arg++) {\n" +
				"            var arg = args[index$arg];\n" +
				"        }");
	}

	@Test
	public void testWhile() {
		assertCodeContains(Statements3.class, "while(i < 5) {");
	}

	@Test
	public void testSwitch() {
		assertCodeContains(Statements4.class, "switch(i)");
		assertCodeContains(Statements4.class, "case 1: break;");
		assertCodeContains(Statements4.class, "default: break");
	}

	@Ignore
	// comments are currenly disabled
	public void testLineComment() {
		assertCodeContains(Statements7.class, "//line comment");
	}

	@Ignore
	// comments are currenly disabled
	public void testBlockComment() {
		assertCodeContains(Statements8.class, "/* * block comment */");
	}

	@Test
	public void testJavadocCommentMethod() {
		assertCodeContains(Statements8b.class, "/** * javadoc comment */");
	}

	@Test
	public void testJavadocCommentClass() {
		assertCodeContains(Statements8c.class, "/** *javadoc comment */");
	}

	@Test
	public void testJavadocCommentField() {
		assertCodeContains(Statements8d.class, "/** * javadoc comment */");
	}

	@Test
	public void testLiterals() {
		// "abc", "\"", "'", 'a', '\'', 1D, 2f, 1l);
		assertCodeContains(Statements9.class, "\"abc\", \"\\\"\", \"'\", 'a', '\\'', 1.0, 2.0, 1");
	}

	@Test
	public void testInstanceof() {
		assertCodeContains(Statements10.class, "stjs.isInstanceOf(arg.constructor, Statements10)");
	}

	@Test
	public void testForEachArrayOneLine() {
		assertCodeContains(Statements11.class, "if (!(a).hasOwnProperty(i)) continue;parseInt");
	}

	@Test
	public void testForEachInWithIterable() {
		assertCodeContains(Statements22_ForEachIterable.class, "for (var iterator$oneOfTheString = myStringList.iterator(); iterator$oneOfTheString.hasNext(); ) { var oneOfTheString = iterator$oneOfTheString.next(); }");
	}

	@Test
	public void testForEachArrayBlock() {
		assertCodeContains(Statements12.class, "if (!(a).hasOwnProperty(i)) continue;var x");
	}

	@Test
	public void testForEachArrayWithCast() {
		assertCodeContains(Statements12b.class, "if (!(a).hasOwnProperty(i)) continue;");
	}

	@Test
	public void testForEachMapBlock() {
		assertCodeDoesNotContain(Statements13.class, "hasOwnProperty");
	}

	@Test
	public void testStaticInitializer() {
		assertCodeContains(Statements14.class, "{" + //
				"Statements14._instance = new Statements14();" + //
				"var n = Statements14._instance.method();" + //
				"}");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testInstanceInitializer() {
		generate(Statements15.class);
	}

	@Test
	public void testStaticInitializerContainment() {
		// We must do the weird (Number).intValue() because for some reason the execution returns the
		// integer 2 when run from eclipse, but return the double 2.0 when run from maven...
		assertEquals(2, ((Number) execute(Statements16.class)).intValue());
	}

	@Test
	public void testSynchronizedBlock() {
		GeneratorConfiguration configuration = new GeneratorConfigurationBuilder().setSynchronizedAllowed(true).build();
		assertCodeContains(Statements17.class, "for (var i = 0; i < 10; ++i) {}", configuration);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testAssert() {
		// assert not supported
		generate(Statements18.class);
	}

	@Test
	public void testCatch() {
		assertCodeContains(Statements19.class, "catch(e){throw new RuntimeException()._constructor$Throwable(e);}");
	}

	@Test
	public void testForDoubleInit() {
		assertCodeContains(Statements20.class, "for(var i = 0, j = 1; i < 10; ++i){}");
	}

	@Test
	public void testForDoubleInit2() {
		assertCodeContains(Statements20b.class, "for( i = 0, j = 1; i < 10; ++i){}");
	}

	@Test
	public void testForNoInit() {
		assertCodeContains(Statements20c.class, "for(; i < 10; ++i){}");
	}

	@Test
	public void testStaticBlock() {
		assertCodeContains(Statements21.class, "new (stjs.extend(function Statements21$1(){}");
	}
}
