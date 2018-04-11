package org.stjs.generator.check.expression;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.writer.expression.MethodInvocationWriter;
import org.stjs.javascript.stjs.STJS;

import com.sun.source.tree.MethodInvocationTree;

/**
 * (c) Swissquote 05.04.18
 *
 * @author sgoetz
 */
public class MethodInvocationAnnotationsCheck implements CheckContributor<MethodInvocationTree> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visit(CheckVisitor visitor, MethodInvocationTree tree, GenerationContext<Void> context) {
		String name = MethodInvocationWriter.buildMethodName(tree);

		if (GeneratorConstants.SUPER.equals(name)) {
			return null;
		}

		// This only concerns the annotation methods in ST-JS
		if (
				!"getTypeAnnotation".equals(name)
				&& !"getAnnotations".equals(name)
				&& !"getMemberAnnotation".equals(name)
				&& !"getParameterAnnotation".equals(name)
				) {
			return null;
		}

		Element methodElement = InternalUtils.symbol(tree);
		TypeElement methodOwnerElement = (TypeElement) methodElement.getEnclosingElement();

		String canonicalName = STJS.class.getCanonicalName();
		if (canonicalName.equals(methodOwnerElement.toString())) {
			context.addError(tree, "In TypeScript annotations aren't supported . Called 'stjs." + name + "()'");
		}

		return null;
	}
}
