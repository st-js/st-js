package org.stjs.generator.writer.statement;

import static org.stjs.generator.javascript.JavaScriptNodes.paren;
import static org.stjs.generator.javascript.JavaScriptNodes.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.JavaScriptNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.VariableTree;

public class BlockWriter implements VisitorContributor<BlockTree, List<AstNode>, GenerationContext> {
	private final MultipleVariableWriter multipleVariableWriter = new MultipleVariableWriter();

	@SuppressWarnings("unchecked")
	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, BlockTree tree, GenerationContext context,
			List<AstNode> prev) {
		// only for regular and static block. instance initializing blocks are not supported
		Block stmt = new Block();
		int i = 0;
		List<? extends StatementTree> statements = tree.getStatements();
		while (i < statements.size()) {
			int sameLineVars = sameLineVars(i, statements, context);
			List<AstNode> jsNodes = null;
			if (sameLineVars == 1) {
				jsNodes = visitor.scan(statements.get(i++), context);
			} else {
				jsNodes = multipleVariableWriter.visit(visitor, (List<VariableTree>) statements.subList(i, i + sameLineVars), context);
				i += sameLineVars;
			}
			for (AstNode jsNode : jsNodes) {
				stmt.addChild(jsNode);
			}
		}
		if (tree.isStatic()) {
			// generate the enclosing function call (function(){BLOCK()})() to avoid polluting the global scope
			FunctionNode func = JavaScriptNodes.function();
			func.setBody(stmt);

			FunctionCall funcCall = new FunctionCall();
			funcCall.setTarget(paren(func));

			return Collections.<AstNode>singletonList(context.withPosition(tree, statement(funcCall)));
		}
		return Collections.<AstNode>singletonList(stmt);
	}

	/**
	 * 
	 * @return the
	 */
	private int sameLineVars(int start, List<? extends StatementTree> statements, GenerationContext context) {
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
