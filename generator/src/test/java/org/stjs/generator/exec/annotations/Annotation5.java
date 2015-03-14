package org.stjs.generator.exec.annotations;

import org.stjs.javascript.JSGlobal;
import org.stjs.javascript.Map;

public class Annotation5 {
	public int method(int p1, @MyAnnotations.WithMultipleValues(n = 2 + 3) int p2) {
		return 0;
	}

	public static int main(String[] args) {
		Map<String, Object> myAnn = JSGlobal.stjs.getParameterAnnotation(Annotation5.class, "method", 1, "MyAnnotations.WithMultipleValues");
		return (Integer) myAnn.$get("n");
	}
}
