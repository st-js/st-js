package org.stjs.generator.plugin.java8.check.expression;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.plugin.java8.util.Java8InternalUtils;
import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

import com.sun.source.tree.LambdaExpressionTree;

public class LambdaConvertedToOneMethodInterfacesCheck implements CheckContributor<LambdaExpressionTree> {

	private Element getMethod(TypeElement typeElement) {
		return typeElement.getEnclosedElements().stream()
				.filter(element -> element.getKind() == ElementKind.METHOD).findFirst().orElseGet(null);
	}

	private String getTemplateValue(GenerationContext<Void> ctx, Element element) {
		Template tpl = ctx.getAnnotation(element, Template.class);
		if (tpl == null) {
			return null;
		}
		return tpl.value();
	}

	@Override
	public Void visit(CheckVisitor visitor, LambdaExpressionTree tree, GenerationContext<Void> ctx) {
		for (TypeMirror typeMirror : Java8InternalUtils.getPossibleTypes(tree)) {
			TypeElement typeElement = (TypeElement) ctx.getTypes().asElement(typeMirror);
			JavascriptFunction jsFunctionAnnotation = ctx.getAnnotation(typeElement, JavascriptFunction.class);

			Element singleMethod = getMethod(typeElement);
			if (singleMethod != null) {
				String template = getTemplateValue(ctx, singleMethod);
				if (template == null || !template.equals("invoke")) {
					ctx.addError(tree, "Using a lambda expression for an interface that is not annotation with @JavascriptFunction or having a method annotated with @Template(\"invoke\"). "
							+ "This may create problems if the code that expects object implementing this interface will call the interface's single method. "
							+ "Please either annotated correctly the interface that you implement or replace this code with a annonymous class implementing the interface.");
				}
			}

		}

		return null;
	}
}
