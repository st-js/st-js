package org.stjs.generator.writer.templates.fields;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import com.sun.source.tree.MethodInvocationTree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.Tree;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

/**
 * write member select access, for fields, types and methods.
 *
 * @author acraciun
 * @param <JS>
 */
public class GetterMemberSelectTemplate<JS> implements WriterContributor<MemberSelectTree, JS> {

	private JS getTarget(WriterVisitor<JS> visitor, MemberSelectTree tree, GenerationContext<JS> context) {
		if (JavaNodes.isSuper(tree.getExpression())) {
			// super.field does not make sense, so convert it to this
			return context.js().keyword(Keyword.THIS);
		}

		TreeWrapper<IdentifierTree, JS> tw = context.getCurrentWrapper();
		Tree target = tree.getExpression();
		JS targetJS = visitor.scan(target, context);
		if (tw.isStatic() && !ElementUtils.isTypeKind(tw.child(target).getElement())) {
			// this is static method called from an instances: e.g. x.staticField
			targetJS = tw.getContext().js().property(targetJS, JavascriptKeywords.CONSTRUCTOR);
		}

		return targetJS;
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MemberSelectTree tree, GenerationContext<JS> context) {
		return doVisit(visitor, tree, context, false);
	}

	protected JS doVisit(WriterVisitor<JS> visitor, MemberSelectTree tree, GenerationContext<JS> context, boolean global) {
		// this is only for fields.
		TreeWrapper<IdentifierTree, JS> tw = context.getCurrentWrapper();
		Element element = tw.getElement();
		if (element == null || element.getKind() == ElementKind.PACKAGE) {
			// package names are ignored
			return null;
		}

		JS target = getTarget(visitor, tree, context);

		JS name = context.js().string(tree.getIdentifier().toString());

		List<JS> arguments = new ArrayList<JS>();
		arguments.add(name);

		if (global) {
			return context.js().elementGet(target, name);
		}
		return context.js().functionCall(context.js().property(target, "get"), arguments);
	}
}
