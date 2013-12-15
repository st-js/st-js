package org.stjs.generator.javascript;

import org.mozilla.javascript.ast.AstNode;

/**
 * .
 * 
 * @author acraciun
 * 
 */
public interface JavaScriptBuilder<T> {
	public T name(Object name);

	public T binary(int operator, T... operands);

	public T asExpressionList(T... nodes);

	public T block(T... statements);

	public T property(T target, String name);

	public T elementGet(T target, T index);

	public T unary(int operator, T operand);

	public T functionCall(T target, String name, T... arguments);

	public T newExpression(AstNode target, T... arguments);

	public T keyword(int token);

	public T variableDeclarationStatement(String name, T init);

	public T expressionStatement(T expr);

	public T assignment(int operator, T left, T right);

	public T array(T... values);

	public T string(String value);

	public T paren(T expr);

	public T object(PropertyValue... props);

	public T function();

	public T position(T node, int javaLineNumber, int javaColumnNumber);
}
