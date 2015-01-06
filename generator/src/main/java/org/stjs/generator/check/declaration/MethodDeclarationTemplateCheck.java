package org.stjs.generator.check.declaration;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.writer.MemberWriters;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

/**
 * this class verifies that you use @Template only inside a bridge
 * 
 * @author acraciun
 */
public class MethodDeclarationTemplateCheck implements CheckContributor<MethodTree> {

	@Override
	public Void visit(CheckVisitor visitor, MethodTree tree, GenerationContext<Void> context) {
		TreeWrapper<Tree, Void> tw = context.getCurrentWrapper();
		if (MemberWriters.shouldSkip(tw)) {
			return null;
		}
		if (context.getCurrentWrapper().getMethodTemplate() != null) {
			context.addError(tree, "You can only use @Template annotation for methods in bridge classes");
			return null;
		}

		return null;
	}
}
