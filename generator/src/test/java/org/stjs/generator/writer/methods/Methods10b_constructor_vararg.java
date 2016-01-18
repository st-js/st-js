package org.stjs.generator.writer.methods;


public class Methods10b_constructor_vararg<T> {
	public static void main(String[] args) {
		new ClassWithVarArgInConstructor("A", "B", "C");
	}

	public static class ClassWithVarArgInConstructor {
		public ClassWithVarArgInConstructor(String... strings) {
		}
	}

}
