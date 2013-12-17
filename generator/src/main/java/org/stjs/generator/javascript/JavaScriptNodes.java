package org.stjs.generator.javascript;

import java.util.Arrays;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.EmptyExpression;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NewExpression;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;

class JavaScriptNodes {
	private static final int PROP_JAVA_LINE_NO = AstNode.LAST_PROP + 1;
	private static final int PROP_JAVA_COLUMN_NO = AstNode.LAST_PROP + 2;

	public static Name name(Object name) {
		Name n = new Name();
		n.setIdentifier(name.toString());
		return n;
	}

	public static AstNode binary(int operator, List<AstNode> nodes) {
		if (nodes.isEmpty()) {
			return new EmptyExpression();
		}
		if (nodes.size() == 1) {
			return nodes.get(0);
		}
		InfixExpression list = new InfixExpression();
		list.setOperator(operator);
		list.setLeft(nodes.get(0));
		list.setRight(nodes.get(1));
		for (int i = 2; i < nodes.size(); ++i) {
			InfixExpression tmpIncrements = new InfixExpression();
			tmpIncrements.setOperator(operator);
			tmpIncrements.setLeft(list);
			tmpIncrements.setRight(nodes.get(i));
			list = tmpIncrements;
		}
		return list;
	}

	public static AstNode asExpressionList(List<AstNode> nodes) {
		return binary(Token.COMMA, nodes);
	}

	public static AstNode addStatement(AstNode body, AstNode stmt) {
		if (body instanceof Block) {
			((Block) body).addStatement(stmt);
			return body;
		}
		Block block = new Block();
		block.addStatement(body);
		block.addStatement(stmt);
		return block;
	}

	public static AstNode property(AstNode target, String name) {
		if (target == null) {
			return name(name);
		}
		PropertyGet prop = new PropertyGet();
		prop.setTarget(target);
		prop.setProperty(name(name));
		return prop;
	}

	public static ElementGet elementGet(AstNode target, AstNode index) {
		ElementGet prop = new ElementGet();
		prop.setTarget(target);
		prop.setElement(index);
		return prop;
	}

	public static UnaryExpression not(AstNode expr) {
		UnaryExpression op = new UnaryExpression();
		op.setOperator(Token.NOT);
		op.setOperand(expr);
		return op;
	}

	public static FunctionCall functionCall(AstNode target, String name, AstNode... arguments) {
		return functionCall(target, name, Arrays.asList(arguments));
	}

	public static FunctionCall functionCall(AstNode target, String name, List<AstNode> arguments) {
		FunctionCall fc = new FunctionCall();
		if (target == null) {
			fc.setTarget(name(name));
		} else {
			PropertyGet prop = new PropertyGet();
			prop.setTarget(target);
			prop.setProperty(name(name));
			fc.setTarget(prop);
		}
		if (arguments.size() > 0) {
			fc.setArguments(arguments);
		}
		return fc;
	}

	public static NewExpression newExpression(AstNode target, List<AstNode> arguments) {
		NewExpression ne = new NewExpression();
		ne.setTarget(target);
		if (arguments.size() > 0) {
			ne.setArguments(arguments);
		}
		return ne;
	}

	public static AstNode keyword(int token) {
		KeywordLiteral key = new KeywordLiteral();
		key.setType(token);
		return key;
	}

	public static VariableDeclaration variableDeclarationStatement(String name, AstNode init) {
		VariableDeclaration vars = new VariableDeclaration();
		vars.setIsStatement(true);
		VariableInitializer var = new VariableInitializer();
		var.setTarget(name(name));
		var.setInitializer(init);
		vars.addVariable(var);
		return vars;
	}

	public static ExpressionStatement statement(AstNode expr) {
		ExpressionStatement stmt = new ExpressionStatement();
		stmt.setExpression(expr);
		return stmt;
	}

	public static Assignment assignment(AstNode left, AstNode right) {
		Assignment a = new Assignment();
		a.setOperator(Token.ASSIGN);
		a.setLeft(left);
		a.setRight(right);
		return a;
	}

	public static Assignment assignment(AstNode target, String field, AstNode value) {
		Assignment a = new Assignment();
		a.setOperator(Token.ASSIGN);
		if (target == null) {
			a.setLeft(name(field));
		} else {
			PropertyGet prop = new PropertyGet();
			prop.setTarget(target);
			prop.setProperty(name(field));
			a.setLeft(prop);
		}
		a.setRight(value);
		return a;
	}

	public static ArrayLiteral array(AstNode... values) {
		ArrayLiteral array = new ArrayLiteral();
		for (AstNode value : values) {
			array.addElement(value);
		}
		return array;
	}

	public static ArrayLiteral array(List<AstNode> values) {
		ArrayLiteral array = new ArrayLiteral();
		for (AstNode value : values) {
			array.addElement(value);
		}
		return array;
	}

	public static AstNode THIS() {
		return keyword(Token.THIS);
	}

	public static StringLiteral string(String value) {
		StringLiteral expr = new StringLiteral();
		expr.setQuoteCharacter('"');
		expr.setValue(value);
		return expr;
	}

	public static AstNode NULL() {
		return keyword(Token.NULL);
	}

	public static AstNode paren(AstNode expr) {
		ParenthesizedExpression paren = new ParenthesizedExpression();
		paren.setExpression(expr);
		return paren;
	}

	public static ObjectLiteral object(String name, AstNode value) {
		ObjectLiteral object = new ObjectLiteral();
		object.addElement(objectProperty(name(name), value));
		return object;
	}

	public static ObjectLiteral object(String name1, AstNode value1, String name2, AstNode value2) {
		ObjectLiteral object = new ObjectLiteral();
		object.addElement(objectProperty(name(name1), value1));
		object.addElement(objectProperty(name(name2), value2));
		return object;
	}

	public static ObjectLiteral object(List<AstNode> names, List<AstNode> values) {
		ObjectLiteral object = new ObjectLiteral();
		for (int i = 0; i < names.size(); ++i) {
			object.addElement(objectProperty(names.get(i), values.get(i)));
		}
		return object;
	}

	public static ObjectProperty objectProperty(String name, AstNode value) {
		return objectProperty(name(name), value);
	}

	public static ObjectProperty objectProperty(AstNode name, AstNode value) {
		ObjectProperty prop = new ObjectProperty();
		prop.setLeft(name);
		prop.setRight(value);
		return prop;
	}

	public static FunctionNode function() {
		FunctionNode func = new FunctionNode();
		func.setBody(new Block());
		return func;
	}

	public static UnaryExpression unary(int operatorToken, AstNode expr) {
		UnaryExpression ue = new UnaryExpression();
		ue.setOperand(expr);
		ue.setOperator(operatorToken);
		return ue;
	}

	public static AstNode position(AstNode node, int javaLineNumber, int javaColumnNumber) {
		node.putIntProp(PROP_JAVA_LINE_NO, javaLineNumber);
		node.putIntProp(PROP_JAVA_COLUMN_NO, javaColumnNumber);
		return node;
	}
}
