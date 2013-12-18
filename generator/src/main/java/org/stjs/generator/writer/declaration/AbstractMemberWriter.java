package org.stjs.generator.writer.declaration;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptKeywords;

import com.sun.source.tree.Tree;

public class AbstractMemberWriter<JS> {

	protected JS getMemberTarget(Tree tree, GenerationContext<JS> context) {
		Element element = JavaNodes.elementFromDeclaration(tree);
		if (JavaNodes.isGlobal(element.getEnclosingElement())) {
			return null;
		}
		return context.js().name(
				element.getModifiers().contains(Modifier.STATIC) ? JavascriptKeywords.CONSTRUCTOR : JavascriptKeywords.PROTOTYPE);
	}
}
