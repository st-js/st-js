package org.stjs.generator.writer.inlineObjects;

import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.annotation.Template;

@STJSBridge
public class BridgePojo {

	@Template("toProperty")
	public native void x(String str);

	@Template("toProperty")
	public native void x(int i);

	@Template("toProperty")
	public native Object x();

	public native void oops(String str);

	@Template("array")
	public native void damned(int i);
}
