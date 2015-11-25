package org.stjs.generator.writer.templates.fields;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.Tree;

/**
 * write member select access, for fields, types and methods.
 * @author acraciun
 * @param <JS>
 */
public class DefaultMemberSelectTemplate<JS> implements WriterContributor<MemberSelectTree, JS> {

	private JS getTarget(WriterVisitor<JS> visitor, MemberSelectTree tree, GenerationContext<JS> context) {
		if (JavaNodes.isSuper(tree.getExpression())) {
			// super.field does not make sense, so convert it to this
			return context.js().keyword(Keyword.THIS);
		}

		TreeWrapper<IdentifierTree, JS> tw = context.getCurrentWrapper();
		Tree target = tree.getExpression();
		JS targetJS = visitor.scan(target, context);
		if (tw.isStatic() && !ElementUtils.isTypeKind(tw.child(target).getElement())) {
			//this is static method called from an instances: e.g. x.staticField
			targetJS = tw.getContext().js().property(targetJS, JavascriptKeywords.CONSTRUCTOR);
		}

		return targetJS;
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MemberSelectTree tree, GenerationContext<JS> context) {
		// this is only for fields. Methods are handled in MethodInvocationWriter
		TreeWrapper<IdentifierTree, JS> tw = context.getCurrentWrapper();
		Element element = tw.getElement();
		if (element == null || element.getKind() == ElementKind.PACKAGE) {
			// package names are ignored
			return null;
		}
		if (element.getKind() == ElementKind.CLASS) {
			if (tw.isGlobal()) {
				// global classes are ignored
				return null;

			} else {
				// Non global classes however, are not ignored and a translated taking the namespace into account
				return context.js().name(context.getNames().getTypeName(context, element, DependencyType.STATIC));
			}
		}

		JS target = getTarget(visitor, tree, context);

		if (GeneratorConstants.CLASS.equals(tree.getIdentifier().toString())) {
			// When ClassName.class -> ClassName
			return target;
		}

		String fieldName = decorateMember(tree, element);

		return context.js().property(target, fieldName);
	}

	private String decorateMember(MemberSelectTree tree, Element element) {
		String fieldName = tree.getIdentifier().toString();
		if (!element.getModifiers().contains(Modifier.PUBLIC)) {
			fieldName = GeneratorConstants.NON_PUBLIC_METHODS_AND_FIELDS_PREFIX + fieldName;
		}
		return fieldName;
	}
}
