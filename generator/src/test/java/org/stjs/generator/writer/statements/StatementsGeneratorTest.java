package org.stjs.generator.writer.statements;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stjs.generator.MultipleFileGenerationException;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class StatementsGeneratorTest extends AbstractStjsTest {
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();


	@Test
	public void testFor() {
		assertCodeContains(Statements1.class, "for (let i = 0; i < 10; i++) {");
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

	public void testArray1() {
		assertCodeContains(Statements5.class, "new Int32Array([1, 2, 3])");
	}

	public void testArray2() {
		assertCodeContains(Statements6.class, "[1, 2, 3]");
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
		assertCodeContains(Statements9.class, "\"abc\", \"\\\"\", \"'\", 'a'.charCodeAt(0), '\\''.charCodeAt(0), 1.0, 2.0, 1");
	}

	@Test
	public void testInstanceof() {
		assertCodeContains(Statements10.class, "arg instanceof Statements10");
	}

	@Test
	public void testInstanceofOnPrimitive() {
		expectedEx.expect(MultipleFileGenerationException.class);
		expectedEx.expectMessage("Using 'instanceof' on primitives does not work as you would expect, prefer using 'typeof' instead.");
		generate(Statements23.class);
	}

	@Test
	public void testForEachArrayOneLine() {
		assertCodeContains(Statements11.class, "for (let i of a) { parseInt(a[i]); }");
	}

	@Test
	public void testForEachInWithIterable() {
		assertCodeContains(Statements22_ForEachIterable.class, "for (let iterator$oneOfTheString = myStringList.iterator(); iterator$oneOfTheString.hasNext(); ) { let oneOfTheString = iterator$oneOfTheString.next(); }");
	}

	@Test
	public void testForEachArrayBlock() {
		assertCodeContains(Statements12.class, "for (let i of a) { let x");
	}

	@Test
	public void testForEachArrayWithCast() {
		assertCodeContains(Statements12b.class, "for (let i of a) {}");
	}

	@Test
	public void testForEachMapBlock() {
		String code = generate(Statements13.class);
		assertCodeDoesNotContain(code, "hasOwnProperty");
		assertCodeContains(code, "for (let i of a) {");
	}

	@Test
	public void testStaticInitializer() {
		assertCodeContains(Statements14.class, "{" + //
				"Statements14.instance = new Statements14();" + //
				"let n = Statements14.instance.method();" + //
				"}");
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testInstanceInitializer() {
		generate(Statements15.class);
	}

	@Test
	public void testStaticInitializerContainment() {
		assertEquals(2.0, executeAndReturnNumber(Statements16.class), 0);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testSynchronizedBlock() {
		// synchronized not supported
		generate(Statements17.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void testAssert() {
		// assert not supported
		generate(Statements18.class);
	}

	@Test
	public void testCatch() {
		assertCodeContains(Statements19.class, "catch(e){throw new RuntimeException(e);}");
	}

	@Test
	public void testForDoubleInit() {
		assertCodeContains(Statements20.class, "for(let i = 0, j = 1; i < 10; ++i){}");
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
    public void testForNoCondition() {
        assertCodeContains(Statements20d.class, "for(let i=0; ; ++i)");
    }

	@Test
    public void testForNoUpdate() {
        assertCodeContains(Statements20e.class, "for(let i=0;i<10;)");
    }


	@Test
	public void testStaticBlock() {
		assertCodeContains(Statements21.class, "new (class Statements21$1 implements Statements21_MyInterface {");
	}
}
