package org.stjs.javascript;

public final class JSON {
	private JSON() {
		// forbid the creation using this type
	}

	public native Object parse  (String text);

	public native String stringify  (Object obj);
}
