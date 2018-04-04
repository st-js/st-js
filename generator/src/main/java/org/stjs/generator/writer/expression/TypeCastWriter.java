package org.stjs.generator.writer.expression;

import static java.util.Arrays.asList;
import static javax.lang.model.type.TypeKind.BYTE;
import static javax.lang.model.type.TypeKind.CHAR;
import static javax.lang.model.type.TypeKind.INT;
import static javax.lang.model.type.TypeKind.LONG;
import static javax.lang.model.type.TypeKind.SHORT;
import static org.stjs.generator.javascript.BinaryOperator.LEFT_SHIFT;
import static org.stjs.generator.javascript.BinaryOperator.RIGHT_SHIFT;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.EnumSet;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.BinaryOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Type.JCPrimitiveType;

public class TypeCastWriter<JS> implements WriterContributor<TypeCastTree, JS> {

	private static final int CHAR_MASK = 0xffff;
	private static final int BYTE_SHIFT = 24;
	private static final int SHORT_SHIFT = 16;

	private static final EnumSet<TypeKind> NEED_CAST_TO_INT = EnumSet.of(LONG);
	private static final EnumSet<TypeKind> NEED_CAST_TO_CHAR = EnumSet.of(LONG, INT, SHORT, BYTE);
	private static final EnumSet<TypeKind> NEED_CAST_TO_SHORT = EnumSet.of(LONG, INT, CHAR);
	private static final EnumSet<TypeKind> NEED_CAST_TO_BYTE = EnumSet.of(LONG, INT, SHORT, CHAR);

	@Override
	public JS visit(WriterVisitor<JS> visitor, TypeCastTree tree, GenerationContext<JS> context) {
		TypeMirror type = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getType()));
		JS expr = visitor.scan(tree.getExpression(), context);
		ExpressionTree expression = tree.getExpression();
		TypeKind fromKind = getKind(expression);
		TypeKind toKind = type.getKind();
		JavaScriptBuilder<JS> b = context.js();

		if (needCastToInt(fromKind, toKind)) {
			// long l = 8*1024*1024*1024;
			// int a = (int) l;
			// var a = ((i)|0);

			JS or = b.binary(BinaryOperator.OR, asList(b.paren(expr), b.number(0)));
			return b.paren(or);
		}

		if (needCastToByte(fromKind, toKind)) {
			// long l = 8*1024*1024*1024;
			// byte a = (byte) l;
			// var a = (l<< 24 >> 24);

			JS lsh = b.binary(LEFT_SHIFT, asList(expr, b.number(BYTE_SHIFT)));
			JS rsh = b.binary(RIGHT_SHIFT, asList(lsh, b.number(BYTE_SHIFT)));
			return b.paren(rsh);
		}

		if (needCastToShort(fromKind, toKind)) {
			// int i = 2*1024*1024*1024; //MAX_VALUE
			// short a = (short) i;
			// var a = ((i)<<16>>16);

			JS lsh = b.binary(LEFT_SHIFT, asList(b.paren(expr), b.number(SHORT_SHIFT)));
			JS rsh = b.binary(RIGHT_SHIFT, asList(lsh, b.number(SHORT_SHIFT)));
			return b.paren(rsh);
		}

		if (needCastToChar(fromKind, toKind)) {
			// int i = 2*1024*1024*1024; //MAX_VALUE
			// char a = (char) i;
			// var a = ((i)&0xffff);

			JS and = b.binary(BinaryOperator.AND, asList(b.paren(expr), b.number(CHAR_MASK)));
			return b.paren(and);
		}

		if (TypesUtils.isIntegral(type)) {
			// add explicit cast in this case
			JS target = b.property(b.name(GeneratorConstants.STJS), "trunc");
			expr = b.functionCall(target, Collections.singleton(expr));
		}
		// otherwise skip to cast type - continue with the expression
		return expr;
	}

	private boolean needCastToChar(TypeKind fromKind, TypeKind toKind) {
		return CHAR.equals(toKind) && NEED_CAST_TO_CHAR.contains(fromKind);
	}

	private boolean needCastToShort(TypeKind fromKind, TypeKind toKind) {
		return SHORT.equals(toKind) && NEED_CAST_TO_SHORT.contains(fromKind);
	}

	private boolean needCastToByte(TypeKind fromKind, TypeKind toKind) {
		return BYTE.equals(toKind) && NEED_CAST_TO_BYTE.contains(fromKind);
	}

	private boolean needCastToInt(TypeKind fromKind, TypeKind toKind) {
		return INT.equals(toKind) && NEED_CAST_TO_INT.contains(fromKind);
	}

	private TypeKind getKind(ExpressionTree expression) {
		try {
			Field field = expression.getClass().getField("type");
			Object object = field.get(expression);
			if (object instanceof JCPrimitiveType) {
				JCPrimitiveType p = (JCPrimitiveType) object;
				return p.getKind();
			}
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
		return null;
	}
}
