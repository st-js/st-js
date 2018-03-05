package org.stjs.generator.writer.statement;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.BinaryOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.NameValue;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

import com.sun.source.tree.EnhancedForLoopTree;

import java.util.Arrays;
import java.util.Collections;

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
			// only Arrays need hasOwnProperty. Maps are just plain JS objects and don't have any other enumerable properties.
			return body;
		}
		JavaScriptBuilder<JS> js = context.js();

		// !(iterated).hasOwnProperty(tree.getVariable().getName())
		JS not =
				js.unary(
						UnaryOperator.LOGICAL_COMPLEMENT,
						js.functionCall(
								js.property(js.paren(iterated), "hasOwnProperty"),
								Collections.singleton(js.name(tree.getVariable().getName()))));

		JS ifs = js.ifStatement(not, js.continueStatement(null), null);
		return js.addStatementBeginning(body, ifs);
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, EnhancedForLoopTree tree, GenerationContext<JS> context) {
		// Java Statement
		//     for (String s : myCollection)
		// Scanned values
		//     iterator = (VariableDeclaration) --> "String s"
		//     iterated = (Name) --> "myCollection"
		JS iterator = visitor.scan(tree.getVariable(), context);
		JS iterated = visitor.scan(tree.getExpression(), context);
		JS body = visitor.scan(tree.getStatement(), context);

		TypeMirror iteratedType = InternalUtils.typeOf(tree.getExpression());
		if (TypesUtils.isDeclaredOfName(iteratedType, Array.class.getName())
				|| TypesUtils.isDeclaredOfName(iteratedType, Map.class.getName())) {
			return generateForEachInObject(tree, context, iterator, iterated, body);

		} else if (isErasuredClassAssignableFromType(Iterable.class, iteratedType, context)) {
			return generateForEachWithIterable(tree, context, iterated, body);
		} else if (TypeKind.ARRAY == iteratedType.getKind()) {
			return generateForWithIndex(tree, context, iterated, body);
		} else {
			return context.withPosition(tree, context.js().forInLoop(iterator, iterated, body));
		}
	}

	private boolean isErasuredClassAssignableFromType(Class clazz, TypeMirror iteratedType, GenerationContext<JS> context) {
		TypeMirror erasedClassToCheck = context.getTypes().erasure(TypesUtils.typeFromClass(context.getTypes(), context.getElements(), clazz));
		TypeMirror erasedIteratedType = context.getTypes().erasure(iteratedType);

		return context.getTypes().isAssignable(erasedIteratedType, erasedClassToCheck);
	}

	private JS generateForEachInObject(EnhancedForLoopTree tree, GenerationContext<JS> context, JS iterator, JS iterated, JS body) {
		JS newBody = generateArrayHasOwnProperty(tree, context, iterated, body);
		return context.withPosition(tree, context.js().forInLoop(iterator, iterated, newBody));
	}

	private JS generateForEachWithIterable(EnhancedForLoopTree tree, GenerationContext<JS> context, JS iterated, JS body) {
		JavaScriptBuilder<JS> js = context.js();

		// Java source code:
		// ---------------------------------------------
		//	 for (String oneOfTheString : myStringList) {
		//	   // do whatever you want with 'oneOfTheString'
		//	 }
		//
		// Translated Javascript:
		// ---------------------------------------------
		//   for (var iterator$oneOfTheString = myStringList.iterator(); iterator$oneOfTheString.hasNext(); ) {
		//     var oneOfTheString = iterator$oneOfTheString.next();
		//   }
		String initialForLoopVariableName = tree.getVariable().getName().toString();

		JS iteratorMethodCall = js.functionCall(
				js.property(iterated, "iterator"),
				Collections.<JS>emptyList());

		String newIteratorName = "iterator$" + initialForLoopVariableName;
		JS forLoopIterator = js.name(newIteratorName);
		JS init = js.variableDeclaration(false, newIteratorName, iteratorMethodCall);
		JS condition = js.functionCall(js.property(forLoopIterator, "hasNext"), Collections.<JS>emptyList());
		JS update = js.emptyExpression();

		JS iteratorNextStatement = js.variableDeclaration(true, initialForLoopVariableName,
			js.functionCall(js.property(forLoopIterator, "next"), Collections.<JS>emptyList()));
		JS newBody = js.addStatementBeginning(body, iteratorNextStatement);

		return context.withPosition(tree, context.js().forLoop(init, condition, update, newBody));
	}
	
	private JS generateForWithIndex(EnhancedForLoopTree tree, GenerationContext<JS> context, JS iterated, JS body) {
		JavaScriptBuilder<JS> js = context.js();

		// Java source code:
		// ---------------------------------------------
		//   String[] myStringArray = ...
		//	 for (String str : myStringArray) {
		//	   // do whatever you want with 'str'
		//	 }
		//
		// Translated Javascript:
		// ---------------------------------------------
		//   for (var index$str = 0, arr$str = myStringArray; index$str < arr$str.length; index$str++) {
		//     var str = arr$str[index$str];
		//	   // do whatever you want with 'str'
		//   }
		String initialForLoopVariableName = tree.getVariable().getName().toString();

		JS iteratorMethodCall = js.number(0);

		String newIteratorName = "index$" + initialForLoopVariableName;
		String newArrayName = "arr$" + initialForLoopVariableName;
		JS newArray = js.name(newArrayName);
		JS index = js.name(newIteratorName);
		JS init = js.variableDeclaration(false, Arrays.asList(NameValue.of(newIteratorName, iteratorMethodCall), NameValue.of(newArrayName, iterated)));
		JS condition = js.binary(BinaryOperator.LESS_THAN, Arrays.asList(index, js.property(newArray, "length")));
		JS update = js.unary(UnaryOperator.POSTFIX_INCREMENT, index);

		JS iteratorNextStatement = js.variableDeclaration(true, initialForLoopVariableName, js.elementGet(newArray, index));
		JS newBody = js.addStatementBeginning(body, iteratorNextStatement);

		return context.withPosition(tree, context.js().forLoop(init, condition, update, newBody));
	}

}