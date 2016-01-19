package org.stjs.generator.writer.expression;

import java.util.Arrays;

import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.InstanceOfTree;
import com.sun.source.util.TreePath;

public class InstanceofWriter<JS> implements WriterContributor<InstanceOfTree, JS> {

	@SuppressWarnings("unchecked")
	@Override
	public JS visit(WriterVisitor<JS> visitor, InstanceOfTree tree, GenerationContext<JS> context) {

		// build stjs.isInstanceOf(expr.constructor, type);
		// TODO do I need a check or parenthesis around !?

		TypeMirror type = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getType()));
		JS thObject = visitor.scan(tree.getExpression(), context);
		JS targetInst = context.js().property(context.js().name("stjs"), "isInstanceOf");
		JS typeName = context.js().name(context.getNames().getTypeName(context, type, DependencyType.STATIC));
		return context.js().functionCall(targetInst, Arrays.asList(thObject, typeName));
	}
}
