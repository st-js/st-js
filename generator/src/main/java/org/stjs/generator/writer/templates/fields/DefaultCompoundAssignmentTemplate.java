package org.stjs.generator.writer.templates.fields;

import java.util.Arrays;
import java.util.Collections;

import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.javascript.BinaryOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.TreePath;

public class DefaultCompoundAssignmentTemplate<JS> implements WriterContributor<CompoundAssignmentTree, JS> {

	/**
	 * handle the case a /= b, where a and b are integers. it generates: a = stjs.trunc(a/(b));
	 */
	public static <JS> JS rightSide(JS left, JS right, CompoundAssignmentTree tree, GenerationContext<JS> context) {
		TypeMirror leftType = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getVariable()));
		TypeMirror rightType = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getExpression()));
		JavaScriptBuilder<JS> js = context.js();
		boolean integerDivision =
				tree.getKind() == Kind.DIVIDE_ASSIGNMENT && TypesUtils.isIntegral(leftType) && TypesUtils.isIntegral(rightType);

		if (integerDivision) {
			// force a cast for integer division to have the expected behavior in JavaScript too
			JS target = js.property(js.name("stjs"), "trunc");
			JS expr = js.binary(BinaryOperator.DIVIDE, Arrays.asList(left, js.paren(right)));
			return js.functionCall(target, Collections.singleton(expr));
		}
		return right;
	}

	public static <JS> AssignOperator getAssignOperator(CompoundAssignmentTree tree, GenerationContext<JS> context) {
		TypeMirror leftType = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getVariable()));
		TypeMirror rightType = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getExpression()));
		boolean integerDivision =
				tree.getKind() == Kind.DIVIDE_ASSIGNMENT && TypesUtils.isIntegral(leftType) && TypesUtils.isIntegral(rightType);
		return integerDivision ? AssignOperator.ASSIGN : AssignOperator.valueOf(tree.getKind());
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, CompoundAssignmentTree tree, GenerationContext<JS> context) {
		JS left = visitor.scan(tree.getVariable(), context);
		JS right = visitor.scan(tree.getExpression(), context);

		return context.js().assignment(getAssignOperator(tree, context), left, rightSide(left, right, tree, context));
	}

}
