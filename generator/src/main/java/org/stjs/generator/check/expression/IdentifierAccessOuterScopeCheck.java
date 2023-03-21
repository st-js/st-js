package org.stjs.generator.check.expression;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.JavaNodes;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;

public class IdentifierAccessOuterScopeCheck implements CheckContributor<IdentifierTree> {

	private static boolean isInsideInizializerBlock(TreePath path) {
		TreePath p = path;
		while (p != null) {
			if (p.getLeaf() instanceof BlockTree && p.getParentPath().getLeaf() instanceof ClassTree) {
				return true;
			}
			if (p.getLeaf() instanceof MethodTree) {
				return false;
			}
			p = p.getParentPath();
		}
		return false;
	}

	/**
	 * if the block is an anonymous initializer, then return the outer class
	 *
	 * @param element
	 * @return
	 */
	public static ClassTree enclosingClassSkipAnonymousInitializer(TreePath path) {
		TreePath enclosingClassTreePath = TreeUtils.enclosingPathOfType(path, ClassTree.class);
		if (isInsideInizializerBlock(path)) {
			// get the outer class
			TreePath outerClassTreePath = TreeUtils.enclosingPathOfType(enclosingClassTreePath.getParentPath(), ClassTree.class);
			if (outerClassTreePath != null) {
				enclosingClassTreePath = outerClassTreePath;
			}
		}
		return (ClassTree) enclosingClassTreePath.getLeaf();
	}

	/**
	 * true if outerType is striclty the outer type of the subtype
	 *
	 * @param context
	 * @param outerType
	 * @param subType
	 * @return
	 */
	public static boolean isOuterType(GenerationContext<Void> context, TypeElement outerType, TypeElement subType) {
		TypeMirror subTypeErasure = context.getTypes().erasure(subType.asType());

		if (!(subTypeErasure instanceof DeclaredType)) {
			return false;
		}

		TypeMirror outerTypeErasure = context.getTypes().erasure(outerType.asType());
		for (TypeMirror type = ((DeclaredType) subTypeErasure).getEnclosingType(); type != null; type = ((DeclaredType) type).getEnclosingType()) {
			if (isSameType(type, outerTypeErasure)) {
				return true;
			}
			if (!(type instanceof DeclaredType)) {
				return false;
			}
		}
		return false;
	}

	private static boolean isSameType(TypeMirror type1, TypeMirror type2) {
		// XXX not sure if the classloader would not mess up this equals if tests is done against types themselves
		return type1.toString().equals(type2.toString());
	}

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

		ClassTree enclosingClassTree = enclosingClassSkipAnonymousInitializer(context.getCurrentPath());

		TypeElement currentScopeClassElement = TreeUtils.elementFromDeclaration(enclosingClassTree);
		TypeElement fieldOwnerElement = (TypeElement) fieldElement.getEnclosingElement();
		if (isOuterType(context, fieldOwnerElement, currentScopeClassElement)) {
			context.addError(
					tree,
					"In Javascript you cannot access a field from the outer type. "
							+ "You should define a variable var that=this outside your function definition and use the property of this object. The field: "
							+ tree);
		}

		return null;
	}
}
