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

	@Override
	public AstNode functionCall(AstNode target, Iterable<AstNode> arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode property(AstNode target, CharSequence name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode variableDeclaration(boolean statement, Iterable<NameValue<AstNode>> vars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode variableDeclaration(boolean statement, CharSequence name, AstNode init) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode tryStatement(AstNode tryBlock, Iterable<AstNode> catchClauses, AstNode finallyBlock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode whileLoop(AstNode condition, AstNode body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode root(Iterable<AstNode> children) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode code(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(AstNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode conditionalExpression(AstNode test, AstNode trueExpr, AstNode falseExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode character(String c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode number(Number n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AstNode statements(Iterable<AstNode> stmts) {
		// TODO Auto-generated method stub
		return null;
	}

}
