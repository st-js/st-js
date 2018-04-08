package org.stjs.generator.javascript;

import java.io.File;
import java.io.Writer;

import com.google.debugging.sourcemap.SourceMapGenerator;

import javax.annotation.Nonnull;

/**
 * <p>StringJavaScriptBuilder class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@SuppressWarnings("PMD.ExcessivePublicCount")
public class StringJavaScriptBuilder implements JavaScriptBuilder<String> {

	/** {@inheritDoc} */
	@Override
	public String asExpressionList(Iterable<String> nodes) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String block(Iterable<String> statements) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String elementGet(String target, String index) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String expressionStatement(String expr) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String array(Iterable<String> values) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String string(String value) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String paren(String expr) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String position(String node, int javaStartLineNumber, int javaStartColumnNumber, int javaEndLineNumber, int javaEndColumnNumber) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String breakStatement(String label) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String caseStatement(String expression, Iterable<String> statements) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String catchClause(String contidion, String body) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String continueStatement(String label) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String function(String name, Iterable<String> params, String body) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String method(String name, Iterable<String> params, String body) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String field(@Nonnull String name, String value) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String arrowFunction(Iterable<String> params, String body) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String name(CharSequence name) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String vararg(CharSequence name) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String newExpression(String target, Iterable<String> arguments) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String object(Iterable<NameValue<String>> props) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String doLoop(String condition, String body) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String emptyStatement() {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String forInLoop(String iterator, String iterated, String body) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String forLoop(String init, String condition, String update, String body) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String ifStatement(String condition, String thenPart, String elsePart) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String addStatement(String blockOrStatement, String statement) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String addStatementBeginning(String blockOrStatement, String statement) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String labeledStatement(String label, String statement) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String returnStatement(String returnValue) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String switchStatement(String expr, Iterable<String> cases) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String functionCall(String target, Iterable<String> arguments) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String property(String target, CharSequence name) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String variableDeclaration(boolean statement, Iterable<NameValue<String>> vars, boolean isFinal) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String variableDeclaration(boolean statement, CharSequence name, String init, boolean isFinal) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String tryStatement(String tryBlock, Iterable<String> catchClauses, String finallyBlock) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String whileLoop(String condition, String body) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String root(Iterable<String> children) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String code(String code) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String toString(String node) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String conditionalExpression(String test, String trueExpr, String falseExpr) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String character(String c) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String number(Number n) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String statements(Iterable<String> stmts) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String enumDeclaration(@Nonnull String name, @Nonnull Iterable<String> values) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String interfaceDeclaration(@Nonnull String name, Iterable<String> members, Iterable<String> extension) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String assignment(AssignOperator operator, String left, String right) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String binary(BinaryOperator operator, Iterable<String> operands) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String keyword(Keyword token) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String unary(UnaryOperator operator, String operand) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String label(CharSequence name) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public SourceMapGenerator writeJavaScript(String javascriptRoot, File inputFile, boolean generateSourceMap, Writer writer) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String comment(String node, String comment) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String throwStatement(String expr) {
		// Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String emptyExpression() {
		// Auto-generated method stub
		return null;
	}
}
