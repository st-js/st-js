package org.stjs.generator.writer.inlineFunctions;

import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.annotation.Template;

@JavascriptFunction
@STJSBridge
public interface FunctionInterface {
	@Template("invoke")
	public void $invoke(int arg);
}
