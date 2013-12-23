package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.declaration.FieldWriter;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

/**
 * variable declaration. Covers also the fields.
 * 
 * @author acraciun
 */
public class VariableWriter<JS> implements WriterContributor<VariableTree, JS> {
	private final FieldWriter<JS> fieldWriter = new FieldWriter<JS>();

	private boolean isLoopInitializer(GenerationContext<JS> context) {
		Tree parent = context.getCurrentPath().getParentPath().getLeaf();
		return parent instanceof ForLoopTree || parent instanceof EnhancedForLoopTree;
	}

	private JS fieldDeclaration(WriterVisitor<JS> visitor, VariableTree tree, GenerationContext<JS> context) {
		if (context.getCurrentPath().getParentPath().getLeaf() instanceof ClassTree) {
			return fieldWriter.visit(visitor, tree, context);
		}
		return null;
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, VariableTree tree, GenerationContext<JS> context) {
		JS js = null;
		js = fieldDeclaration(visitor, tree, context);
		if (js != null) {
			return js;
		}
		// if it's the init part of a for, mark it as expression, not statement
		boolean isStatement = !isLoopInitializer(context);

		JS init = null;
		if (tree.getInitializer() != null) {
			init = visitor.scan(tree.getInitializer(), context);
		}
		return context.withPosition(tree, context.js().variableDeclaration(isStatement, tree.getName(), init));
	}
}
