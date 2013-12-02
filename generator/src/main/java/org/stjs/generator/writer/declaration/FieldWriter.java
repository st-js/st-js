package org.stjs.generator.writer.declaration;

import static org.stjs.generator.writer.JavaScriptNodes.assignment;
import static org.stjs.generator.writer.JavaScriptNodes.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;
import org.stjs.generator.writer.statement.VariableWriter;

import com.sun.source.tree.VariableTree;

/**
 * This will add the declaration of a field. This contributor is not added directly, but redirect from
 * {@link VariableWriter}
 * @author acraciun
 */
public class FieldWriter extends AbstractMemberWriter implements VisitorContributor<VariableTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, VariableTree tree,
			GenerationContext context, List<AstNode> prev) {
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
