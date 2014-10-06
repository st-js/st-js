package org.stjs.generator.check.expression;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.javascript.annotation.ServerSide;

import com.sun.source.tree.IdentifierTree;

public class IdentifierAccessServerSideCheck implements CheckContributor<IdentifierTree> {

	private boolean isRegularField(Element fieldElement, IdentifierTree tree) {
		if (fieldElement == null || fieldElement.getKind() != ElementKind.FIELD) {
			// only meant for fields
			return false;
		}
		if (GeneratorConstants.THIS.equals(tree.getName().toString()) || GeneratorConstants.SUPER.equals(tree.getName().toString())) {
			return false;
		}
		return true;
	}

	@Override
	public Void visit(CheckVisitor visitor, IdentifierTree tree, GenerationContext<Void> context) {
		Element fieldElement = TreeUtils.elementFromUse(tree);
		if (!isRegularField(fieldElement, tree)) {
			return null;
		}

		if (fieldElement.getAnnotation(ServerSide.class) != null) {
			context.addError(tree, "You cannot access fields annotated with @ServerSide in a client code");
		}

		return null;
	}
}
