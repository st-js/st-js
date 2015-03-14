package org.stjs.generator.writer.statement;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.Tree;

/**
 * switch
 * 
 * @author acraciun
 */
public class SwitchWriter<JS> implements WriterContributor<SwitchTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, SwitchTree tree, GenerationContext<JS> context) {
		Tree expr = tree.getExpression();
		if (expr instanceof ParenthesizedTree) {
			// remove the parans
			expr = ((ParenthesizedTree) expr).getExpression();
		}
		JS jsExpr = visitor.scan(expr, context);
		List<JS> cases = new ArrayList<JS>();
		for (Tree c : tree.getCases()) {
			cases.add(visitor.scan(c, context));
		}

		return context.withPosition(tree, context.js().switchStatement(jsExpr, cases));
	}
}
