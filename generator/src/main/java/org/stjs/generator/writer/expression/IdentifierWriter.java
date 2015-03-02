package org.stjs.generator.writer.expression;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.visitor.DiscriminatorKey;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.IdentifierTree;

/**
 * this class deal with identifiers likes variable, field references.
 *
 * @author acraciun
 */
public class IdentifierWriter<JS> implements WriterContributor<IdentifierTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, IdentifierTree tree, GenerationContext<JS> context) {
		String templateName = buildTemplateName(tree, context);

		return visitor.forward(DiscriminatorKey.of(IdentifierWriter.class.getSimpleName(), templateName), tree, context);
	}

	private String buildTemplateName(IdentifierTree tree, GenerationContext<JS> context) {
		String name = tree.getName().toString();
		if (GeneratorConstants.THIS.equals(name)) {
			return "none";
		}
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
