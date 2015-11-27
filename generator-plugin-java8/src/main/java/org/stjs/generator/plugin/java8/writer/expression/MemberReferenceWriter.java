package org.stjs.generator.plugin.java8.writer.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberReferenceTree.ReferenceMode;
import com.sun.source.util.TreePath;

/**
 * this class is for reference to a member like:<br>
 * Static method reference: String::valueOf <br>
 * Non-static method reference: Object::toString <br>
 * Capturing method reference: x::toString <br>
 * Constructor reference: ArrayList::new
 * @author acraciun
 */
public class MemberReferenceWriter<JS> implements WriterContributor<MemberReferenceTree, JS> {

	private List<JS> generateArguments(GenerationContext<JS> context, int n) {
		JavaScriptBuilder<JS> js = context.js();
		List<JS> args = new ArrayList<>();
		for (int i = 0; i < n; ++i) {
			args.add(js.elementGet(js.name("arguments"), js.number(i)));
		}
		return args;
	}

	/**
	 * Type::method -> generate Type.method
	 */
	private JS generateStaticRef(MemberReferenceTree tree, GenerationContext<JS> context, ExecutableElement methodElement) {
		JavaScriptBuilder<JS> js = context.js();
		Element type = methodElement.getEnclosingElement();
		JS typeName = js.name(context.getNames().getTypeName(context, type, DependencyType.STATIC));
		return js.property(typeName, tree.getName());
	}

	/**
	 * Type::method -> stjs.bind("method")
	 */
	private JS generateInstanceRef(MemberReferenceTree tree, GenerationContext<JS> context, ExecutableElement methodElement) {
		JavaScriptBuilder<JS> js = context.js();
		Element type = methodElement.getEnclosingElement();
		context.getNames().getTypeName(context, type, DependencyType.STATIC); // Make sure that we record the dependency on the type
		JS stjsBind = context.js().property(context.js().name("stjs"), "bind");
		JS methodName = js.string(tree.getName().toString());
		return js.functionCall(stjsBind, Collections.singletonList(methodName));
	}

	/**
	 * x::method -> stjs.bind(x, "method")
	 */
	private JS generateCapturingRef(WriterVisitor<JS> visitor, MemberReferenceTree tree, GenerationContext<JS> context,
			ExecutableElement methodElement) {
		JavaScriptBuilder<JS> js = context.js();
		JS target = visitor.scan(tree.getQualifierExpression(), context);
		JS methodName = js.string(tree.getName().toString());
		JS stjsBind = context.js().property(context.js().name("stjs"), "bind");
		return js.functionCall(stjsBind, Arrays.asList(target, methodName));
	}

	/**
	 * Type::new -> function() {new Type(arguments[0], arguments[1]);}
	 */
	private JS generateConstructorRef(GenerationContext<JS> context, ExecutableElement methodElement) {
		JavaScriptBuilder<JS> js = context.js();
		Element type = methodElement.getEnclosingElement();

		JS typeName = js.name(context.getNames().getTypeName(context, type, DependencyType.STATIC));
		JS newExpr = context.js().newExpression(typeName, generateArguments(context, methodElement.getParameters().size()));
		return js.function(null, Collections.emptyList(), js.returnStatement(newExpr));
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MemberReferenceTree tree, GenerationContext<JS> context) {
		ExecutableElement methodElement = (ExecutableElement) context.getTrees().getElement(context.getCurrentPath());
		Element qualifierElement = context.getTrees().getElement(new TreePath(context.getCurrentPath(), tree.getQualifierExpression()));

		// System.out.println(tree + ":left:" + tree.getQualifierExpression().getClass() + ", kind:" +
		// qualifierElemenet.getKind());
		if (tree.getMode() == ReferenceMode.INVOKE) {
			if (qualifierElement.getKind() == ElementKind.CLASS || qualifierElement.getKind() == ElementKind.INTERFACE) {
				if (JavaNodes.isStatic(methodElement)) {
					return generateStaticRef(tree, context, methodElement);
				}
				return generateInstanceRef(tree, context, methodElement);
			}
			return generateCapturingRef(visitor, tree, context, methodElement);
		}

		return generateConstructorRef(context, methodElement);

	}
}
