package org.stjs.generator.check.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavascriptKeywords;

import com.sun.source.tree.VariableTree;

public class VariableWrongNameCheck implements VisitorContributor<VariableTree, Void, GenerationContext> {
	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, VariableTree tree, GenerationContext context, Void v) {
		//TODO add new SourcePosition(n)
		JavascriptKeywords.checkIdentifier(tree, tree.getName().toString(), context);
		return null;
	}

}
