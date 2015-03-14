package org.stjs.generator.writer.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree.Kind;

public class LiteralWriter<JS> implements WriterContributor<LiteralTree, JS> {

	@Override
	@SuppressWarnings("PMD.CyclomaticComplexity")
	public JS visit(WriterVisitor<JS> visitor, LiteralTree tree, GenerationContext<JS> context) {
		if (tree.getKind() == Kind.STRING_LITERAL || tree.getKind() == Kind.CHAR_LITERAL) {
			return tree.getKind() == Kind.STRING_LITERAL ? context.js().string(tree.getValue().toString()) : context.js().character(
					tree.getValue().toString());
		}
		if (tree.getKind() == Kind.NULL_LITERAL) {
			return context.js().keyword(Keyword.NULL);
		}
		if (tree.getKind() == Kind.BOOLEAN_LITERAL) {
			return context.js().keyword(Boolean.TRUE.equals(tree.getValue()) ? Keyword.TRUE : Keyword.FALSE);
		}
		return context.js().number((Number) tree.getValue());
	}
}
