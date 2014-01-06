package org.stjs.generator.check.expression;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreeScanner;

/**
 * this class checks that you don't use in the same method variables or parameters with the same name as a global scoped
 * field. This is to avoid situations like this one:
 * 
 * <pre>
 * void m() {
 * 	if (true) {
 * 		int x = 0;
 * 	}
 * 	int y = GlobalVars.x;
 * }
 * </pre>
 * 
 * After generation you'll have:
 * 
 * <pre>
 * function m() {
 * 	if (true) {
 * 		var x = 0;
 * 	}
 * 	var y = x;
 * }
 * </pre>
 * 
 * The global x will be in fact hidden by the local variable.
 * 
 * @author acraciun
 */
public class IdentifierGlobalScopeNameClashCheck implements CheckContributor<IdentifierTree> {

	private static MethodTree getEnclosingMethod(TreePath path) {
		for (TreePath p = path.getParentPath(); p != null; p = p.getParentPath()) {
			if (p.getLeaf() instanceof MethodTree) {
				return (MethodTree) p.getLeaf();
			}
		}
		return null;
	}

	private static void findVariablesInMethod(final String name, final GenerationContext<Void> context) {
		MethodTree enclosingMethod = getEnclosingMethod(context.getCurrentPath());

		if (enclosingMethod == null) {
			// don't see a reason why!?
			return;
		}
		enclosingMethod.accept(new TreeScanner<Void, Void>() {
			private boolean checkStopped;

			@Override
			public Void visitClass(ClassTree arg0, Void arg1) {
				// stop the checks if a new type is encountered
				checkStopped = true;
				return super.visitClass(arg0, arg1);
			}

			@Override
			public Void visitVariable(VariableTree var, Void arg1) {
				if (!checkStopped && var.getName().toString().equals(name)) {
					context.addError(var, "A variable with the same name as your global variable is already defined in this method's scope. "
							+ "Please rename either the local variable/parameter or the global variable.");
				}
				return super.visitVariable(var, arg1);
			}
		}, null);
	}

	public static Void checkGlobalScope(final ExpressionTree tree, final String name, final GenerationContext<Void> context) {
		Element fieldElement = TreeUtils.elementFromUse(tree);
		if (fieldElement == null || fieldElement.getKind() != ElementKind.FIELD) {
			// only meant for fields
			return null;
		}

		if (GeneratorConstants.THIS.equals(name)) {
			// getScope breaks if the identifier is "this"
			return null;
		}

		TreeWrapper<ExpressionTree, Void> tw = context.getCurrentWrapper();

		if (!tw.getEnclosingType().isGlobal()) {
			// check only global fields
			return null;
		}
		findVariablesInMethod(name, context);
		return null;
	}

	@Override
	public Void visit(CheckVisitor visitor, IdentifierTree tree, GenerationContext<Void> context) {

		return checkGlobalScope(tree, tree.getName().toString(), context);
	}

}
