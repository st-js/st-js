package org.stjs.generator.writer.templates;

import java.util.Collections;
import java.util.List;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;

/**
 * this is the standard generation template
 * @author acraciun
 */
public class DefaultTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {
	/**
	 * super(args) -> SuperType.call(this, args)
	 */
	private List<AstNode> callToSuperConstructor(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor,
			MethodInvocationTree tree, GenerationContext context) {
		if (!TreeUtils.isSuperCall(tree)) {
			return null;
		}

		Element methodElement = TreeUtils.elementFromUse(tree);
		if (JavaNodes.isStatic(methodElement)) {
			// this is a call of type super.staticMethod(args) -> it should be handled as a simple call to staticMethod
			return null;
		}

		TypeElement typeElement = (TypeElement) methodElement.getEnclosingElement();

		String methodName;
		if (tree.getMethodSelect() instanceof IdentifierTree) {
			methodName = ((IdentifierTree) tree.getMethodSelect()).getName().toString();
		} else {
			methodName = ((MemberSelectTree) tree.getMethodSelect()).getIdentifier().toString();
		}

		// avoid useless call to super() when the super class is Object
		if (GeneratorConstants.SUPER.equals(methodName) && JavaNodes.sameRawType(typeElement.asType(), Object.class)) {
			return Collections.emptyList();
		}

		// transform it into superType.[prototype.method].call(this, args..);
		String typeName = context.getNames().getTypeName(context, typeElement);
		AstNode superType = JavaScriptNodes.name(GeneratorConstants.SUPER.equals(methodName) ? typeName : typeName + ".prototype." + methodName);

		List<AstNode> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);
		arguments.add(0, JavaScriptNodes.keyword(Token.THIS));
		return Collections.<AstNode> singletonList(JavaScriptNodes.functionCall(superType, "call", arguments));
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		List<AstNode> js = null;

		js = callToSuperConstructor(visitor, tree, context);
		if (js != null) {
			return js;
		}

		AstNode target = MethodInvocationWriter.buildTarget(visitor, tree, context);
		String name = MethodInvocationWriter.buildMethodName(tree);
		List<AstNode> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);
		return Collections.<AstNode> singletonList(JavaScriptNodes.functionCall(target, name, arguments));
	}
}
