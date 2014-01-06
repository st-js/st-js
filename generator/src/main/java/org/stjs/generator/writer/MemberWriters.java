package org.stjs.generator.writer;

import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.Keyword;

import com.sun.source.tree.Tree;

public final class MemberWriters {
	private MemberWriters() {
		//
	}

	public static <JS, T extends Tree> JS buildTarget(TreeWrapper<T, JS> tw) {
		if (tw.getEnclosingType().isGlobal()) {
			return null;
		}

		if (tw.isStatic()) {
			return tw.getContext().js().name(tw.getEnclosingType().getTypeName());
		}
		return tw.getContext().js().keyword(Keyword.THIS);
	}

}
