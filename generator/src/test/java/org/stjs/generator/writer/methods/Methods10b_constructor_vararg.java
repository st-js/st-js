package org.stjs.generator.writer.methods;


public class Methods10b_constructor_vararg<T> {
	public static void main(String[] args) {
		new ClassWithVarArgInConstructorAsSingleParameter("A", "B", "C");
		new ClassWithVarArgInConstructorAsSecondParameter(0, "A", "B", "C");
	}

}
