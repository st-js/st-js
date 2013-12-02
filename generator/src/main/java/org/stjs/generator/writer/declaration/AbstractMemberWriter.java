package org.stjs.generator.writer.declaration;

import static org.stjs.generator.writer.JavaScriptNodes.name;
import javacutils.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.JavascriptClassGenerationException;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptKeywords;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

public class AbstractMemberWriter {
	protected Element elementFromDeclaration(Tree tree) {
		if (tree instanceof MethodTree) {
			return TreeUtils.elementFromDeclaration((MethodTree) tree);
		}
		if (tree instanceof VariableTree) {
			return TreeUtils.elementFromDeclaration((VariableTree) tree);
		}
		if (tree instanceof ClassTree) {
			return TreeUtils.elementFromDeclaration((ClassTree) tree);
		}
		throw new JavascriptClassGenerationException("none", "Unexpected node type:" + tree.getClass() + "," + tree.getKind());
	}

	protected AstNode getMemberTarget(Tree tree) {
		Element element = elementFromDeclaration(tree);
		if (JavaNodes.isGlobal(element.getEnclosingElement())) {
			return null;
		}
		return name(element.getModifiers().contains(Modifier.STATIC) ? JavascriptKeywords.CONSTRUCTOR : JavascriptKeywords.PROTOTYPE);
	}
}
