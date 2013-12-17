package org.stjs.generator.writer.templates;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * x.$invoke(a,b) -> x(a,b)
 * @author acraciun
 */
public class InvokeTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		AstNode target = MethodInvocationWriter.buildTarget(visitor, tree, context);
		List<AstNode> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);

		FunctionCall fc = new FunctionCall();
		fc.setTarget(target);
		if (arguments.size() > 0) {
			fc.setArguments(arguments);
		}

		return Collections.<AstNode> singletonList(fc);
	}
}
