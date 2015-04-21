package org.stjs.generator.writer.expression;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.visitor.DiscriminatorKey;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;

/**
 * write member select access, for fields, types and methods.
 *
 * @author acraciun
 * @param <JS>
 */
public class MemberSelectWriter<JS> implements WriterContributor<MemberSelectTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MemberSelectTree tree, GenerationContext<JS> context) {
		String templateName = buildTemplateName(context);

		return visitor.forward(DiscriminatorKey.of(MemberSelectWriter.class.getSimpleName(), templateName), tree, context);
	}

	private String buildTemplateName(GenerationContext<JS> context) {
		TreeWrapper<IdentifierTree, JS> tw = context.getCurrentWrapper();

		Element def = tw.getElement();

		if (def.getKind() == ElementKind.FIELD) {
			String template = tw.getFieldTemplate();
			if (template != null) {
				return template;
			}
			return "none";
		}
		return "none";
	}
}
