package org.stjs.generator.writer.statement;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.CaseTree;
import com.sun.source.tree.Tree;

/**
 * @author acraciun
 */
public class CaseWriter<JS> implements WriterContributor<CaseTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, CaseTree tree, GenerationContext<JS> context) {
		// TODO: check qualified enums:
		// if (selectorType instanceof ClassWrapper && ((ClassWrapper) selectorType).getClazz().isEnum()) {
		// printer.print(names.getTypeName(selectorType));
		// printer.print(".");

		JS expression = null;
		List<JS> statements = new ArrayList<JS>();

		if (tree.getExpression() != null) {
			expression = visitor.scan(tree.getExpression(), context);
		}
		for (Tree c : tree.getStatements()) {
			statements.add(visitor.scan(c, context));
		}

		return context.withPosition(tree, context.js().caseStatement(expression, statements));
	}
}
