package org.stjs.generator.javascript.rhino.types;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;

public class ParamNode extends AstNode {

	private String name;
	private AstNode paramType;
	private boolean isVarargs;

	{
		type = TSToken.PARAM;
	}

	public ParamNode(String name, AstNode paramType, boolean isVarargs) {
		this.name = name;
		this.paramType = paramType;
		this.isVarargs = isVarargs;
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

	public AstNode getParamType() {
		return paramType;
	}

	public boolean isVarargs() {
		return isVarargs;
	}
}
