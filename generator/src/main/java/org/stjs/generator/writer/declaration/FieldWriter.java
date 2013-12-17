package org.stjs.generator.writer.declaration;

import java.util.Collections;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.statement.VariableWriter;

import com.sun.source.tree.VariableTree;

/**
 * This will add the declaration of a field. This contributor is not added directly, but redirect from
 * {@link VariableWriter}
 * @author acraciun
 */
public class FieldWriter<JS> extends AbstractMemberWriter implements WriterContributor<VariableTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, VariableTree tree, GenerationContext<JS> context) {
		AstNode initializer = null;
		if (tree.getInitializer() == null) {
			initializer = JavaScriptNodes.NULL();
		} else {
			initializer = visitor.scan(tree.getInitializer(), context).get(0);
		}

		String fieldName = tree.getName().toString();
		return Collections.<AstNode> singletonList(statement(assignment(getMemberTarget(tree), fieldName, initializer)));
	}

}
