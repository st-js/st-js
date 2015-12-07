package org.stjs.generator.check.expression;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.utils.Scopes;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

public class IdentifierAccessOuterScopeCheck implements CheckContributor<IdentifierTree> {

	public static boolean isRegularInstanceField(Element fieldElement, IdentifierTree tree) {
		if (fieldElement == null || fieldElement.getKind() != ElementKind.FIELD) {
			// only meant for fields
			return false;
		}
		if (JavaNodes.isStatic(fieldElement)) {
			// only instance fieds
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
		if (!isRegularInstanceField(fieldElement, tree)) {
			return null;
		}

		ClassTree enclosingClassTree = Scopes.findEnclosingClassSkipAnonymousInitializer(context.getCurrentPath());

		TypeElement currentScopeClassElement = TreeUtils.elementFromDeclaration(enclosingClassTree);
		TypeElement fieldOwnerElement = (TypeElement) fieldElement.getEnclosingElement();
		if (Scopes.isOuterType(context, fieldOwnerElement, currentScopeClassElement)) {
			context.addError(
					tree,
					"In Javascript you cannot access a field from the outer type. "
							+ "You should define a variable var that=this outside your function definition and use the property of this object. The field: "
							+ tree);
		}

		return null;
	}
}
