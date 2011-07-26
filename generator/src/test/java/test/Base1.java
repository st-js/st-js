package test;

public class Base1 {
	private String field1;

	public Base1(String field1) {
		this.field1 = field1;
	}

	public String method(String param1) {
		return param1 + field1;
	}

	public static String staticMethod(String param1) {
		return param1 + "-static";
	}
}
