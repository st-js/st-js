package org.stjs.generator.writer.declaration;

import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.writer.JavascriptKeywords;

import com.sun.source.tree.Tree;

public class AbstractMemberWriter<JS> {

	protected <T extends Tree> JS getMemberTarget(TreeWrapper<T, JS> tw) {
		if (tw.getEnclosingType().isGlobal()) {
			return null;
		}
		return tw.getContext().js().name(tw.isStatic() ? JavascriptKeywords.CONSTRUCTOR : JavascriptKeywords.PROTOTYPE);
	}

}
