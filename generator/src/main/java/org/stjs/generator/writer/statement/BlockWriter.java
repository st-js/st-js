package org.stjs.generator.writer.statement;

import static org.stjs.generator.writer.JavaScriptNodes.paren;
import static org.stjs.generator.writer.JavaScriptNodes.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.Tree;

public class BlockWriter implements VisitorContributor<BlockTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, BlockTree tree, GenerationContext context,
			List<AstNode> prev) {
		// only for regular and static block. instance initializing blocks are not supported
		Block stmt = new Block();
		for (Tree child : tree.getStatements()) {
			List<AstNode> jsNodes = visitor.scan(child, context);
			for (AstNode jsNode : jsNodes) {
				stmt.addChild(jsNode);
			}
		}
		if (tree.isStatic()) {
			//generate the enclosing function call (function(){BLOCK()})() to avoir polluting the global scope
			FunctionNode func = JavaScriptNodes.function();
			func.setBody(stmt);

			FunctionCall funcCall = new FunctionCall();
			funcCall.setTarget(paren(func));

			return Collections.<AstNode> singletonList(context.withPosition(tree, statement(funcCall)));
		}
		return Collections.<AstNode> singletonList(stmt);
	}
}
