package org.stjs.generator.writer.templates.fields;

import static org.stjs.generator.writer.templates.DefaultTemplate.isConvertedEquals;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.UnaryTree;

/**
 * Java unary operator
 *
 * @author acraciun
 */
public class DefaultUnaryTemplate<JS> implements WriterContributor<UnaryTree, JS> {

	private boolean isNotEquals(UnaryOperator op, Tree tree, GenerationContext<JS> context) {
		if (!UnaryOperator.LOGICAL_COMPLEMENT.equals(op)) {
			return false;
		}

		if (!(tree instanceof MethodInvocationTree)) {
			return false;
		}

		Element methodElement = TreeUtils.elementFromUse((MethodInvocationTree) tree);
		if (JavaNodes.isStatic(methodElement)) {
			return false;
		}

		String name = MethodInvocationWriter.buildMethodName((MethodInvocationTree) tree);
		if (!"equals".equals(name)) {
			return false;
		}

		String methodOwner = methodElement.getEnclosingElement().toString();
		return isConvertedEquals(methodOwner);
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, UnaryTree tree, GenerationContext<JS> context) {
		JS operand = visitor.scan(tree.getExpression(), context);
		UnaryOperator op = UnaryOperator.valueOf(tree.getKind());
		assert op != null : "Unknow operator:" + tree.getKind();

		// If it's a not equal that is converted separately,
		// we'll just do that in the equals
		if (isNotEquals(op, tree.getExpression(), context)) {
			return operand;
		}

		return context.js().unary(op, operand);
	}
}
