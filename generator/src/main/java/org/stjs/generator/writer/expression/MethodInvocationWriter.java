package org.stjs.generator.writer.expression;

import java.util.ArrayList;
import java.util.List;

import javacutils.TreeUtils;

import javax.lang.model.element.ExecutableElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.DiscriminatorKey;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;

public class MethodInvocationWriter<JS> implements WriterContributor<MethodInvocationTree, JS> {

	public static <JS> JS buildTarget(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		ExpressionTree select = tree.getMethodSelect();
		ExecutableElement methodDecl = TreeUtils.elementFromUse(tree);
		assert methodDecl != null : "Cannot find the definition for method  " + tree.getMethodSelect();

		if (select instanceof IdentifierTree) {
			// simple call: method(args)
			return MemberWriters.buildTarget(context, methodDecl);
		}
		// calls with target: target.method(args)
		MemberSelectTree memberSelect = (MemberSelectTree) select;
		if (TreeUtils.isSuperCall(tree)) {
			// this is a call of type super.staticMethod(args) -> it should be handled as a simple call to
			// staticMethod
			return MemberWriters.buildTarget(context, methodDecl);
		}
		return visitor.scan(memberSelect.getExpression(), context);
	}

	public static String buildMethodName(MethodInvocationTree tree) {
		ExpressionTree select = tree.getMethodSelect();
		if (select instanceof IdentifierTree) {
			// simple call: method(args)
			return ((IdentifierTree) select).getName().toString();
		}
		// calls with target: target.method(args)
		MemberSelectTree memberSelect = (MemberSelectTree) select;
		return memberSelect.getIdentifier().toString();
	}

	public static <JS> List<JS> buildArguments(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		List<JS> arguments = new ArrayList<JS>();
		for (Tree arg : tree.getArguments()) {
			arguments.add(visitor.scan(arg, context));
		}
		return arguments;
	}

	public static <JS> String buildTemplateName(MethodInvocationTree tree, GenerationContext<JS> context) {
		ExecutableElement methodDecl = TreeUtils.elementFromUse(tree);
		String name = JavaNodes.getMethodTemplate(context.getElements(), methodDecl);
		if (name != null) {
			return name;
		}
		return JavaNodes.isJavaScriptFunction(methodDecl.getEnclosingElement()) ? "invoke" : "none";
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		String templateName = buildTemplateName(tree, context);

		return visitor.forward(DiscriminatorKey.of(MethodInvocationWriter.class.getSimpleName(), templateName), tree, context);
	}
}
