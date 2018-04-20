package org.stjs.generator.writer.conversions;

/**
 * (c) Swissquote 20.04.18
 *
 * @author sgoetz
 */
public class Conversion4 {

	public boolean equals(Object b) {
		return false;
	}

	public static void test() {
		Integer entry1 = 1;
		String entry2 = "hey";
		Boolean entry3 = false;
		Conversion4 entry4 = new Conversion4();

		entry1.equals(2);
		entry2.equals("ho");
		entry3.equals(true);
		entry4.equals(new Conversion4());

		if (!entry3.equals(true)) {

		}
	}
}
