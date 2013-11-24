package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ContinueStatement;
import org.mozilla.javascript.ast.ForInLoop;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.UnaryExpression;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;

import com.sun.source.tree.EnhancedForLoopTree;

public class EnhancedForLoopWriter implements VisitorContributor<EnhancedForLoopTree, List<AstNode>, GenerationContext> {

	private void generateArrayHasOwnProperty(EnhancedForLoopTree tree, GenerationContext context, ForInLoop stmt) {
		if (!context.getConfiguration().isGenerateArrayHasOwnProperty()) {
			return;
		}
		// TODO check
		// TypeWrapper iterated = resolvedType(n.getIterable());
		//
		// if (!iterated.isAssignableFrom(TypeWrappers.wrap(Array.class))) {
		// return;
		// }

		UnaryExpression not = new UnaryExpression();
		not.setOperator(Token.NOT);
		not.setOperand(JavaScriptNodes.functionCall(stmt.getIteratedObject(), "hasOwnProperty", stmt.getIterator()));

		IfStatement ifs = new IfStatement();
		ifs.setCondition(not);
		ifs.setThenPart(new ContinueStatement());
		stmt.setBody(JavaScriptNodes.addStatement(stmt.getBody(), ifs));
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, EnhancedForLoopTree tree,
			GenerationContext p, List<AstNode> prev) {
		ForInLoop stmt = new ForInLoop();
		stmt.setIterator(visitor.scan(tree.getVariable(), p).get(0));
		stmt.setIteratedObject(visitor.scan(tree.getExpression(), p).get(0));
		stmt.setBody(visitor.scan(tree.getStatement(), p).get(0));

		generateArrayHasOwnProperty(tree, p, stmt);
		return Collections.<AstNode>singletonList(stmt);
	}
}
