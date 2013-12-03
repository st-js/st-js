package org.stjs.generator.writer.templates;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;

/**
 * $js("code") -> code
 * @author acraciun
 */
public class JsTemplate implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {
		int argCount = tree.getArguments().size();
		if (argCount != 1) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"A 'js' template can only be applied for methods with 1 parameter");
		}
		if (tree.getArguments().get(0).getKind() != Tree.Kind.STRING_LITERAL) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null, "$js can be used only with string literals");
		}

		String code = ((LiteralTree) tree.getArguments().get(0)).getValue().toString();
		AstNode node = new Parser().parse(code, "inline", 0);
		return Collections.<AstNode> singletonList(node);
	}
}
