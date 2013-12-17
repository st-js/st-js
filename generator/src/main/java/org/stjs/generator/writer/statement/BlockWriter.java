package org.stjs.generator.writer.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.VariableTree;

/**
 * regular blocks. The static blocks are wrapped in a anonymous function to prevent global scope pollution.
 * @author acraciun
 */
public class BlockWriter<JS> implements WriterContributor<BlockTree, JS> {
	private final MultipleVariableWriter<JS> multipleVariableWriter = new MultipleVariableWriter<JS>();

	@SuppressWarnings("unchecked")
	@Override
	public JS visit(WriterVisitor<JS> visitor, BlockTree tree, GenerationContext<JS> context) {
		// only for regular and static block. instance initializing blocks are not supported
		int i = 0;
		JavaScriptBuilder<JS> js = context.js();

		List<? extends StatementTree> statements = tree.getStatements();
		List<JS> jsStatements = new ArrayList<JS>(statements.size());

		while (i < statements.size()) {
			int sameLineVars = sameLineVars(i, statements, context);
			JS jsNodes = null;
			if (sameLineVars == 1) {
				jsNodes = visitor.scan(statements.get(i++), context);
			} else {
				jsNodes = multipleVariableWriter.visit(visitor, (List<VariableTree>) statements.subList(i, i + sameLineVars), context);
				i += sameLineVars;
			}
			jsStatements.add(jsNodes);
		}
		JS block = js.block(jsStatements);

		if (tree.isStatic()) {
			// generate the enclosing function call (function(){BLOCK()})() to avoid polluting the global scope
			JS function = js.function(null, Collections.<JS> emptyList(), block);
			block = js.functionCall(js.paren(function), null, Collections.<JS> emptyList());
		}
		return context.withPosition(tree, block);
	}

	/**
	 * @return the
	 */
	private int sameLineVars(int start, List<? extends StatementTree> statements, GenerationContext<JS> context) {
		if (!(statements.get(start) instanceof VariableTree)) {
			return 1;
		}
		int line = context.getStartLine(statements.get(start));
		int same = 1;
		for (int i = start + 1; i < statements.size(); ++i) {
			if (statements.get(i) instanceof VariableTree && context.getStartLine(statements.get(i)) == line) {
				++same;
			} else {
				break;
			}
		}
		return same;
	}
}
