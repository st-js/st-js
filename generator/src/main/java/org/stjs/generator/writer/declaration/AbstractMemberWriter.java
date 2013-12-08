package org.stjs.generator.writer.declaration;

import static org.stjs.generator.writer.JavaScriptNodes.name;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptKeywords;

import com.sun.source.tree.Tree;

public class AbstractMemberWriter {

	protected AstNode getMemberTarget(Tree tree) {
		Element element = JavaNodes.elementFromDeclaration(tree);
		if (JavaNodes.isGlobal(element.getEnclosingElement())) {
			return null;
		}
		return name(element.getModifiers().contains(Modifier.STATIC) ? JavascriptKeywords.CONSTRUCTOR : JavascriptKeywords.PROTOTYPE);
	}
}
