package org.stjs.generator.check.expression;

import java.lang.reflect.Field;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;

/**
 * this checks that no java array is used. You should use {@link org.stjs.javascript.Array} instead.
 * 
 * @author acraciun
 */
public class NewArrayForbiddenCheck implements CheckContributor<NewArrayTree> {

	@Override
	public Void visit(CheckVisitor visitor, NewArrayTree tree, GenerationContext<Void> context) {
		if (isAnnotationParam(context.getCurrentPath()) || isPrimitive(tree)) {
			return null;
		}
		context.addError(tree,
				"You cannot use Java arrays because they are incompatible with Javascript arrays. "
						+ "Use org.stjs.javascript.Array<T> instead. "
						+ "You can use also the method org.stjs.javascript.Global.$castArray to convert an "
						+ "existent Java array to the corresponding Array type." + "The only exception is void main(String[] args).");

		return null;
	}

	private boolean isPrimitive(NewArrayTree tree) {
		Tree type = tree.getType();
		if (type == null) {
			try {
				Field field = tree.getClass().getField("type");
				Object ftype = field.get(tree);
				while (ftype instanceof com.sun.tools.javac.code.Type.ArrayType) {
					com.sun.tools.javac.code.Type.ArrayType atype = (com.sun.tools.javac.code.Type.ArrayType) ftype;
					com.sun.tools.javac.code.Type elemtype2 = atype.elemtype;
					boolean primitive = elemtype2.isPrimitive();
					if (primitive) {
						return primitive;
					} else {
						ftype = elemtype2;
					}
				}
			}
			catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				return false;
			}

		}
		return type instanceof PrimitiveTypeTree;
	}

	private boolean isAnnotationParam(TreePath currentPath) {
		return currentPath.getParentPath().getParentPath().getLeaf() instanceof AnnotationTree;
	}

}
