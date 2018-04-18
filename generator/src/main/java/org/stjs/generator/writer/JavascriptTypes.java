package org.stjs.generator.writer;

import com.sun.source.tree.TypeParameterTree;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.javascript.NameValue;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.javascript.Map;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * (c) Swissquote 12.04.18
 *
 * @author sgoetz
 */
abstract public class JavascriptTypes<JS> {

	private JS mapPrimitiveType(TypeMirror type, GenerationContext<JS> context) {
		JavaScriptBuilder<JS> js = context.js();

		String qualifiedName = "";
		if (type instanceof DeclaredType) {
			qualifiedName = TypesUtils.getQualifiedName((DeclaredType) type).toString();
		}

		if (TypeKind.BOOLEAN.equals(type.getKind()) || "java.lang.Boolean".equals(qualifiedName)) {
			return js.name("boolean");
		}

		if (TypeKind.DOUBLE.equals(type.getKind()) || TypeKind.FLOAT.equals(type.getKind())
				|| TypeKind.INT.equals(type.getKind()) || TypeKind.LONG.equals(type.getKind())
				|| TypeKind.SHORT.equals(type.getKind()) || "java.lang.Short".equals(qualifiedName)
				|| "java.lang.Integer".equals(qualifiedName) || "java.lang.Long".equals(qualifiedName)
				|| "java.lang.Double".equals(qualifiedName) || "java.lang.Float".equals(qualifiedName)) {
			return js.name("number");
		}

		if ("java.lang.String".equals(qualifiedName)) {
			return js.name("string");
		}

		/*
		qualifiedName.equals("java.lang.Byte")
				|| qualifiedName.equals("java.lang.Character")
		case BYTE:
		case CHAR:
			   */

		// TODO :: make sure that's what we want
		return js.name("any");
	}

	private JS getParametrized(DeclaredType declaredType, GenerationContext<JS> context, JS typeName) {
		JavaScriptBuilder<JS> js = context.js();
		List<JS> array = new ArrayList<>();
		for (TypeMirror arg : declaredType.getTypeArguments()) {
			array.add(getTypeDesc(arg, context, true));
		}

		String qualifiedName = TypesUtils.getQualifiedName(declaredType).toString();

		// Render maps
		if (qualifiedName.equals(Map.class.getCanonicalName())) {
			return js.object(Arrays.asList(
					NameValue.of("[key: string]", array.get(1))
			));
		}

		return js.genericType(typeName, array);
	}

	public List<JS> getTypeParams(List<? extends TypeParameterTree> treeParams, GenerationContext<JS> context) {
		List<JS> params = new ArrayList<>();

		if (treeParams.size() == 0) {
			return null;
		}

		for (TypeParameterTree param : treeParams) {
			params.add(getGenericTypeDesc(InternalUtils.symbol(param).asType(), context));
		}
		return params;
	}

	private JS getTypeVariable(TypeVariable variableType, GenerationContext<JS> context, JS typeName) {
		JS upper = getFieldTypeDesc(variableType.getUpperBound(), context);
		JS lower = getFieldTypeDesc(variableType.getLowerBound(), context);

		if (lower instanceof KeywordLiteral && ((KeywordLiteral) lower).getType() == Token.NULL) {
			lower = null;
		}

		if (upper instanceof KeywordLiteral && ((KeywordLiteral) upper).getType() == Token.NULL) {
			upper = null;
		}

		return context.js().variableType(typeName, upper, lower);
	}

	private JS getWildcardVariable(WildcardType type, GenerationContext<JS> context) {
		return context.js().name("any");

		//TypeMirror upperType = type.getExtendsBound();
		//TypeMirror lowerType = type.getSuperBound();

		//JS upper = upperType == null ? null : getFieldTypeDesc(upperType, context);
		//JS lower = lowerType == null ? null : getFieldTypeDesc(lowerType, context);

		//return context.js().variableType(context.js().name("?"), upper, lower);
	}

	public JS getTypeDesc(TypeMirror type, GenerationContext<JS> context, Boolean genericDefinition) {
		JavaScriptBuilder<JS> js = context.js();
		if (JavaNodes.isJavaScriptPrimitive(type)) {
			return mapPrimitiveType(type, context);
		}
		String typeNameString = context.getNames().getTypeName(context, type, DependencyType.OTHER);
		JS typeName = js.name(typeNameString);

		if (type instanceof DeclaredType) {
			DeclaredType declaredType = (DeclaredType) type;

			String qualifiedName = TypesUtils.getQualifiedName((DeclaredType) type).toString();

			if ("java.lang.Object".equals(qualifiedName)) {
				return js.name("any");
			}

			// parametrized type
			if (!declaredType.getTypeArguments().isEmpty()) {
				return getParametrized((DeclaredType) type, context, typeName);
			}

			// If this is an array, it must at least be parametrized as any
			if ("org.stjs.javascript.Array".equals(qualifiedName) && typeNameString.equals("Array")) {
				List<JS> array = new ArrayList<>();
				array.add(js.name("any"));
				return js.genericType(typeName, array);
			}

		} else if (type instanceof WildcardType) {
			if (genericDefinition) {
				return getWildcardVariable((WildcardType) type, context);
			}
		} else if (type instanceof TypeVariable) {
			if (genericDefinition) {
				return getTypeVariable((TypeVariable) type, context, typeName);
			}
		} else if (type instanceof ArrayType) {
			if (typeNameString.equals("Array")) {
				List<JS> types = new ArrayList<>();
				types.add(getFieldTypeDesc(((ArrayType) type).getComponentType(), context));

				return js.genericType(typeName, types);
			}
		} else if (type instanceof NullType) {
			return js.keyword(Keyword.NULL);
		} else {
			System.out.println("What is this ? " + type.getClass());
		}

		return typeName;
	}

	public JS getFieldTypeDesc(TypeMirror type, GenerationContext<JS> context) {
		return getTypeDesc(type, context, false);
	}

	public JS getGenericTypeDesc(TypeMirror type, GenerationContext<JS> context) {
		return getTypeDesc(type, context, true);
	}
}
