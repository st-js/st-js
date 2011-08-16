package org.stjs.generator.handlers;

import static org.stjs.generator.GeneratedScriptTester.handlerTester;
import japa.parser.ast.expr.IntegerLiteralExpr;
import org.junit.Test;

public class LitteralExpressionHandlerTest {

	@Test
	public void shouldPrintIntegerLitter() throws Exception {
		handlerTester(LiteralExpressionHandler.class, false).assertGenerateString("7", new IntegerLiteralExpr("7"));
	}
}
