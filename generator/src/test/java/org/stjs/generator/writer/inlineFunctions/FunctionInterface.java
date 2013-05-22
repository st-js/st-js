package org.stjs.generator.writer.inlineFunctions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Template;

@JavascriptFunction
public interface FunctionInterface {
	@Template("invoke")
	public void $invoke(int arg);
}
