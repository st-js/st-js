package org.stjs.generator.javascript.rhino.types;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;

import java.util.List;

public class ClassDeclaration extends AstNode {

	private AstNode name;
	private Iterable<AstNode> members;
	private AstNode extension;
	private List<AstNode> interfaces;
	private boolean isAbstract;

	{
		type = TSToken.CLASS;
	}

	public ClassDeclaration(AstNode name, Iterable<AstNode> members, AstNode extension, List<AstNode> interfaces, boolean isAbstract) {
		this.name = name;
		this.members = members;
		this.extension = extension;
		this.interfaces = interfaces;
		this.isAbstract = isAbstract;
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

	public AstNode getExtends() {
		return extension;
	}

	public List<AstNode> getInterfaces() {
		return interfaces;
	}

	public boolean isAbstract() {
		return isAbstract;
	}
}
