package org.stjs.generator.plugin.java8.check.expression;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.check.expression.IdentifierAccessOuterScopeCheck;
import org.stjs.generator.javac.TreeUtils;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.util.TreePath;

public class FieldAccessFromLambdaCheck implements CheckContributor<IdentifierTree> {
	@Override
	public Void visit(CheckVisitor visitor, IdentifierTree tree, GenerationContext<Void> context) {
		Element fieldElement = TreeUtils.elementFromUse(tree);
		if (!IdentifierAccessOuterScopeCheck.isRegularInstanceField(fieldElement, tree)
				&& !GeneratorConstants.THIS.equals(tree.getName().toString())) {
			return null;
		}

		TreePath enclosingLambdaPath = TreeUtils.enclosingPathOfType(context.getCurrentPath(), LambdaExpressionTree.class);

		if (enclosingLambdaPath != null) {
			context.addError(
					tree,
					"In Javascript you cannot access a field from the outer type. "
							+ "You should define a variable var that=this outside your lamda expression and use the property of this object. The field: "
							+ tree);
		}

		return null;
	}
}
