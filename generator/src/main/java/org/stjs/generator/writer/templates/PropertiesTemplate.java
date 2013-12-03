package org.stjs.generator.writer.templates;

import static org.stjs.generator.writer.JavaScriptNodes.paren;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $properties(obj) -> obj
 * @author acraciun
 */
public class PropertiesTemplate implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {
		int argCount = tree.getArguments().size();
		if (argCount != 1) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"A 'properties' template can only be applied for methods with 1 parameter");
		}

		return Collections.<AstNode> singletonList(paren(visitor.scan(tree.getArguments().get(0), context).get(0)));
	}
}
