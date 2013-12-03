package org.stjs.generator.writer.templates;

import static org.stjs.generator.writer.JavaScriptNodes.paren;
import static org.stjs.generator.writer.JavaScriptNodes.unary;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $typeOf(arg) -> (typeof arg)
 * @author acraciun
 */
public class TypeOfTemplate implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {
		int argCount = tree.getArguments().size();
		if (argCount != 1) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"A 'typeof' template can only be applied for methods with 1 parameter");
		}
		AstNode prop = visitor.scan(tree.getArguments().get(0), context).get(0);
		return Collections.<AstNode> singletonList(paren(unary(Token.TYPEOF, prop)));
	}
}
