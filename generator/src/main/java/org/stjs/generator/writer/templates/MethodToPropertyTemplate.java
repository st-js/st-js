package org.stjs.generator.writer.templates;

import static org.stjs.generator.writer.JavaScriptNodes.property;

import java.util.Collections;
import java.util.List;

import javacutils.TreeUtils;

import javax.lang.model.element.ExecutableElement;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $method() => $method and <br>
 * $method(x) => $method = x and $staticMethod(x) => x.$method and <br>
 * $staticMethod(x, y) => x.$method = y
 * @author acraciun
 */
public class MethodToPropertyTemplate implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {
		int argCount = tree.getArguments().size();
		if (argCount > 2) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"A 'toProperty' template can only be applied for methods with 0 or 1 parameters");
		}

		AstNode target = null;
		int arg = 0;

		//TARGET
		ExecutableElement methodElement = TreeUtils.elementFromUse(tree);
		if (JavaNodes.isStatic(methodElement)) {
			//$staticMethod(x) or $staticMethod(x,y)
			target = JavaScriptNodes.paren(visitor.scan(tree.getArguments().get(arg++), context).get(0));
		} else {
			//$method() or $method(x)
			target = MethodInvocationWriter.buildTarget(visitor, tree, context);
		}

		//NAME
		String name = MethodInvocationWriter.buildMethodName(tree);
		int start = name.startsWith("$") ? 1 : 0;
		AstNode property = property(target, name.substring(start));

		//VALUE
		if (argCount == arg) {
			// $staticMethod(x) or $method()
			return Collections.<AstNode> singletonList(property);
		}

		//$staticMethod(x,y) or $method(x)
		return Collections.<AstNode> singletonList(JavaScriptNodes.assignment(property, visitor.scan(tree.getArguments().get(arg++), context)
				.get(0)));
	}
}
