package org.stjs.generator.writer.typedarrays;

public class StringArrayInit1 {
	public void testInit() {
		String[] arr = { "", null, "hello", "world", a() };
	}
	
	public String a() {
		return "42";
	}
	
//
//	public void testNewInit() {
//		float arr[] = new float[]{ 1.2f, 2f, 3, .4f };
//	}
//
//	public void test11() {
//		float arr[][] = new float[1][1];
//	}
//
//	public void test1_() {
//		float arr[][] = new float[1][];
//		arr[0][0] = 1f;
//	}


}
