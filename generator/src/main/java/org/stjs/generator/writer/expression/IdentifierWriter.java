package org.stjs.generator.writer.expression;


import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.IdentifierTree;

/**
 * this class deal with identifiers likes variable, field references.
 * @author acraciun
 */
public class IdentifierWriter<JS> implements WriterContributor<IdentifierTree, JS> {

	// private void visitField(FieldWrapper field, NameExpr n) {
	// if (Modifier.isStatic(field.getModifiers())) {
	// printStaticFieldOrMethodAccessPrefix(field.getOwnerType(), true);
	// } else if (!isInlineObjectCreationChild(n, INLINE_CREATION_PARENT_LEVEL)) {
	// printer.print("this.");
	// }
	// }

	private JS visitField(Element def, IdentifierTree tree, GenerationContext<JS> context) {
		return context.js().property(MemberWriters.buildTarget(context, def), tree.getName());
	}

	private JS visitEnumConstant(Element def, IdentifierTree tree, GenerationContext<JS> context) {
		JS target = context.js().name(context.getNames().getTypeName(context, def.getEnclosingElement()));
		return context.js().property(target, tree.getName());
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, IdentifierTree tree, GenerationContext<JS> context) {

		String name = tree.getName().toString();

		if (GeneratorConstants.SPECIAL_THIS.equals(name) || GeneratorConstants.THIS.equals(name)) {
			return context.js().keyword(Keyword.THIS);
		}
		Element def = TreeUtils.elementFromUse(tree);
		assert def != null : "Cannot find the definition for variable " + tree.getName();

		if (def.getKind() == ElementKind.PACKAGE) {
			return null;
		}
		if (def.getKind() == ElementKind.FIELD) {
			return visitField(def, tree, context);
		}
		if (def.getKind() == ElementKind.ENUM_CONSTANT) {
			return visitEnumConstant(def, tree, context);
		}
		if (def.getKind() == ElementKind.CLASS || def.getKind() == ElementKind.ENUM || def.getKind() == ElementKind.INTERFACE) {
			if (JavaNodes.isGlobal(def)) {
				return null;
			}
			name = context.getNames().getTypeName(context, def);
		}

		// assume variable
		return context.js().name(name);
	}

}
