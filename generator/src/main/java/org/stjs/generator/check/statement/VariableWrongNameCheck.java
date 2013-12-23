package org.stjs.generator.check.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.writer.JavascriptKeywords;

import com.sun.source.tree.VariableTree;

/**
 * check that no JavaScript keyword (that is not a Java keyword) is used as a variable name
 * 
 * @author acraciun
 */
public class VariableWrongNameCheck implements CheckContributor<VariableTree> {
	@Override
	public Void visit(CheckVisitor visitor, VariableTree tree, GenerationContext<Void> context) {
		JavascriptKeywords.checkIdentifier(tree, tree.getName().toString(), context);
		return null;
	}

}
