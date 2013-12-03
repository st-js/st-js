package org.stjs.generator.writer.templates;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * method(x, arg1, arg2) -> x.method(arg1, arg2)
 * 
 * @author acraciun
 */
public class AdapterTemplate implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {
		int argCount = tree.getArguments().size();
		if (argCount < 1) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"An 'adapter' template can only be applied for methods with at least 1 parameter");
		}
		String name = MethodInvocationWriter.buildMethodName(tree);
		List<AstNode> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);

		AstNode target = JavaScriptNodes.paren(arguments.get(0));

		return Collections.<AstNode>singletonList(JavaScriptNodes.functionCall(target, name, arguments.subList(1, arguments.size())));
	}
}
