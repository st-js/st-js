package org.stjs.generator.writer.statement;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.NameValue;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.VariableTree;

/**
 * this is a special writer called from a block to handle the case of multiple variables declarated on the same line.
 * This is needed because the Javac AST parser has an separate node per variable
 * 
 * @author acraciun
 */
public class MultipleVariableWriter<JS> {
	public JS visit(WriterVisitor<JS> visitor, List<VariableTree> trees, GenerationContext<JS> context) {
		// if it's the init part of a for, mark it as expression, not statement
		boolean isStatement = true;
		// if (isLoopInitializer(context)) {
		// stmt.setIsStatement(false);
		// } else {
		// stmt.setIsStatement(true);
		// }

		List<NameValue<JS>> vars = new ArrayList<NameValue<JS>>();
		for (VariableTree tree : trees) {
			JS init = null;
			if (tree.getInitializer() != null) {
				init = visitor.scan(tree.getInitializer(), context);
			}
			vars.add(NameValue.of(tree.getName(), init));
		}
		return context.withPosition(trees.get(0), context.js().variableDeclaration(isStatement, vars));
	}
}
