package org.stjs.generator.writer.expression;

import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.InstanceOfTree;
import com.sun.source.util.TreePath;

public class InstanceofWriter<JS> implements WriterContributor<InstanceOfTree, JS> {

	@SuppressWarnings("unchecked")
	@Override
	public JS visit(WriterVisitor<JS> visitor, InstanceOfTree tree, GenerationContext<JS> context) {
		TypeMirror type = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getType()));
		JS leftSide = visitor.scan(tree.getExpression(), context);
		JS typeName = context.js().name(context.getNames().getTypeName(context, type, DependencyType.STATIC));
		return context.js().assignment(AssignOperator.INSTANCE_OF, leftSide, typeName);
	}
}
