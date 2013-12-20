package org.stjs.generator.writer;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.utils.JavaNodes;

public class MemberWriters {
	public static <JS> JS buildTarget(GenerationContext<JS> context, Element memberDecl) {
		if (JavaNodes.isGlobal(memberDecl.getEnclosingElement())) {
			return null;
		}
		if (JavaNodes.isStatic(memberDecl)) {
			return context.js().name(context.getNames().getTypeName(context, memberDecl.getEnclosingElement()));
		}
		return context.js().keyword(Keyword.THIS);
	}

}
