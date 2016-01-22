package org.stjs.generator.writer.innerTypes;

@SuppressWarnings("unused")
public class InnerTypes17 {

	public static void main() {
		Inner.EnumInsideNestedClass deep = Inner.EnumInsideNestedClass.a;
	}

	private static class Inner {
		enum EnumInsideNestedClass {
			a, b, c;
		}
	}
}
