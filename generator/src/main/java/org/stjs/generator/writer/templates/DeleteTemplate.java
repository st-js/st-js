package org.stjs.generator.writer.templates;

import static org.stjs.generator.javascript.JavaScriptNodes.elementGet;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.javascript.JavaScriptNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * map.$delete(key) -> delete map[key]
 * @author acraciun
 */
public class DeleteTemplate implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {
		int argCount = tree.getArguments().size();
		if (argCount != 1) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"A 'delete' template can only be applied for methods with 1 parameter");
		}
		int arg = 0;
		AstNode target = MethodInvocationWriter.buildTarget(visitor, tree, context);
		AstNode prop = elementGet(target, visitor.scan(tree.getArguments().get(arg++), context).get(0));
		return Collections.<AstNode> singletonList(JavaScriptNodes.unary(Token.DELPROP, prop));
	}
}
