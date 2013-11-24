package org.stjs.generator.writer.declaration;

import static org.stjs.generator.writer.JavaScriptNodes.assignment;
import static org.stjs.generator.writer.JavaScriptNodes.name;
import static org.stjs.generator.writer.JavaScriptNodes.statement;

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Modifier;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavascriptKeywords;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;

public class MethodWriter implements VisitorContributor<MethodTree, List<AstNode>, GenerationContext> {

	private String changeName(String name) {
		if (name.equals(GeneratorConstants.ARGUMENTS_PARAMETER)) {
			return "_" + name;
		}

		return name.toString();
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodTree tree, GenerationContext p,
			List<AstNode> prev) {
		if (tree.getModifiers().getFlags().contains(Modifier.NATIVE)) {
			// native methods are there only to indicate already existing javascript code - or to allow method
			// overloading
			return Collections.emptyList();
		}
		if (tree.getModifiers().getFlags().contains(Modifier.ABSTRACT)) {
			// abstract methods (from interfaces) are not generated
			return Collections.emptyList();
		}

		// no type appears for global scopes
		/*
		 * boolean global = isGlobal(type) && isStatic(modifiers); if (!anonymous) { if (!global) { if
		 * (isStatic(modifiers)) { printer.print("constructor."); } else { printer.print("prototype."); } }
		 * printer.print(name); printer.print(EQUALS); } printer.print("function"); if (isInnerClassConstructor) {
		 * printer.print(" "); printer.print(name); }
		 */

		FunctionNode decl = new FunctionNode();
		for (VariableTree param : tree.getParameters()) {
			if (GeneratorConstants.SPECIAL_THIS.equals(param.getName().toString())) {
				continue;
			}
			decl.addParam(name(changeName(param.getName().toString())));
		}
		decl.setBody(visitor.scan(tree.getBody(), p).get(0));

		// add the constructor.<name> or prototype.<name> if needed
		if (!JavaNodes.isConstructor(tree)) {
			AstNode target = name(tree.getModifiers().getFlags().contains(Modifier.STATIC) ? JavascriptKeywords.CONSTRUCTOR
					: JavascriptKeywords.PROTOTYPE);
			return Collections.<AstNode>singletonList(statement(assignment(target, tree.getName().toString(), decl)));
		}

		return Collections.<AstNode>singletonList(decl);
	}
}
