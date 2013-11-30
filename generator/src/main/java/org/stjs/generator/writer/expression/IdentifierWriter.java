package org.stjs.generator.writer.expression;

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.utils.Scopes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;

import com.sun.source.tree.IdentifierTree;

public class IdentifierWriter implements VisitorContributor<IdentifierTree, List<AstNode>, GenerationContext> {

	// private void visitField(FieldWrapper field, NameExpr n) {
	// if (Modifier.isStatic(field.getModifiers())) {
	// printStaticFieldOrMethodAccessPrefix(field.getOwnerType(), true);
	// } else if (!isInlineObjectCreationChild(n, INLINE_CREATION_PARENT_LEVEL)) {
	// printer.print("this.");
	// }
	// }

	private List<AstNode> visitField(Element def, IdentifierTree tree, GenerationContext context) {
		AstNode target = null;
		if (JavaNodes.isStatic(def)) {
			// TODO use type names here
			target = JavaScriptNodes.name(def.getEnclosingElement().getSimpleName());
		} else {
			target = JavaScriptNodes.keyword(Token.THIS);
		}
		return Collections.<AstNode>singletonList(JavaScriptNodes.property(target, tree.getName().toString()));
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, IdentifierTree tree,
			GenerationContext context, List<AstNode> prev) {

		if (GeneratorConstants.SPECIAL_THIS.equals(tree.getName().toString())) {
			return Collections.singletonList(JavaScriptNodes.keyword(Token.THIS));
		}
		Element def = Scopes.findElement(context.getTrees().getScope(context.getCurrentPath()), tree.getName().toString());
		assert def != null : "Cannot find the definition for variable " + tree.getName();

		// if (var == null) {
		// if (!(parent(n) instanceof SwitchEntryStmt)) {
		// TypeWrapper type = resolvedType(n);
		// if (type != null) {
		// printStaticFieldOrMethodAccessPrefix(type, false);
		// return;
		// }
		// }
		// } else {
		if (def.getKind() == ElementKind.FIELD) {
			return visitField(def, tree, context);
		}

		// assume variable
		return Collections.<AstNode>singletonList(JavaScriptNodes.name(tree.getName().toString()));
	}

}
