package org.stjs.generator.writer.declaration;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import com.sun.tools.javac.code.Type;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

public class MethodWriter<JS> implements WriterContributor<MethodTree, JS> {

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

	public static <JS> List<JS> getParams(List<? extends VariableTree> treeParams, GenerationContext<JS> context) {
		List<JS> params = new ArrayList<>();
		for (VariableTree param : treeParams) {
			if (InternalUtils.isVarArg(param)) {
				params.add(context.js().vararg(changeName(param.getName().toString())));
			} else {
				params.add(context.js().name(changeName(param.getName().toString())));
			}
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

		if (isMethodOfJavascriptFunction(context.getCurrentWrapper())) {
			return context.js().arrowFunction(params, body);
		}

		String methodName = context.getNames().getMethodName(context, tree, context.getCurrentPath());

		if (tw.getEnclosingType().isGlobal()) {
			JS decl = context.js().function(name, params, body);
			// var method=function() ...; //for global types
			return context.js().variableDeclaration(true, methodName, decl, true);
		}

		if (JavaNodes.isConstructor(tree)) {
			methodName = "constructor";

			if (body == null && params.size() == 0) {
				return null;
			}
		}

		// Generate interface methods differently
		TypeMirror type = context.getTrees().getTypeMirror(tw.getEnclosingType().getPath());


		if (!(type instanceof Type.ClassType)) {
			throw new RuntimeException("How do you even arrive here ?");
		}

		boolean isAbstract = tw.isAbstract();

		if (body == null) {
			body = context.js().block(new ArrayList<JS>());
		}

		if (tw.isAbstract()) {
			body = null;
		}

		if (((Type.ClassType) type).isInterface()) {
			isAbstract = false;
			body = null;
		}

		return context.js().method(methodName, params, body, tw.isStatic(), isAbstract);
	}

}
