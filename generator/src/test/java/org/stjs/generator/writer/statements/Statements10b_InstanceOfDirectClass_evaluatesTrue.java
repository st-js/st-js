package org.stjs.generator.writer.statements;

public class Statements10b_InstanceOfDirectClass_evaluatesTrue {

	public static boolean main(String[] args) {
		ClassB b = new ClassB();
		boolean result = b instanceof ClassB;
		return result;
	}

	public static class ClassA {

	}

	public static class ClassB extends ClassA {

	}

}
