package org.stjs.generator.writer.expression;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.visitor.DiscriminatorKey;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;

public class MethodInvocationWriter<JS> implements WriterContributor<MethodInvocationTree, JS> {

	public static <JS, T extends MethodInvocationTree> JS buildTarget(WriterVisitor<JS> visitor, TreeWrapper<T, JS> tw) {
		ExpressionTree select = tw.getTree().getMethodSelect();

		if (select instanceof IdentifierTree) {
			// simple call: method(args)
			return MemberWriters.buildTarget(tw);
		}
		// calls with target: target.method(args)
		if (TreeUtils.isSuperCall(tw.getTree())) {
			// this is a call of type super.staticMethod(args) -> it should be handled as a simple call to
			// staticMethod
			return MemberWriters.buildTarget(tw);
		}
		MemberSelectTree memberSelect = (MemberSelectTree) select;
		JS targetJS = visitor.scan(memberSelect.getExpression(), tw.getContext());
		if (tw.isStatic() && !ElementUtils.isTypeKind(tw.child(memberSelect).child(memberSelect.getExpression()).getElement())) {
			//this is static method called from an instances: e.g. x.staticMethod()
			targetJS = tw.getContext().js().property(targetJS, JavascriptKeywords.CONSTRUCTOR);
		}

		return targetJS;
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
		String name = context.getCurrentWrapper().getMethodTemplate();
		if (name != null) {
			return name;
		}
		return context.getCurrentWrapper().getEnclosingType().isJavaScriptFunction() ? "invoke" : "none";
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		String templateName = buildTemplateName(tree, context);

		return visitor.forward(DiscriminatorKey.of(MethodInvocationWriter.class.getSimpleName(), templateName), tree, context);
	}
}
