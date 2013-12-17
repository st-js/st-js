package org.stjs.generator.javascript;

import org.mozilla.javascript.ast.AstNode;

public class RhinoJavaScriptBuilder implements JavaScriptBuilder<AstNode> {

	@Override
	public AstNode name(CharSequence name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode binary(int operator, Iterable<AstNode> operands) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode asExpressionList(Iterable<AstNode> nodes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode block(Iterable<AstNode> statements) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode property(AstNode target, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode elementGet(AstNode target, AstNode index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode unary(int operator, AstNode operand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode functionCall(AstNode target, String name, Iterable<AstNode> arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode newExpression(AstNode target, Iterable<AstNode> arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode keyword(int token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode expressionStatement(AstNode expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode assignment(int operator, AstNode left, AstNode right) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode array(Iterable<AstNode> values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode string(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode paren(AstNode expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode position(AstNode node, int javaLineNumber, int javaColumnNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode breakStatement(AstNode label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode caseStatement(AstNode expression, Iterable<AstNode> statements) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode catchClause(AstNode contidion, AstNode body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode continueStatement(AstNode label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode function(String name, Iterable<AstNode> params, AstNode body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode object(Iterable<NameValue<AstNode>> props) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode variableDeclarationStatement(Iterable<NameValue<AstNode>> vars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode doLoop(AstNode condition, AstNode body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode emptyStatement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode forInLoop(AstNode iterator, AstNode iterated, AstNode body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode forLoop(AstNode init, AstNode condition, AstNode update, AstNode body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode ifStatement(AstNode condition, AstNode thenPart, AstNode elsePart) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode addStatement(AstNode blockOrStatement, AstNode statement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode labeledStatement(AstNode label, AstNode statement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode returnStatement(AstNode returnValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode switchStatement(AstNode expr, Iterable<AstNode> cases) {
		// TODO Auto-generated method stub
		return null;
	}

}
