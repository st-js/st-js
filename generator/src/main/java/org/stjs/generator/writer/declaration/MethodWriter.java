package org.stjs.generator.writer.declaration;

import static org.stjs.generator.writer.JavaScriptNodes.assignment;
import static org.stjs.generator.writer.JavaScriptNodes.name;
import static org.stjs.generator.writer.JavaScriptNodes.statement;

import java.util.Collections;
import java.util.List;

import javacutils.InternalUtils;
import javacutils.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;

public class MethodWriter extends AbstractMemberWriter implements VisitorContributor<MethodTree, List<AstNode>, GenerationContext> {

	private String changeName(String name) {
		if (name.equals(GeneratorConstants.ARGUMENTS_PARAMETER)) {
			return "_" + name;
		}

		return name.toString();
	}

	/**
	 * @return true if this method is the unique method of an online declaration
	 */
	private boolean isMethodOfJavascriptFunction(TreePath path) {
		if (path.getParentPath().getLeaf() instanceof NewClassTree) {
			return JavaNodes.isJavaScriptFunction(TreeUtils.elementFromUse((NewClassTree) path.getParentPath().getLeaf()));
		}
		return false;
	}

	private void setAnonymousTypeConstructorName(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodTree tree,
			GenerationContext context, FunctionNode decl) {
		if (!JavaNodes.isConstructor(tree)) {
			return;
		}
		Element typeElement = TreeUtils.elementFromDeclaration((ClassTree) context.getCurrentPath().getParentPath().getLeaf());
		boolean anonymous = typeElement.getSimpleName().toString().isEmpty();
		if (anonymous) {
			decl.setFunctionName(name(InternalUtils.getSimpleName(typeElement)));
		}
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodTree tree,
			GenerationContext context, List<AstNode> prev) {
		if (tree.getModifiers().getFlags().contains(Modifier.NATIVE)) {
			// native methods are there only to indicate already existing javascript code - or to allow method
			// overloading
			return Collections.emptyList();
		}
		if (tree.getModifiers().getFlags().contains(Modifier.ABSTRACT)) {
			// abstract methods (from interfaces) are not generated
			return Collections.emptyList();
		}

		FunctionNode decl = new FunctionNode();
		for (VariableTree param : tree.getParameters()) {
			if (GeneratorConstants.SPECIAL_THIS.equals(param.getName().toString())) {
				continue;
			}
			decl.addParam(name(changeName(param.getName().toString())));
		}
		decl.setBody(visitor.scan(tree.getBody(), context).get(0));

		// set if needed Type$1 name, if this is an anonymous type constructor
		setAnonymousTypeConstructorName(visitor, tree, context, decl);

		// add the constructor.<name> or prototype.<name> if needed
		if (!JavaNodes.isConstructor(tree) && !isMethodOfJavascriptFunction(context.getCurrentPath())) {
			String methodName = context.getNames().getMethodName(context, tree, context.getCurrentPath());
			return Collections.<AstNode> singletonList(statement(assignment(getMemberTarget(tree), methodName, decl)));
		}

		return Collections.<AstNode> singletonList(decl);
	}
}
