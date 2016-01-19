package org.stjs.generator.writer.statements;

public class Statements10e_InstanceOfNullObject_evaluatesFalse {

	public static boolean main(String[] args) {
		ClassA a = null;
		boolean result = a instanceof ClassA;
		return result;
	}

	public static class ClassA {

	}

}
