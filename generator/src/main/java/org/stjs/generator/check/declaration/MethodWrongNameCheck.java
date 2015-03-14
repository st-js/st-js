package org.stjs.generator.check.declaration;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.writer.JavascriptKeywords;

import com.sun.source.tree.MethodTree;

public class MethodWrongNameCheck implements CheckContributor<MethodTree> {

	@Override
	public Void visit(CheckVisitor visitor, MethodTree tree, GenerationContext<Void> context) {
		JavascriptKeywords.checkIdentifier(tree, tree.getName().toString(), context);
		return null;
	}

}
