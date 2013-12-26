package org.stjs.javascript;

import org.stjs.javascript.annotation.Adapter;
import org.stjs.javascript.annotation.Template;
import org.stjs.javascript.functions.Function1;

@Adapter
public class JSStringAdapterBase {
	@Template("adapter")
	public native static Array<String> match(String applyTo, RegExp re);

	@Template("adapter")
	public native static Array<String> split(String applyTo, String re);

	@Template("adapter")
	public native static Array<String> split(String applyTo, String re, int limit);

	@Template("adapter")
	public native static String replace(String applyTo, RegExp re, String repl);

	@Template("adapter")
	public native static String replace(String applyTo, RegExp re, Function1<String, String> replaceFunction);

	@Template("adapter")
	public native static int charCodeAt(String applyTo, int x);

	@Template("adapter")
	public native static String fromCharCode(Class<? extends String> applyTo, int... codes);
}
