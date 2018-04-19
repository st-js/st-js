package org.stjs.generator.javascript.rhino.types;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;

import java.util.List;

public class InterfaceDeclaration extends AstNode {

	private AstNode name;
	private Iterable<AstNode> members;
	private List<AstNode> extension;

	{
		type = TSToken.INTERFACE;
	}

	public InterfaceDeclaration(AstNode name, Iterable<AstNode> members, List<AstNode> extension) {
		this.name = name;
		this.members = members;
		this.extension = extension;
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

	public Iterable<AstNode> getMembers() {
		return members;
	}

	public List<AstNode> getExtends() {
		return extension;
	}
}
