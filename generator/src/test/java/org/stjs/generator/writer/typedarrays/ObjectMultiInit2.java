package org.stjs.generator.writer.typedarrays;

public class ObjectMultiInit2 {
	public static Object[][][] testName() {
		Object[][][] ac = new Object[][][]{ new String[][]{ { "hello" }, { "world" }, new String[]{} }, new Integer[][]{ { 1, 2 }, { 3, 4 } },
				new Double[4][5] };
		return ac;
	}

}
