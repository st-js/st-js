package org.stjs.generator.writer.declaration;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.STJSRuntimeException;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.google.common.base.Defaults;
import com.google.common.primitives.Primitives;
import com.sun.source.tree.VariableTree;

/**
 * This will add the declaration of a field. This contributor is not added directly, but redirect from
 * {@link org.stjs.generator.writer.statement.VariableWriter}
 * 
 * @author acraciun
 */
public class FieldWriter<JS> extends AbstractMemberWriter<JS> implements WriterContributor<VariableTree, JS> {
	private static final Map<String, Class<?>> PRIMITIVE_CLASSES = new HashMap<String, Class<?>>();
	static {
		for (Class<?> cls : Primitives.allPrimitiveTypes()) {
			PRIMITIVE_CLASSES.put(cls.getName(), cls);
		}
	}

	private Class<?> primitiveClass(TypeMirror type) {
		Class<?> cls = PRIMITIVE_CLASSES.get(type.toString());
		if (cls == null) {
			throw new STJSRuntimeException("Cannot load class " + type);
		}
		return cls;
	}

	private JS getPrimitiveDefaultValue(Element element, GenerationContext<JS> context) {
		Object defaultValue = Defaults.defaultValue(primitiveClass(element.asType()));
		if (defaultValue instanceof Number) {
			return context.js().number((Number) defaultValue);
		}
		if (defaultValue instanceof Character) {
			return context.js().character(defaultValue.toString());
		}
		if (defaultValue instanceof Boolean) {
			return context.js().keyword(Boolean.TRUE.equals(defaultValue) ? Keyword.TRUE : Keyword.FALSE);
		}
		return context.js().keyword(Keyword.NULL);
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, VariableTree tree, GenerationContext<JS> context) {
		JS initializer = null;
		if (tree.getInitializer() == null) {
			Element element = TreeUtils.elementFromDeclaration(tree);
			if (TypesUtils.isPrimitive(element.asType())) {
				initializer = getPrimitiveDefaultValue(element, context);
			} else {
				initializer = context.js().keyword(Keyword.NULL);
			}
		} else {
			initializer = visitor.scan(tree.getInitializer(), context);
		}

		String fieldName = tree.getName().toString();
		JS member = context.js().property(getMemberTarget(tree, context), fieldName);
		return context.js().expressionStatement(context.js().assignment(AssignOperator.ASSIGN, member, initializer));
	}
}
