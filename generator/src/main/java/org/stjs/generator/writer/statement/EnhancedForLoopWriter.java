package org.stjs.generator.writer.statement;

import static org.stjs.generator.writer.JavaScriptNodes.functionCall;
import static org.stjs.generator.writer.JavaScriptNodes.name;
import static org.stjs.generator.writer.JavaScriptNodes.paren;

import java.util.Collections;
import java.util.List;

import javacutils.TreeUtils;
import javacutils.TypesUtils;

import javax.lang.model.element.Element;

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
import org.stjs.javascript.Array;

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

		Element iteratedElement = TreeUtils.elementFromUse(tree.getExpression());
		if (!TypesUtils.isDeclaredOfName(iteratedElement.asType(), Array.class.getName())) {
			return;
		}
		UnaryExpression not = new UnaryExpression();
		not.setOperator(Token.NOT);
		not.setOperand(functionCall(paren(stmt.getIteratedObject()), "hasOwnProperty", name(tree.getVariable().getName().toString())));

		IfStatement ifs = new IfStatement();
		ifs.setCondition(not);
		ifs.setThenPart(new ContinueStatement());
		stmt.setBody(JavaScriptNodes.addStatement(stmt.getBody(), ifs));
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, EnhancedForLoopTree tree,
			GenerationContext context, List<AstNode> prev) {
		ForInLoop stmt = new ForInLoop();
		stmt.setIterator(visitor.scan(tree.getVariable(), context).get(0));
		stmt.setIteratedObject(visitor.scan(tree.getExpression(), context).get(0));
		stmt.setBody(visitor.scan(tree.getStatement(), context).get(0));

		generateArrayHasOwnProperty(tree, context, stmt);
		return Collections.<AstNode> singletonList(context.withPosition(tree, stmt));
	}
}
