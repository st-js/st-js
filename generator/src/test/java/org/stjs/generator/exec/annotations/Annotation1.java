package org.stjs.generator.exec.annotations;

import org.stjs.javascript.JSGlobal;
import org.stjs.javascript.Map;

import static org.stjs.javascript.JSObjectAdapter.$js;

@MyAnnotations.WithMultipleValues(n = 10)
public class Annotation1 {
	public static int main(String[] args) {
		Map<String, Object> myAnn = JSGlobal.stjs.getTypeAnnotation(Annotation1.class, "MyAnnotations.WithMultipleValues");
		int result = (Integer) myAnn.$get("n");

		$js("console.log(result)");
		return 1;
	}
}
