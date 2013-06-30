package org.stjs.generator.scope.simple;

public class SimpleClass {

	public static class AmbiguousName {
		public static class InnerClassLevel2 {
			public static Integer innerField;
		}
	}

	public static AmbiguousName AmbiguousName;

	public static void AmbiguousName() {
	}

	public static class InnerClass2 {
	}

	public static void method() {

	}

	public static void method(Integer x) {

	}

	public static Integer field;

}
