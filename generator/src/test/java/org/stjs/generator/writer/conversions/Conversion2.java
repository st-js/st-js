package org.stjs.generator.writer.conversions;

/**
 * (c) Swissquote 20.04.18
 *
 * @author sgoetz
 */
public class Conversion2 {
	public static void test() {
		Integer result1 = Integer.parseInt("1");
		Short result2 = Short.parseShort("2");
		Long result3 = Long.parseLong("3");
		Byte result4 = Byte.parseByte("4");
		Double result5 = Double.parseDouble("5.0");
		Float result6 = Float.parseFloat("2.0");
		Boolean result7 = Double.isNaN(400);

		//STJS_STATIC_METHODS.put("isNaN", numberTypes);

	}
}
