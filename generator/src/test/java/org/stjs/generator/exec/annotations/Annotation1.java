package org.stjs.generator.exec.annotations;

import org.stjs.javascript.JSGlobal;
import org.stjs.javascript.Map;

@MyAnnotations.WithMultipleValues(n = 10)
public class Annotation1 {
	public static int main(String[] args) {
		Map<String, Object> myAnn = JSGlobal.stjs.getTypeAnnotation(Annotation1.class, "MyAnnotations.WithMultipleValues");
		return (Integer) myAnn.$get("n");
	}
}
