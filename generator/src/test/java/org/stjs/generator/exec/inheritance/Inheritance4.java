package org.stjs.generator.exec.inheritance;

public class Inheritance4 implements MyInterface {
	public static int main(String[] args) {
		Inheritance4 x = new Inheritance4();
		return (x instanceof MyInterface) ? 1 : 0;
	}
}
