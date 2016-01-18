package org.stjs.generator.writer.statements;

public class Statements10d_InstanceOfClassNotInHierarchy_evaluatesFalse {

	public static boolean main(String[] args) {
		ClassA a = new ClassA();
		boolean result = a instanceof ClassB;
		return result;
	}

	public static class ClassA {

	}

	public static class ClassB extends ClassA {

	}

}
