package org.stjs.generator.exec.inheritance;


public class Inheritance3 extends MySuperClass {
	public static int main(String[] args) {
		Inheritance3 x = new Inheritance3();
		return (x instanceof MySuperClass) ? 1 : 0;
	}
}
