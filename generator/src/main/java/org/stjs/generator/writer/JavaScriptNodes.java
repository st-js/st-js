package org.stjs.generator.writer;

import java.util.Arrays;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.EmptyExpression;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NewExpression;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;

public class JavaScriptNodes {
	public static Name name(Object name) {
		Name n = new Name();
		n.setIdentifier(name.toString());
		return n;
	}

	public static AstNode asExpressionList(List<AstNode> nodes) {
		if (nodes.isEmpty()) {
			return new EmptyExpression();
		}
		if (nodes.size() == 1) {
			return nodes.get(0);
		}
		InfixExpression list = new InfixExpression();
		list.setOperator(Token.COMMA);
		list.setLeft(nodes.get(0));
		list.setRight(nodes.get(1));
		for (int i = 2; i < nodes.size(); ++i) {
			InfixExpression tmpIncrements = new InfixExpression();
			tmpIncrements.setOperator(Token.COMMA);
			tmpIncrements.setLeft(list);
			tmpIncrements.setRight(nodes.get(i));
			list = tmpIncrements;
		}
		return list;
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

	public static PropertyGet property(AstNode target, String name) {
		PropertyGet prop = new PropertyGet();
		prop.setTarget(target);
		prop.setProperty(name(name));
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

}
