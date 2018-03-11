package org.stjs.generator.writer.typedarrays;

public class StringArrayInit1 {
	public void testInit() {
		String[] arr = { "", null, "hello", "world", a() };
	}
	
	public String a() {
		return "42";
	}
}
