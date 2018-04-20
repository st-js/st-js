package org.stjs.generator.writer.conversions;

/**
 * (c) Swissquote 20.04.18
 *
 * @author sgoetz
 */
public class Conversion3 {
	public static void test() {
		Integer entry1 = 1;
		Short   entry2 = 2;
		Long    entry3 = 3L;
		Byte    entry4 = 4;
		Double  entry5 = 5.0;
		Float   entry6 = 6F;

		Short result1 = entry1.shortValue();
		Long result2 = entry2.longValue();
		Byte result3 = entry3.byteValue();
		Double result4 = entry4.doubleValue();
		Float result5 = entry5.floatValue();
		Integer result6 = entry6.intValue();
		Boolean result7 = entry6.isNaN();
	}
}
