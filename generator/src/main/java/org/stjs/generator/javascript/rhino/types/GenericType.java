package org.stjs.generator.javascript.rhino.types;

import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;

public class GenericType extends AstNode {

	private AstNode name;
	private List<AstNode> generics;

	{
		type = TSToken.GENERICTYPE;
	}

	public GenericType(AstNode name, List<AstNode> generics) {
		this.name = name;
		this.generics = generics;
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

	public List<AstNode> getGenerics() {
		return generics;
	}
}

