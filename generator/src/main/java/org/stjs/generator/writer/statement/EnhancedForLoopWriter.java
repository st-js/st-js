package org.stjs.generator.writer.statement;

import java.util.Collections;

import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.javascript.Array;

import com.sun.source.tree.EnhancedForLoopTree;

/**
 * generates from
 * 
 * <pre>
 * for (String x : list) {
 * }
 * </pre>
 * 
 * <pre>
 * for(var x in list) {
 * }
 * </pre>
 * 
 * Warning: the iteration is on indexes as in JavaScript, not on values as in Java!
 * @author acraciun
 */
public class EnhancedForLoopWriter<JS> implements WriterContributor<EnhancedForLoopTree, JS> {

	private JS generateArrayHasOwnProperty(EnhancedForLoopTree tree, GenerationContext<JS> context, JS iterated, JS body) {
		if (!context.getConfiguration().isGenerateArrayHasOwnProperty()) {
			return body;
		}

		TypeMirror iteratedType = InternalUtils.typeOf(tree.getExpression());
		if (!TypesUtils.isDeclaredOfName(iteratedType, Array.class.getName())) {
			return body;
		}
		JavaScriptBuilder<JS> js = context.js();

		// !(iterated).hasOwnProperty(tree.getVariable().getName())
		JS not =
				js.unary(
						UnaryOperator.LOGICAL_COMPLEMENT,
						js.functionCall(js.property(js.paren(iterated), "hasOwnProperty"),
								Collections.singleton(js.name(tree.getVariable().getName()))));

		JS ifs = js.ifStatement(not, js.continueStatement(null), null);
		return js.addStatementBeginning(body, ifs);
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, EnhancedForLoopTree tree, GenerationContext<JS> context) {
		JS iterator = visitor.scan(tree.getVariable(), context);
		JS iterated = visitor.scan(tree.getExpression(), context);
		JS body = visitor.scan(tree.getStatement(), context);

		body = generateArrayHasOwnProperty(tree, context, iterated, body);
		return context.withPosition(tree, context.js().forInLoop(iterator, iterated, body));
	}
}
