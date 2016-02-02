package org.stjs.generator.writer.declaration;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.NamespaceUtil;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.utils.FieldUtils;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.utils.Scopes;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.javascript.annotation.Namespace;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
		return context.getNames().getMethodName(context, tree);
	}

	private boolean isFromInterface(GenerationContext<JS> context) {
		return ElementKind.INTERFACE.equals(context.getCurrentWrapper().getEnclosingType().getElement().getKind());
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
	@SuppressWarnings("PMD.CyclomaticComplexity")
	public JS visit(WriterVisitor<JS> visitor, MethodTree tree, GenerationContext<JS> context) {
		TreeWrapper<MethodTree, JS> tw = context.getCurrentWrapper();
		if (!accept(tw)) {
			return null;
		}

		List<JS> params = getParams(tree.getParameters(), context);

		JS body = visitor.scan(tree.getBody(), context);

//		if (JavaNodes.isConstructor(tree) && JavaNodes.hasMultipleConstructors(context.getCurrentPath())) {
//			// Multiple constructors are generated as static methods within the class
//			//    prototype._constructor$String = function(string) {
//			body = context.withPosition(tree, context.js().block(new ArrayList<JS>()));
//		}

		// set if needed Type$1 name, if this is an anonymous type constructor
		String anonymousTypeConstructorName = getAnonymousTypeConstructorName(tree, context);
		boolean isAnonymousConstructor = (anonymousTypeConstructorName != null);

		if (!isAnonymousConstructor) {
			decorateBodyWithScopeAccessor(tree, context, body);
		}

		if (JavaNodes.isConstructor(tree) && JavaNodes.hasMultipleConstructors(context.getCurrentPath())) {
			params.clear();
		}

		JS decl = context.js().function(anonymousTypeConstructorName, params, body);

		JS constructorOrPrototypePrefix = addConstructorOrPrototypePrefixIfNeeded(tree, context, tw, decl);
		if (constructorOrPrototypePrefix == null) {
			return decl;
		} else {
			return constructorOrPrototypePrefix;
		}
	}

	private void decorateBodyWithScopeAccessor(MethodTree tree, GenerationContext<JS> context, JS body) {
		JS scopeAccessor = addScopeAccessorIfNeeded(tree, context, body);
		if (scopeAccessor != null) {
            context.js().addStatementBeginning(body, scopeAccessor);
        }
	}

	private boolean isInnerClass(MethodTree tree) {
		Element classElement = TreeUtils.elementFromDeclaration(tree).getEnclosingElement();
		return ElementUtils.isInnerClass(classElement);
	}

	private boolean isStaticNestedClass(MethodTree tree) {
		Element classElement = TreeUtils.elementFromDeclaration(tree).getEnclosingElement();
		return ElementUtils.isInnerClass(classElement) && ElementUtils.isStatic(classElement);
	}

	private String getOuterClassAccessorParamName(MethodTree tree) {
		Element element = TreeUtils.elementFromDeclaration(tree);
		int deepnessLevel = Scopes.getElementDeepnessLevel(element);
		return GeneratorConstants.INNER_CLASS_CONSTRUCTOR_PARAM_PREFIX + GeneratorConstants.AUTO_GENERATED_ELEMENT_SEPARATOR + deepnessLevel;
	}

	/**
	 * Evaluate if any accessor may require our current scope.
	 *
	 * For anonymous methods:
	 * 	Return a "var this$X = this;"
	 *
	 * For normal inner classes:
	 *  Return "this._outerClass$x = outerClass$x;"
	 *
	 * @param tree
	 * @param context
	 * @param body
	 * @return
     */
	private JS addScopeAccessorIfNeeded(MethodTree tree, GenerationContext<JS> context, JS body) {
		if (isFromInterface(context) || isMethodOfJavascriptFunction(context.getCurrentWrapper())) {
			return null;
		}

		Element element = TreeUtils.elementFromDeclaration(tree);
		int deepnessLevel = Scopes.getElementDeepnessLevel(element);

		String scopeAccessorPrefix = GeneratorConstants.THIS;
		JS scopeAccessorValue = context.js().name(GeneratorConstants.THIS);

		String scopeAccessorVariable = scopeAccessorPrefix + GeneratorConstants.AUTO_GENERATED_ELEMENT_SEPARATOR + deepnessLevel;

		if (findAccessor(body, scopeAccessorVariable)) {
			return context.js().variableDeclaration(true, scopeAccessorVariable, scopeAccessorValue);
		}

		return null;
	}

	private boolean findAccessor(JS body, String thisScopeAccessorVariable) {
		if (body instanceof Block) {
			String thisScopeIteratorReady = thisScopeAccessorVariable + ".";
			Iterator blockIterator = ((Block) body).iterator();
			while (blockIterator.hasNext()) {
				Node node = (Node) blockIterator.next();
				if (node instanceof AstNode && ((AstNode) node).toSource().contains(thisScopeIteratorReady)) {
					return true;
				}
			}
		}
		return false;
	}

	private JS addConstructorOuterClassAccessor(MethodTree tree, GenerationContext<JS> context) {
		Element element = TreeUtils.elementFromDeclaration(tree);
		int deepnessLevel = Scopes.getElementDeepnessLevel(element);

		// Create a target such as 'this._outerClass$x'
		JS innerClassConstructorParam = context.js().name(GeneratorConstants.INNER_CLASS_CONSTRUCTOR_PARAM_PREFIX
				+ GeneratorConstants.AUTO_GENERATED_ELEMENT_SEPARATOR + deepnessLevel);

		String scopeAccessorPrefix = Scopes.buildOuterClassAccessTargetPrefix();
		String scopeAccessorVariable = scopeAccessorPrefix + GeneratorConstants.AUTO_GENERATED_ELEMENT_SEPARATOR + deepnessLevel;

		return context.js().expressionStatement(
				context.js().assignment(AssignOperator.ASSIGN, context.js().name(scopeAccessorVariable), innerClassConstructorParam));
	}

	private JS addConstructorOrPrototypePrefixIfNeeded(MethodTree tree, GenerationContext<JS> context,
													   TreeWrapper<MethodTree, JS> tw, JS declaration) {
		if (JavaNodes.isConstructor(tree) || isMethodOfJavascriptFunction(context.getCurrentWrapper())) {
			return null;
		}

		String methodName = decorateMethodName(tree, context);
		if (tw.getEnclosingType().isGlobal()) {
			// var method=function() ...; //for global types
			return context.js().variableDeclaration(true, methodName, declaration);
		}
		JS member = context.js().property(getMemberTarget(tw), methodName);
		return context.js().expressionStatement(context.js().assignment(AssignOperator.ASSIGN, member, declaration));
	}
}
