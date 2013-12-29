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
 */
@SuppressWarnings("PMD.ExcessivePublicCount")
public interface JavaScriptBuilder<T> {
	T array(@Nonnull Iterable<T> values);

	T asExpressionList(@Nonnull Iterable<T> nodes);

	T assignment(@Nonnull AssignOperator operator, @Nonnull T left, @Nonnull T right);

	T binary(@Nonnull BinaryOperator operator, @Nonnull Iterable<T> operands);

	T block(@Nonnull Iterable<T> statements);

	T breakStatement(@Nullable T label);

	T caseStatement(@Nullable T expression, @Nonnull Iterable<T> statements);

	T catchClause(@Nonnull T varName, @Nonnull T body);

	T continueStatement(@Nullable T label);

	T elementGet(@Nonnull T target, @Nonnull T index);

	T expressionStatement(@Nonnull T expr);

	T function(@Nullable String name, @Nonnull Iterable<T> params, @Nullable T body);

	T functionCall(@Nonnull T target, @Nonnull Iterable<T> arguments);

	T keyword(@Nonnull Keyword token);

	T name(@Nonnull CharSequence name);

	T label(@Nonnull CharSequence name);

	T newExpression(@Nonnull T target, @Nonnull Iterable<T> arguments);

	T object(@Nonnull Iterable<NameValue<T>> props);

	T paren(@Nonnull T expr);

	T position(@Nonnull T node, int javaLineNumber, int javaColumnNumber);

	T property(@Nullable T target, @Nonnull CharSequence name);

	T string(@Nonnull String value);

	T unary(@Nonnull UnaryOperator operator, @Nonnull T operand);

	T variableDeclaration(boolean statement, @Nonnull Iterable<NameValue<T>> vars);

	T variableDeclaration(boolean statement, @Nonnull CharSequence name, @Nullable T init);

	T doLoop(@Nonnull T condition, @Nonnull T body);

	T emptyStatement();

	T forInLoop(@Nullable T iterator, @Nullable T iterated, @Nullable T body);

	T forLoop(@Nullable T init, @Nullable T condition, @Nullable T update, @Nullable T body);

	T ifStatement(@Nonnull T condition, @Nonnull T thenPart, @Nullable T elsePart);

	T addStatement(@Nullable T blockOrStatement, @Nullable T statement);

	T labeledStatement(@Nonnull T label, @Nonnull T statement);

	T returnStatement(@Nullable T returnValue);

	T switchStatement(@Nonnull T expr, @Nonnull Iterable<T> cases);

	T tryStatement(@Nonnull T tryBlock, @Nonnull Iterable<T> catchClauses, @Nullable T finallyBlock);

	T whileLoop(@Nonnull T condition, @Nullable T body);

	T root(@Nonnull Iterable<T> children);

	T code(@Nonnull String code);

	String toString(@Nullable T node);

	T conditionalExpression(@Nonnull T test, @Nonnull T trueExpr, @Nonnull T falseExpr);

	T character(@Nonnull String c);

	T number(@Nonnull Number n);

	T statements(@Nonnull Iterable<T> stmts);

	SourceMapGenerator writeJavaScript(T javascriptRoot, File inputFile, boolean generateSourceMap, Writer writer);

	T comment(@Nullable T node, @Nullable String comment);

	T throwStatement(@Nonnull T expr);
}
