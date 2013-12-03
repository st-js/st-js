package org.stjs.generator.writer.templates;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * x.$invoke(a,b) -> x(a,b)
 * @author acraciun
 */
public class InvokeTemplate implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {
		AstNode target = MethodInvocationWriter.buildTarget(visitor, tree, context);
		List<AstNode> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);

		FunctionCall fc = new FunctionCall();
		fc.setTarget(target);
		if (arguments.size() > 0) {
			fc.setArguments(arguments);
		}

		return Collections.<AstNode> singletonList(fc);
	}
}
