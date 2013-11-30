package org.stjs.generator.writer;

import javax.lang.model.element.ExecutableElement;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;

public class MemberWriters {
	public static AstNode buildTarget(GenerationContext context, ExecutableElement methodDecl) {
		if (JavaNodes.isStatic(methodDecl)) {
			// TODO check global
			return JavaScriptNodes.name(context.getNames().getTypeName(context, methodDecl.getEnclosingElement()));
		}
		return JavaScriptNodes.keyword(Token.THIS);
	}
}
