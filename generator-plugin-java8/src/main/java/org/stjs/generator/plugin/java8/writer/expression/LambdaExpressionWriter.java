package org.stjs.generator.plugin.java8.writer.expression;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LambdaExpressionTree.BodyKind;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.util.TreeScanner;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.utils.Scopes;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.declaration.MethodWriter;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * generates the code for Lambda expressions.
 *
 * <pre>
 * (x, y) -&gt; x + y
 * </pre>
 *
 * gives
 *
 * <pre>
 * function (x,y) {
 * 	return x + y;
 * }
 * </pre>
 * @author acraciun
 * @param <JS>
 */
public class LambdaExpressionWriter<JS> implements WriterContributor<LambdaExpressionTree, JS> {

	private boolean accessOuterScope(LambdaExpressionTree lambda, GenerationContext<JS> context) {
		AtomicBoolean outerScopeAccess = new AtomicBoolean(false);

		lambda.accept(new TreeScanner<Void, Void>() {
			private boolean checkStopped;

			@Override
			public Void visitIdentifier(IdentifierTree tree, Void arg1) {
				if (checkStopped) {
					return super.visitIdentifier(tree, arg1);
				}
				Element fieldElement = TreeUtils.elementFromUse(tree);
				if (Scopes.isRegularInstanceField(fieldElement, tree)
						|| GeneratorConstants.THIS.equals(tree.getName().toString())) {
					outerScopeAccess.set(true);
				}
				return super.visitIdentifier(tree, arg1);
			}

			@Override
			public Void visitClass(ClassTree arg0, Void arg1) {
				// stop the checks if a new type is encountered
				checkStopped = true;
				return super.visitClass(arg0, arg1);
			}

			@Override
			public Void visitMethodInvocation(MethodInvocationTree tree, Void arg1) {
				if (checkStopped) {
					return super.visitMethodInvocation(tree, arg1);
				}
				Element methodElement = TreeUtils.elementFromUse(tree);
				if (JavaNodes.isStatic(methodElement)) {
					// only instance methods
					return super.visitMethodInvocation(tree, arg1);
				}
				String name = context.getNames().getMethodName(context, tree);

				if (GeneratorConstants.THIS.equals(name) || GeneratorConstants.SUPER.equals(name)) {
					// this and super call are ok
					return super.visitMethodInvocation(tree, arg1);
				}

				if (!(tree.getMethodSelect() instanceof IdentifierTree)) {
					// check for Outer.this check
					return super.visitMethodInvocation(tree, arg1);
				}
				outerScopeAccess.set(true);
				return super.visitMethodInvocation(tree, arg1);
			}
		}, null);

		return outerScopeAccess.get();
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, LambdaExpressionTree tree, GenerationContext<JS> context) {
		List<JS> params = MethodWriter.getParams(tree.getParameters(), context);
		JavaScriptBuilder<JS> js = context.js();
		JS body = visitor.scan(tree.getBody(), context);

		if (tree.getBodyKind() == BodyKind.EXPRESSION) {
			body = js.returnStatement(body);
		}

		if (!(tree.getBody() instanceof BlockTree)) {
			body = js.block(Collections.singleton(body));
		}
		JS lambdaFunc = js.function(null, params, body);
		int specialThisParamPos = MethodWriter.getTHISParamPos(tree.getParameters());

		if (accessOuterScope(tree, context) || specialThisParamPos >= 0) {
			// bind for lamdas accessing the outher scope
			JS target = js.keyword(Keyword.THIS);
			JS stjsBind = js.property(context.js().name("stjs"), "bind");
			if (specialThisParamPos < 0) {
				return js.functionCall(stjsBind, Arrays.asList(target, lambdaFunc));
			}
			return js.functionCall(stjsBind, Arrays.asList(target, lambdaFunc, js.number(specialThisParamPos)));
		}
		return lambdaFunc;
	}
}
