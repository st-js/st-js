package org.stjs.generator.writer.innerTypes;

@SuppressWarnings("unused")
public class InnerTypes17 {

	public static void main() {
		Inner.Enum deep = Inner.Enum.a;
	}

	private static class Inner {
		enum Enum {
			a, b, c;
		}
	}
}
