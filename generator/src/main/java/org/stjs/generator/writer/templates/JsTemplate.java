package org.stjs.generator.writer.templates;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;

/**
 * $js("code") -> code
 * 
 * @author acraciun
 */
public class JsTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount != 1) {
			throw context.addError(tree, "A 'js' template can only be applied for methods with 1 parameter");
		}
		if (tree.getArguments().get(0).getKind() != Tree.Kind.STRING_LITERAL) {
			throw context.addError(tree, "$js can be used only with string literals");
		}

		String code = ((LiteralTree) tree.getArguments().get(0)).getValue().toString();
		return context.js().code(code);
	}
}
