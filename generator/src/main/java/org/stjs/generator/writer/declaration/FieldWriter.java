package org.stjs.generator.writer.declaration;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.statement.VariableWriter;

import com.sun.source.tree.VariableTree;

/**
 * This will add the declaration of a field. This contributor is not added directly, but redirect from
 * {@link VariableWriter}
 * @author acraciun
 */
public class FieldWriter<JS> extends AbstractMemberWriter<JS> implements WriterContributor<VariableTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, VariableTree tree, GenerationContext<JS> context) {
		JS initializer = null;
		if (tree.getInitializer() == null) {
			initializer = context.js().keyword(Keyword.NULL);
		} else {
			initializer = visitor.scan(tree.getInitializer(), context);
		}

		String fieldName = tree.getName().toString();
		JS member = context.js().property(getMemberTarget(tree, context), fieldName);
		return context.js().expressionStatement(context.js().assignment(AssignOperator.ASSIGN, member, initializer));
	}
}
