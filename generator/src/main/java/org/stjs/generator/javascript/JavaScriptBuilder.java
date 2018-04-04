package org.stjs.generator.javascript;

import java.io.File;
import java.io.Writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.debugging.sourcemap.SourceMapGenerator;

/**
 * .
 *
 * @author acraciun
 * @version $Id: $Id
 */
@SuppressWarnings("PMD.ExcessivePublicCount")
public interface JavaScriptBuilder<T> {
	/**
	 * <p>array.</p>
	 *
	 * @param values a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T array(@Nonnull Iterable<T> values);

	/**
	 * <p>asExpressionList.</p>
	 *
	 * @param nodes a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T asExpressionList(@Nonnull Iterable<T> nodes);

	/**
	 * <p>assignment.</p>
	 *
	 * @param operator a {@link org.stjs.generator.javascript.AssignOperator} object.
	 * @param left a T object.
	 * @param right a T object.
	 * @return a T object.
	 */
	T assignment(@Nonnull AssignOperator operator, @Nonnull T left, @Nonnull T right);

	/**
	 * <p>binary.</p>
	 *
	 * @param operator a {@link org.stjs.generator.javascript.BinaryOperator} object.
	 * @param operands a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T binary(@Nonnull BinaryOperator operator, @Nonnull Iterable<T> operands);

	/**
	 * <p>block.</p>
	 *
	 * @param statements a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T block(@Nonnull Iterable<T> statements);

	/**
	 * <p>breakStatement.</p>
	 *
	 * @param label a T object.
	 * @return a T object.
	 */
	T breakStatement(@Nullable T label);

	/**
	 * <p>caseStatement.</p>
	 *
	 * @param expression a T object.
	 * @param statements a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T caseStatement(@Nullable T expression, @Nonnull Iterable<T> statements);

	/**
	 * <p>catchClause.</p>
	 *
	 * @param varName a T object.
	 * @param body a T object.
	 * @return a T object.
	 */
	T catchClause(@Nonnull T varName, @Nonnull T body);

	/**
	 * <p>continueStatement.</p>
	 *
	 * @param label a T object.
	 * @return a T object.
	 */
	T continueStatement(@Nullable T label);

	/**
	 * <p>elementGet.</p>
	 *
	 * @param target a T object.
	 * @param index a T object.
	 * @return a T object.
	 */
	T elementGet(@Nonnull T target, @Nonnull T index);

	/**
	 * <p>expressionStatement.</p>
	 *
	 * @param expr a T object.
	 * @return a T object.
	 */
	T expressionStatement(@Nonnull T expr);

	/**
	 * <p>function.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param params a {@link java.lang.Iterable} object.
	 * @param body a T object.
	 * @return a T object.
	 */
	T function(@Nullable String name, @Nonnull Iterable<T> params, @Nullable T body);

	/**
	 * <p>arrow function.</p>
	 *
	 * @param params a {@link java.lang.Iterable} object.
	 * @param body a T object.
	 * @return a T object.
	 */
	T arrowFunction(@Nonnull Iterable<T> params, @Nullable T body);

	/**
	 * <p>functionCall.</p>
	 *
	 * @param target a T object.
	 * @param arguments a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T functionCall(@Nonnull T target, @Nonnull Iterable<T> arguments);

	/**
	 * <p>keyword.</p>
	 *
	 * @param token a {@link org.stjs.generator.javascript.Keyword} object.
	 * @return a T object.
	 */
	T keyword(@Nonnull Keyword token);

	/**
	 * <p>name.</p>
	 *
	 * @param name a {@link java.lang.CharSequence} object.
	 * @return a T object.
	 */
	T name(@Nonnull CharSequence name);

	/**
	 * <p>label.</p>
	 *
	 * @param name a {@link java.lang.CharSequence} object.
	 * @return a T object.
	 */
	T label(@Nonnull CharSequence name);

	/**
	 * <p>newExpression.</p>
	 *
	 * @param target a T object.
	 * @param arguments a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T newExpression(@Nonnull T target, @Nonnull Iterable<T> arguments);

	/**
	 * <p>object.</p>
	 *
	 * @param props a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T object(@Nonnull Iterable<NameValue<T>> props);

	/**
	 * <p>paren.</p>
	 *
	 * @param expr a T object.
	 * @return a T object.
	 */
	T paren(@Nonnull T expr);

	/**
	 * <p>position.</p>
	 *
	 * @param node a T object.
	 * @param javaStartLineNumber a int.
	 * @param javaStartColumnNumber a int.
	 * @param javaEndLineNumber a int.
	 * @param javaEndColumnNumber a int.
	 * @return a T object.
	 */
	T position(@Nonnull T node, int javaStartLineNumber, int javaStartColumnNumber, int javaEndLineNumber, int javaEndColumnNumber);

	/**
	 * <p>property.</p>
	 *
	 * @param target a T object.
	 * @param name a {@link java.lang.CharSequence} object.
	 * @return a T object.
	 */
	T property(@Nullable T target, @Nonnull CharSequence name);

	/**
	 * <p>string.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 * @return a T object.
	 */
	T string(@Nonnull String value);

	/**
	 * <p>unary.</p>
	 *
	 * @param operator a {@link org.stjs.generator.javascript.UnaryOperator} object.
	 * @param operand a T object.
	 * @return a T object.
	 */
	T unary(@Nonnull UnaryOperator operator, @Nonnull T operand);

	/**
	 * <p>variableDeclaration.</p>
	 *
	 * @param statement a boolean.
	 * @param vars a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T variableDeclaration(boolean statement, @Nonnull Iterable<NameValue<T>> vars, boolean isFinal);

	/**
	 * <p>variableDeclaration.</p>
	 *
	 * @param statement a boolean.
	 * @param name a {@link java.lang.CharSequence} object.
	 * @param init a T object.
	 * @return a T object.
	 */
	T variableDeclaration(boolean statement, @Nonnull CharSequence name, @Nullable T init, boolean isFinal);

	/**
	 * <p>doLoop.</p>
	 *
	 * @param condition a T object.
	 * @param body a T object.
	 * @return a T object.
	 */
	T doLoop(@Nonnull T condition, @Nonnull T body);

	/**
	 * <p>emptyStatement.</p>
	 *
	 * @return a T object.
	 */
	T emptyStatement();

	/**
	 * <p>emptyExpression.</p>
	 *
	 * @return a T object.
	 */
	T emptyExpression();

	/**
	 * <p>forInLoop.</p>
	 *
	 * @param iterator a T object.
	 * @param iterated a T object.
	 * @param body a T object.
	 * @return a T object.
	 */
	T forInLoop(@Nullable T iterator, @Nullable T iterated, @Nullable T body);

	/**
	 * <p>forLoop.</p>
	 *
	 * @param init a T object.
	 * @param condition a T object.
	 * @param update a T object.
	 * @param body a T object.
	 * @return a T object.
	 */
	T forLoop(@Nullable T init, @Nullable T condition, @Nullable T update, @Nullable T body);

	/**
	 * <p>ifStatement.</p>
	 *
	 * @param condition a T object.
	 * @param thenPart a T object.
	 * @param elsePart a T object.
	 * @return a T object.
	 */
	T ifStatement(@Nonnull T condition, @Nonnull T thenPart, @Nullable T elsePart);

	/**
	 * <p>addStatement.</p>
	 *
	 * @param blockOrStatement a T object.
	 * @param statement a T object.
	 * @return a T object.
	 */
	T addStatement(@Nullable T blockOrStatement, @Nullable T statement);

	/**
	 * <p>addStatementBeginning.</p>
	 *
	 * @param blockOrStatement a T object.
	 * @param statement a T object.
	 * @return a T object.
	 */
	T addStatementBeginning(@Nullable T blockOrStatement, @Nullable T statement);

	/**
	 * <p>labeledStatement.</p>
	 *
	 * @param label a T object.
	 * @param statement a T object.
	 * @return a T object.
	 */
	T labeledStatement(@Nonnull T label, @Nonnull T statement);

	/**
	 * <p>returnStatement.</p>
	 *
	 * @param returnValue a T object.
	 * @return a T object.
	 */
	T returnStatement(@Nullable T returnValue);

	/**
	 * <p>switchStatement.</p>
	 *
	 * @param expr a T object.
	 * @param cases a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T switchStatement(@Nonnull T expr, @Nonnull Iterable<T> cases);

	/**
	 * <p>tryStatement.</p>
	 *
	 * @param tryBlock a T object.
	 * @param catchClauses a {@link java.lang.Iterable} object.
	 * @param finallyBlock a T object.
	 * @return a T object.
	 */
	T tryStatement(@Nonnull T tryBlock, @Nonnull Iterable<T> catchClauses, @Nullable T finallyBlock);

	/**
	 * <p>whileLoop.</p>
	 *
	 * @param condition a T object.
	 * @param body a T object.
	 * @return a T object.
	 */
	T whileLoop(@Nonnull T condition, @Nullable T body);

	/**
	 * <p>root.</p>
	 *
	 * @param children a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T root(@Nonnull Iterable<T> children);

	/**
	 * <p>code.</p>
	 *
	 * @param code a {@link java.lang.String} object.
	 * @return a T object.
	 */
	T code(@Nonnull String code);

	/**
	 * <p>toString.</p>
	 *
	 * @param node a T object.
	 * @return a {@link java.lang.String} object.
	 */
	String toString(@Nullable T node);

	/**
	 * <p>conditionalExpression.</p>
	 *
	 * @param test a T object.
	 * @param trueExpr a T object.
	 * @param falseExpr a T object.
	 * @return a T object.
	 */
	T conditionalExpression(@Nonnull T test, @Nonnull T trueExpr, @Nonnull T falseExpr);

	/**
	 * <p>character.</p>
	 *
	 * @param c a {@link java.lang.String} object.
	 * @return a T object.
	 */
	T character(@Nonnull String c);

	/**
	 * <p>number.</p>
	 *
	 * @param n a {@link java.lang.Number} object.
	 * @return a T object.
	 */
	T number(@Nonnull Number n);

	/**
	 * <p>statements.</p>
	 *
	 * @param stmts a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T statements(@Nonnull Iterable<T> stmts);

	/**
	 * Enum
	 * @param name The enum's name
	 * @param values a {@link java.lang.Iterable} object.
	 * @return a T object.
	 */
	T enumDeclaration(@Nonnull String name, @Nonnull Iterable<String> values);

	/**
	 * <p>writeJavaScript.</p>
	 *
	 * @param javascriptRoot a T object.
	 * @param inputFile a {@link java.io.File} object.
	 * @param generateSourceMap a boolean.
	 * @param writer a {@link java.io.Writer} object.
	 * @return a {@link com.google.debugging.sourcemap.SourceMapGenerator} object.
	 */
	SourceMapGenerator writeJavaScript(T javascriptRoot, File inputFile, boolean generateSourceMap, Writer writer);

	/**
	 * <p>comment.</p>
	 *
	 * @param node a T object.
	 * @param comment a {@link java.lang.String} object.
	 * @return a T object.
	 */
	T comment(@Nullable T node, @Nullable String comment);

	/**
	 * <p>throwStatement.</p>
	 *
	 * @param expr a T object.
	 * @return a T object.
	 */
	T throwStatement(@Nonnull T expr);

}
