package org.stjs.generator.writer.statement;

import java.util.Collections;

import javacutils.TreeUtils;
import javacutils.TypesUtils;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
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
		// TODO check
		// TypeWrapper iterated = resolvedType(n.getIterable());
		//
		// if (!iterated.isAssignableFrom(TypeWrappers.wrap(Array.class))) {
		// return;
		// }

		Element iteratedElement = TreeUtils.elementFromUse(tree.getExpression());
		if (!TypesUtils.isDeclaredOfName(iteratedElement.asType(), Array.class.getName())) {
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
		return js.addStatement(body, ifs);
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
