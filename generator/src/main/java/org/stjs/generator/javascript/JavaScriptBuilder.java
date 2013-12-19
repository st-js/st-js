package org.stjs.generator.javascript;

/**
 * .
 * @author acraciun
 */
public interface JavaScriptBuilder<T> {
	public T array(Iterable<T> values);

	public T asExpressionList(Iterable<T> nodes);

	public T assignment(int operator, T left, T right);

	public T binary(int operator, Iterable<T> operands);

	public T block(Iterable<T> statements);

	public T breakStatement(T label);

	public T caseStatement(T expression, Iterable<T> statements);

	public T catchClause(T contidion, T body);

	public T continueStatement(T label);

	public T elementGet(T target, T index);

	public T expressionStatement(T expr);

	public T function(String name, Iterable<T> params, T body);

	public T functionCall(T target, Iterable<T> arguments);

	public T keyword(int token);

	public T name(CharSequence name);

	public T newExpression(T target, Iterable<T> arguments);

	public T object(Iterable<NameValue<T>> props);

	public T paren(T expr);

	public T position(T node, int javaLineNumber, int javaColumnNumber);

	public T property(T target, CharSequence name);

	public T string(String value);

	public T unary(int operator, T operand);

	public T variableDeclaration(boolean statement, Iterable<NameValue<T>> vars);

	public T variableDeclaration(boolean statement, CharSequence name, T init);

	public T doLoop(T condition, T body);

	public T emptyStatement();

	public T forInLoop(T iterator, T iterated, T body);

	public T forLoop(T init, T condition, T update, T body);

	public T ifStatement(T condition, T thenPart, T elsePart);

	public T addStatement(T blockOrStatement, T statement);

	public T labeledStatement(T label, T statement);

	public T returnStatement(T returnValue);

	public T switchStatement(T expr, Iterable<T> cases);

	public T tryStatement(T tryBlock, Iterable<T> catchClauses, T finallyBlock);

	public T whileLoop(T condition, T body);

	public T root(Iterable<T> children);

	// AstNode node = new Parser().parse(code, "inline", 0);
	public T code(String code);

	public String toString(T node);

	public T conditionalExpression(T test, T trueExpr, T falseExpr);

	public T character(String c);

	public T number(Number n);

	public T statements(Iterable<T> stmts);
}
