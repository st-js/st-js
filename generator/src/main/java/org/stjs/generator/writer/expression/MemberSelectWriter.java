package org.stjs.generator.writer.expression;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.MemberSelectTree;

/**
 * write member select access, for fields, types and methods.
 * 
 * @author acraciun
 * 
 * @param <JS>
 */
public class MemberSelectWriter<JS> implements WriterContributor<MemberSelectTree, JS> {

	private JS getTarget(WriterVisitor<JS> visitor, MemberSelectTree tree, GenerationContext<JS> context) {
		if (JavaNodes.isSuper(tree.getExpression())) {
			// super.field does not make sense, so convert it to this
			return context.js().keyword(Keyword.THIS);
		}
		return visitor.scan(tree.getExpression(), context);

	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MemberSelectTree tree, GenerationContext<JS> context) {
		// this is only for fields. Methods are handled in MethodInvocationWriter

		Element element = TreeUtils.elementFromUse(tree);
		if (element == null || element.getKind() == ElementKind.PACKAGE) {
			// package names are ignored
			return null;
		}
		if (element.getKind() == ElementKind.CLASS && JavaNodes.isGlobal(element)) {
			// global classes are ignored
			return null;
		}

		JS target = getTarget(visitor, tree, context);

		if (GeneratorConstants.CLASS.equals(tree.getIdentifier().toString())) {
			// When ClassName.class -> ClassName
			return target;
		}
		return context.js().property(target, tree.getIdentifier().toString());
	}
}
