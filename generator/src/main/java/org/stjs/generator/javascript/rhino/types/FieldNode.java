package org.stjs.generator.javascript.rhino.types;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;

public class FieldNode extends AstNode {

	private String name;
	private AstNode value;
	private boolean isStatic;

	{
		type = TSToken.FIELD;
	}

	public FieldNode(String name, AstNode value, boolean isStatic) {
		this.name = name;
		this.value = value;
		this.isStatic = isStatic;
	}

	@Override
	public String toSource(int depth) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void visit(NodeVisitor visitor) {

	}

	public String getName() {
		return name;
	}

	public AstNode getValue() {
		return value;
	}

	public boolean isStatic() {
		return isStatic;
	}
}

