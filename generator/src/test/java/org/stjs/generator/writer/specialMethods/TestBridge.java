package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.annotation.Template;

@STJSBridge(
		sources = "javascript/bridges.js")
public class TestBridge {
	@Template("toProperty")
	public native int $length();

	@Template("toProperty")
	public native void $length(int n);

	@Template("prefix")
	public native String _prefix();

	@Template("none")
	public native String $get(int n);

	@Template("prefix(then)")
	public native String thenSay();

	@Template("suffix(Special)")
	public native void saySpecial();
}
