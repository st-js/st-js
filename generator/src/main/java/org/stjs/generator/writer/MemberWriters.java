package org.stjs.generator.writer;

import javax.lang.model.element.Element;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;

public class MemberWriters {
	public static AstNode buildTarget(GenerationContext context, Element memberDecl) {
		if (JavaNodes.isGlobal(memberDecl.getEnclosingElement())) {
			return null;
		}
		if (JavaNodes.isStatic(memberDecl)) {
			return JavaScriptNodes.name(context.getNames().getTypeName(context, memberDecl.getEnclosingElement()));
		}
		return JavaScriptNodes.THIS();
	}
}
