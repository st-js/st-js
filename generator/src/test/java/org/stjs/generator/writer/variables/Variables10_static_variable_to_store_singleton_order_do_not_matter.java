package org.stjs.generator.writer.variables;

public class Variables10_static_variable_to_store_singleton_order_do_not_matter {

	// Can declare static method before constructor
	public static Variables10_static_variable_to_store_singleton_order_do_not_matter singleton = new Variables10_static_variable_to_store_singleton_order_do_not_matter();

	public Variables10_static_variable_to_store_singleton_order_do_not_matter() {
	}

	public String getAValue() {
		return getAValueFromStaticMethodDeclaredAfterAllMethods();
	}

	public static String getAValueFromStaticMethodDeclaredAfterAllMethods() {
		return "This is a value returned by the static method";
	}

	public static String main(String[] args) {
		return singleton.getAValue();
	}

}
