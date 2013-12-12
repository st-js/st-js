package org.stjs.generator.writer.templates;

import japa.parser.ast.expr.MethodCallExpr;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;
import org.stjs.generator.writer.JavascriptWriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;
import org.stjs.generator.writer.template.TemplateUtils;

import com.sun.source.tree.MethodInvocationTree;

/**
 * This template generate a code that allows you to add into the javascript file the original java code and its position
 * in the source file. It's no longer very useful since the javaScript stacktrace can be translated back to Java
 * stacktrace.
 * 
 * assertMethod(params) -> assertMethod("sourceFile:line", "assertMethod(params)", params);
 * 
 * @author acraciun
 */
public class AssertTemplate implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		TemplateUtils.printScope(currentHandler, n, context, false);
		currentHandler.getPrinter().print(n.getName());
		String location = "\"" + context.getInputFile().getName() + ":" + n.getBeginLine() + "\"";
		String params = "\"" + n.toString().replace("\"", "\\\"") + "\"";
		currentHandler.printArguments(Arrays.asList(location, params), n.getArgs(), Collections.<String>emptyList(), context);
		return true;
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {
		int argCount = tree.getArguments().size();
		if (argCount < 1) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"An 'adapter' template can only be applied for methods with at least 1 parameter");
		}
		String name = MethodInvocationWriter.buildMethodName(tree);
		AstNode target = MethodInvocationWriter.buildTarget(visitor, tree, context);
		List<AstNode> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);
		arguments.add(0, JavaScriptNodes.string(context.getInputFile().getName() + ":" + context.getStartLine(tree)));
		arguments.add(1, JavaScriptNodes.string(tree.toString()));

		return Collections.<AstNode>singletonList(JavaScriptNodes.functionCall(target, name, arguments));
	}
}
