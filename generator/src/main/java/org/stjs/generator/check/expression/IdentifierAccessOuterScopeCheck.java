package org.stjs.generator.check.expression;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.Scope;

public class IdentifierAccessOuterScopeCheck implements VisitorContributor<IdentifierTree, Void, GenerationContext> {
	/**
	 * if the block is an anonymous initializer, then return the outer class
	 * 
	 * @param element
	 * @return
	 */
	public static TypeElement getEnclosingElementSkipAnonymousInitializer(TypeElement element) {
		if (element.getNestingKind() != NestingKind.ANONYMOUS) {
			return element;
		}
		return element.getSuperclass() instanceof DeclaredType ? (TypeElement) ((DeclaredType) element.getSuperclass()).asElement() : element;
	}

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, IdentifierTree tree, GenerationContext context, Void prev) {
		Element fieldElement = TreeUtils.elementFromUse(tree);
		if (fieldElement == null || fieldElement.getKind() != ElementKind.FIELD) {
			// only meant for fields
			return null;
		}
		if (JavaNodes.isStatic(fieldElement)) {
			// only instance fieds
			return null;
		}

		if (GeneratorConstants.THIS.equals(tree.getName().toString())) {
			// getScope breaks if the identifier is "this"
			return null;
		}
		Scope currentScope = context.getTrees().getScope(context.getCurrentPath());

		Element currentScopeClassElement = getEnclosingElementSkipAnonymousInitializer(currentScope.getEnclosingClass());
		Element fieldOwnerElement = fieldElement.getEnclosingElement();
		if (!context.getTypes().isSubtype(currentScopeClassElement.asType(), fieldOwnerElement.asType())) {
			context.addError(tree, "In Javascript you cannot call methods or fields from the outer type. "
					+ "You should define a variable var that=this outside your function definition and call the methods on this object");
		}
		return null;
	}
}
