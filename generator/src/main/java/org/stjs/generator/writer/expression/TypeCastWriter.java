package org.stjs.generator.writer.expression;

import java.util.Collections;

import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.TypeCastTree;
import com.sun.source.util.TreePath;

public class TypeCastWriter<JS> implements WriterContributor<TypeCastTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, TypeCastTree tree, GenerationContext<JS> context) {
		TypeMirror type = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getType()));
		JS expr = visitor.scan(tree.getExpression(), context);
		if (TypesUtils.isIntegral(type)) {
			// add explicit cast in this case
			JS target = context.js().property(context.js().name("stjs"), "trunc");
			expr = context.js().functionCall(target, Collections.singleton(expr));
		}
		// otherwise skip to cast type - continue with the expression
		return expr;
	}
}
