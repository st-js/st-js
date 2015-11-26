package org.stjs.generator.writer.declaration;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import com.sun.tools.javac.code.Symbol;
import org.stjs.generator.AnnotationUtils;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

public class MethodWriter<JS> extends AbstractMemberWriter<JS> implements WriterContributor<MethodTree, JS> {

	private static String changeName(String name) {
		if (name.equals(GeneratorConstants.ARGUMENTS_PARAMETER)) {
			return "_" + name;
		}

		return name;
	}

	/**
	 * @return true if this method is the unique method of an online declaration
	 */
	private boolean isMethodOfJavascriptFunction(TreeWrapper<Tree, JS> treeWrapper) {
		TreeWrapper<Tree, JS> parent = treeWrapper.parent().parent();
		if (parent.getTree() instanceof NewClassTree) {
			return parent.child(((NewClassTree) parent.getTree()).getIdentifier()).isJavaScriptFunction();
		}
		return false;
	}

	private String getAnonymousTypeConstructorName(MethodTree tree, GenerationContext<JS> context) {
		if (!JavaNodes.isConstructor(tree)) {
			return null;
		}
		Element typeElement = TreeUtils.elementFromDeclaration((ClassTree) context.getCurrentPath().getParentPath().getLeaf());
		boolean anonymous = typeElement.getSimpleName().toString().isEmpty();
		if (anonymous) {
			return InternalUtils.getSimpleName(typeElement);
		}
		return null;
	}

	private String decorateMethodName(MethodTree tree, GenerationContext<JS> context) {
		Symbol.MethodSymbol element = (Symbol.MethodSymbol) context.getCurrentWrapper().getElement();
		String methodName = element.getSimpleName().toString();
		boolean isFromInterface = context.getCurrentWrapper().getEnclosingType().getElement().getKind().equals(ElementKind.INTERFACE);

		if (AnnotationUtils.JSOverloadName.isPresent(element)) {
			methodName = AnnotationUtils.JSOverloadName.decorate(element);
		}

		if (!JavaNodes.isPublic(tree) && !isFromInterface) {
			return GeneratorConstants.NON_PUBLIC_METHODS_AND_FIELDS_PREFIX + methodName;
		}

		return methodName;
	}

	public static <JS> List<JS> getParams(List<? extends VariableTree> treeParams, GenerationContext<JS> context) {
		List<JS> params = new ArrayList<JS>();
		for (VariableTree param : treeParams) {
			params.add(context.js().name(changeName(param.getName().toString())));
		}
		return params;
	}

	public static int getTHISParamPos(List<? extends VariableTree> parameters) {
		for (int i = 0; i < parameters.size(); ++i) {
			VariableTree param = parameters.get(i);
			if (GeneratorConstants.SPECIAL_THIS.equals(param.getName().toString())) {
				return i;
			}
		}
		return -1;
	}

	protected boolean accept(TreeWrapper<MethodTree, JS> tw) {
		if (tw.isNative()) {
			// native methods are there only to indicate already existing javascript code - or to allow method
			// overloading
			return false;
		}
		if (MemberWriters.shouldSkip(tw)) {
			return false;
		}

		return true;
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodTree tree, GenerationContext<JS> context) {
		TreeWrapper<MethodTree, JS> tw = context.getCurrentWrapper();
		if (!accept(tw)) {
			return null;
		}

		List<JS> params = getParams(tree.getParameters(), context);

		JS body = visitor.scan(tree.getBody(), context);

		// set if needed Type$1 name, if this is an anonymous type constructor
		String name = getAnonymousTypeConstructorName(tree, context);

		JS decl = context.js().function(name, params, body);

		// add the constructor.<name> or prototype.<name> if needed
		if (!JavaNodes.isConstructor(tree) && !isMethodOfJavascriptFunction(context.getCurrentWrapper())) {
			String methodName = decorateMethodName(tree, context);
			if (tw.getEnclosingType().isGlobal()) {
				// var method=function() ...; //for global types
				return context.js().variableDeclaration(true, methodName, decl);
			}
			JS member = context.js().property(getMemberTarget(tw), methodName);
			return context.js().expressionStatement(context.js().assignment(AssignOperator.ASSIGN, member, decl));
		}

		return decl;
	}
}
