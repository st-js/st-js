package org.stjs.generator.javascript;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * .
 * 
 * @author acraciun
 */
public interface JavaScriptBuilder<T> {
	public T array(@Nonnull Iterable<T> values);

	public T asExpressionList(@Nonnull Iterable<T> nodes);

	public T assignment(@Nonnull AssignOperator operator, @Nonnull T left, @Nonnull T right);

	public T binary(@Nonnull BinaryOperator operator, @Nonnull Iterable<T> operands);

	public T block(@Nonnull Iterable<T> statements);

	public T breakStatement(@Nullable T label);

	public T caseStatement(@Nullable T expression, @Nonnull Iterable<T> statements);

	public T catchClause(@Nonnull T varName, @Nonnull T body);

	public T continueStatement(@Nullable T label);

	public T elementGet(@Nonnull T target, @Nonnull T index);

	public T expressionStatement(@Nonnull T expr);

	public T function(@Nullable String name, @Nonnull Iterable<T> params, @Nullable T body);

	public T functionCall(@Nonnull T target, @Nonnull Iterable<T> arguments);

	public T keyword(@Nonnull Keyword token);

	public T name(@Nonnull CharSequence name);

	public T label(@Nonnull CharSequence name);

	public T newExpression(@Nonnull T target, @Nonnull Iterable<T> arguments);

	public T object(@Nonnull Iterable<NameValue<T>> props);

	public T paren(@Nonnull T expr);

	public T position(@Nonnull T node, int javaLineNumber, int javaColumnNumber);

	public T property(@Nonnull T target, @Nonnull CharSequence name);

	public T string(@Nonnull String value);

	public T unary(@Nonnull UnaryOperator operator, @Nonnull T operand);

	public T variableDeclaration(boolean statement, @Nonnull Iterable<NameValue<T>> vars);

	public T variableDeclaration(boolean statement, @Nonnull CharSequence name, @Nullable T init);

	public T doLoop(@Nonnull T condition, @Nonnull T body);

	public T emptyStatement();

	public T forInLoop(@Nullable T iterator, @Nullable T iterated, @Nullable T body);

	public T forLoop(@Nullable T init, @Nullable T condition, @Nullable T update, @Nullable T body);

	public T ifStatement(@Nonnull T condition, @Nonnull T thenPart, @Nullable T elsePart);

	public T addStatement(@Nullable T blockOrStatement, @Nullable T statement);

	public T labeledStatement(@Nonnull T label, @Nonnull T statement);

	public T returnStatement(@Nullable T returnValue);

	public T switchStatement(@Nonnull T expr, @Nonnull Iterable<T> cases);

	public T tryStatement(@Nonnull T tryBlock, @Nonnull Iterable<T> catchClauses, @Nullable T finallyBlock);

	public T whileLoop(@Nonnull T condition, @Nullable T body);

	public T root(@Nonnull Iterable<T> children);

	public T code(@Nonnull String code);

	public String toString(@Nullable T node);

	public T conditionalExpression(@Nonnull T test, @Nonnull T trueExpr, @Nonnull T falseExpr);

	public T character(@Nonnull String c);

	public T number(@Nonnull Number n);

	public T statements(@Nonnull Iterable<T> stmts);
}
