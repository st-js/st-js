package org.stjs.generator.plugin.java8.util;

import java.util.Collections;
import java.util.List;

import javax.lang.model.type.TypeMirror;

import org.stjs.generator.javac.ErrorReporter;

import com.sun.source.tree.LambdaExpressionTree;
import com.sun.tools.javac.tree.JCTree;

public class Java8InternalUtils {

	@SuppressWarnings("unchecked")
	public static List<TypeMirror> getPossibleTypes(LambdaExpressionTree tree) {
		if (!(tree instanceof JCTree)) {
			ErrorReporter.errorAbort("InternalUtils.symbol: tree is not a valid Javac tree");
			return null; // dead code
		}

		if (!(tree instanceof JCTree.JCLambda)) {
			return Collections.emptyList();
		}

		return (List) ((JCTree.JCLambda) tree).targets;
	}
}
