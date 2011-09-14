package org.stjs.generator.handlers;

import japa.parser.ast.Node;

import org.stjs.generator.ASTNodeData;
import org.stjs.generator.GenerationContext;

/**
 * This visitor go to every node and sets it's parent
 * 
 * @author acraciun
 * 
 */
public class SetParentVisitor extends ForEachNodeVisitor<GenerationContext> {
	private Node currentParent = null;

	@Override
	protected void before(Node node, GenerationContext arg) {
		node.setData(new ASTNodeData(currentParent));
		currentParent = node;
	}

	@Override
	protected void after(Node node, GenerationContext arg) {
		currentParent = ((ASTNodeData) node.getData()).getParent();
	}
}
