package org.stjs.generator.writer.templates;

import java.util.Collections;

import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;

/**
 * $js("code") -> code
 * @author acraciun
 */
public class JsTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
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
