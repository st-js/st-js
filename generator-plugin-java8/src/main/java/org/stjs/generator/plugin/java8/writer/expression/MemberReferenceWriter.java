package org.stjs.generator.plugin.java8.writer.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberReferenceTree.ReferenceMode;
import com.sun.source.util.TreePath;

/**
 * this class is for reference to a member like:<br>
 * 
 * Static method reference: String::valueOf
 * 
 * Non-static method reference: Object::toString
 * 
 * Capturing method reference: x::toString
 * 
 * Constructor reference: ArrayList::new
 * 
 * @author acraciun
 * 
 */
public class MemberReferenceWriter<JS> implements WriterContributor<MemberReferenceTree, JS> {

	private List<JS> generateArguments(GenerationContext<JS> context, int n) {
		JavaScriptBuilder<JS> js = context.js();
		List<JS> args = new ArrayList<>();
		for (int i = 0; i < n; ++i)
			args.add(js.elementGet(js.name("arguments"), js.number(i)));
		return args;
	}

	/**
	 * generate Type.method
	 */
	private JS generateStaticRef(MemberReferenceTree tree, GenerationContext<JS> context, ExecutableElement methodElement) {
		JavaScriptBuilder<JS> js = context.js();
		Element type = methodElement.getEnclosingElement();
		JS typeName = js.name(context.getNames().getTypeName(context, type));
		return js.property(typeName, tree.getName());
	}

	/**
	 * generate function() {return Type.prototype.method.call(arguments[0], arguments[1], ...);}
	 */
	private JS generateInstanceRef(MemberReferenceTree tree, GenerationContext<JS> context, ExecutableElement methodElement) {
		JavaScriptBuilder<JS> js = context.js();
		Element type = methodElement.getEnclosingElement();
		JS typeName = js.name(context.getNames().getTypeName(context, type));
		JS proto = js.property(typeName, JavascriptKeywords.PROTOTYPE);
		JS method = js.property(proto, tree.getName());
		JS methoCall = js.property(method, "call");
		// + 1 because first is the object to which the method is applied
		JS call = js.functionCall(methoCall, generateArguments(context, methodElement.getParameters().size() + 1));
		return js.function(null, Collections.emptyList(), js.returnStatement(call));
	}

	/**
	 * generate function() { return x.method(arguments[0], arguments[1]);}
	 */
	private JS generateCapturingRef(WriterVisitor<JS> visitor, MemberReferenceTree tree, GenerationContext<JS> context,
			ExecutableElement methodElement) {
		JavaScriptBuilder<JS> js = context.js();
		JS methodName = js.property(visitor.scan(tree.getQualifierExpression(), context), tree.getName());
		JS methodCall = js.functionCall(methodName, generateArguments(context, methodElement.getParameters().size()));
		return js.function(null, Collections.emptyList(), js.returnStatement(methodCall));
	}

	/**
	 * generate function() {new Type(arguments[0], arguments[1]);}
	 */
	private JS generateConstructorRef(GenerationContext<JS> context, ExecutableElement methodElement) {
		JavaScriptBuilder<JS> js = context.js();
		Element type = methodElement.getEnclosingElement();

		JS typeName = js.name(context.getNames().getTypeName(context, type));
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
			if (qualifierElement.getKind() == ElementKind.CLASS) {
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
