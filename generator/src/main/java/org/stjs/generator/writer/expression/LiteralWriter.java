package org.stjs.generator.writer.expression;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.StringLiteral;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree.Kind;

public class LiteralWriter implements VisitorContributor<LiteralTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, LiteralTree tree, GenerationContext p,
			List<AstNode> prev) {
		if (tree.getKind() == Kind.STRING_LITERAL || tree.getKind() == Kind.CHAR_LITERAL) {
			StringLiteral expr = new StringLiteral();
			expr.setValue(tree.getValue().toString());
			return Collections.<AstNode>singletonList(expr);
		}
		if (tree.getKind() == Kind.NULL_LITERAL) {
			KeywordLiteral expr = new KeywordLiteral();
			expr.setType(Token.NULL);
			return Collections.<AstNode>singletonList(expr);
		}
		if (tree.getKind() == Kind.BOOLEAN_LITERAL) {
			KeywordLiteral expr = new KeywordLiteral();
			expr.setType(Boolean.TRUE.equals(tree.getValue()) ? Token.TRUE : Token.FALSE);
			return Collections.<AstNode>singletonList(expr);
		}
		NumberLiteral expr = new NumberLiteral();
		expr.setValue(tree.getValue().toString());
		return Collections.<AstNode>singletonList(expr);
	}
}
