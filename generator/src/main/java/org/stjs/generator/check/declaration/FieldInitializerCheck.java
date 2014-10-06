package org.stjs.generator.check.declaration;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.MemberWriters;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;

public class FieldInitializerCheck implements CheckContributor<VariableTree> {

	private boolean checkInitializer(VariableTree tree) {
		// no initializer -> ok
		if (tree.getInitializer() == null) {
			return true;
		}
		// allowed x = 1
		if (tree.getInitializer() instanceof LiteralTree) {
			return true;
		}
		// allowed x = -1
		if (tree.getInitializer() instanceof UnaryTree && ((UnaryTree) tree.getInitializer()).getExpression() instanceof LiteralTree) {
			return true;
		}
		return false;
	}

	@Override
	public Void visit(CheckVisitor visitor, VariableTree tree, GenerationContext<Void> context) {
		Element element = TreeUtils.elementFromDeclaration(tree);
		if (element.getKind() != ElementKind.FIELD) {
			// only deals with fields
			return null;
		}

		TreeWrapper<Tree, Void> tw = context.getCurrentWrapper();
		if (MemberWriters.shouldSkip(tw)) {
			return null;
		}

		// static -> ok1
		if (JavaNodes.isStatic(element)) {
			return null;
		}

		if (checkInitializer(tree)) {
			return null;
		}

		if (!JavaNodes.isJavaScriptPrimitive(context.getTrees().getTypeMirror(context.getCurrentPath()))) {
			context.addError(tree, "Instance field inline initialization is allowed only for string and number field types");
			return null;
		}
		context.addError(tree, "Instance field inline initialization can only done with literal constants");
		return null;
	}
}
