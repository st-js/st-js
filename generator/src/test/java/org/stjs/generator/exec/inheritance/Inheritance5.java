package org.stjs.generator.exec.inheritance;

public class Inheritance5 implements MyInterface {
	public static int main(String[] args) {
		Inheritance5 x = new Inheritance5();
		return (x instanceof MySuperInterface) ? 1 : 0;
	}
}
