package org.stjs.generator.writer.typedarrays;

public class Float32ArrayInit1 {
	public void testInit() {
		float[] arr = { 1.2f, 2f, 3, .4f, a() };
	}
	
	public int a() {
		return 42;
	}
}
