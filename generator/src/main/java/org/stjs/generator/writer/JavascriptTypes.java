package org.stjs.generator.writer;

import com.sun.source.tree.ClassTree;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.javascript.NameValue;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.ServerSide;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
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
			array.add(getFieldTypeDesc(arg, context));
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

	public JS getFieldTypeDesc(TypeMirror type, GenerationContext<JS> context) {
		JavaScriptBuilder<JS> js = context.js();
		if (JavaNodes.isJavaScriptPrimitive(type)) {
			return mapPrimitiveType(type, context);
		}
		JS typeName = js.name(context.getNames().getTypeName(context, type, DependencyType.OTHER));

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
		} else if (type instanceof WildcardType) {
			// TODO :: support ? extends T or similar annotations
			return js.name("_any");
			return js.name("_any");
		} else if (type instanceof ArrayType) {
			List<JS> types = new ArrayList<>();
			types.add(getFieldTypeDesc(((ArrayType) type).getComponentType(), context));
			return js.genericType(typeName, types);
		} else if (type instanceof NullType) {
			return js.keyword(Keyword.NULL);
		} else {
			System.out.println("What is this ? " + type.getClass());
		}

		return typeName;
	}

	@SuppressWarnings("unused")
	private JS getTypeDescription(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context) {
		// if (isGlobal(type)) {
		// printer.print(JavascriptKeywords.NULL);
		// return;
		// }

		TypeElement type = TreeUtils.elementFromDeclaration(tree);

		List<NameValue<JS>> props = new ArrayList<>();
		for (Element member : ElementUtils.getAllFieldsIn(type)) {
			TypeMirror memberType = ElementUtils.getType(member);
			if (JavaNodes.isJavaScriptPrimitive(memberType)) {
				continue;
			}
			if (member.getKind() == ElementKind.ENUM_CONSTANT) {
				continue;
			}
			if (memberType instanceof TypeVariable) {
				// what to do with fields of generic parameters !?
				continue;
			}
			if (!skipTypeDescForField(member)) {
				props.add(NameValue.of(member.getSimpleName(), getFieldTypeDesc(memberType, context)));
			}
		}
		return context.js().object(props);
	}

	private boolean skipTypeDescForField(Element member) {
		if (((TypeElement) member.getEnclosingElement()).getQualifiedName().toString().startsWith("java.lang.")) {
			// maybe we should rather skip the bridge classes here
			return true;
		}
		if (member.getAnnotation(ServerSide.class) != null) {
			return true;
		}
		return false;
	}
}
