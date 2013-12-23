package org.stjs.generator.writer.declaration;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;

public class MethodWriter<JS> extends AbstractMemberWriter<JS> implements WriterContributor<MethodTree, JS> {

	private String changeName(String name) {
		if (name.equals(GeneratorConstants.ARGUMENTS_PARAMETER)) {
			return "_" + name;
		}

		return name;
	}

	/**
	 * @return true if this method is the unique method of an online declaration
	 */
	private boolean isMethodOfJavascriptFunction(TreePath path) {
		if (path.getParentPath().getLeaf() instanceof NewClassTree) {
			return JavaNodes.isJavaScriptFunction(TreeUtils.elementFromUse(((NewClassTree) path.getParentPath().getLeaf()).getIdentifier()));
		}
		return false;
	}

	private String getAnonymousTypeConstructorName(MethodTree tree, GenerationContext context) {
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

	private List<JS> getParams(MethodTree tree, GenerationContext<JS> context) {
		List<JS> params = new ArrayList<JS>();
		for (VariableTree param : tree.getParameters()) {
			if (GeneratorConstants.SPECIAL_THIS.equals(param.getName().toString())) {
				continue;
			}
			params.add(context.js().name(changeName(param.getName().toString())));
		}
		return params;
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodTree tree, GenerationContext<JS> context) {
		Element element = JavaNodes.elementFromDeclaration(tree);
		if (JavaNodes.isNative(element)) {
			// native methods are there only to indicate already existing javascript code - or to allow method
			// overloading
			return null;
		}
		if (tree.getModifiers().getFlags().contains(Modifier.ABSTRACT)) {
			// abstract methods (from interfaces) are not generated
			return null;
		}

		List<JS> params = getParams(tree, context);

		JS body = visitor.scan(tree.getBody(), context);

		// set if needed Type$1 name, if this is an anonymous type constructor
		String name = getAnonymousTypeConstructorName(tree, context);

		JS decl = context.js().function(name, params, body);

		// add the constructor.<name> or prototype.<name> if needed
		if (!JavaNodes.isConstructor(tree) && !isMethodOfJavascriptFunction(context.getCurrentPath())) {
			String methodName = context.getNames().getMethodName(context, tree, context.getCurrentPath());
			JS member = context.js().property(getMemberTarget(tree, context), methodName);
			return context.js().expressionStatement(context.js().assignment(AssignOperator.ASSIGN, member, decl));
		}

		return decl;
	}
}
