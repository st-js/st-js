package org.stjs.generator.javascript.rhino.types;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;

public class TypeVariableNode extends AstNode {

	private AstNode name;
	private AstNode upperBound;
	private AstNode lowerBound;

	{
		type = TSToken.TYPEVARIABLE;
	}

	public TypeVariableNode(AstNode name, AstNode upperBound, AstNode lowerBound) {
		this.name = name;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
	}

	@Override
	public String toSource(int depth) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void visit(NodeVisitor visitor) {

	}

	public AstNode getName() {
		return name;
	}

	public AstNode getUpperBound() {
		return upperBound;
	}

	public AstNode getLowerBound() {
		return lowerBound;
	}
}
