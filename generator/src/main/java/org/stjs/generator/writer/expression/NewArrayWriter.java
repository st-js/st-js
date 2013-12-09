package org.stjs.generator.writer.expression;

import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.NewArrayTree;

/**
 * useless as arrays are supposed to be checked before
 * @author acraciun
 */
public class NewArrayWriter implements VisitorContributor<NewArrayTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, NewArrayTree tree,
			GenerationContext context, List<AstNode> prev) {
		throw new JavascriptFileGenerationException(context.getInputFile(), null, "Java arrays are not supported. This is a ST-JS bug.");
	}
}
