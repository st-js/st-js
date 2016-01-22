package org.stjs.generator.writer.trycatch;

import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

public class TryCatch1_simpleCatchClause {

	public static String main(String[] args) {
		Array<Object> array = JSCollections.$array();
		array.push("before try");
		try {
			array.push("before throw");
			if (true) {
				throw new RuntimeException();
			}
			array.push("after throw");
		} catch (Throwable t) {
			array.push("inside catch");
		} finally {
			array.push("inside finally");
		}
		array.push("after try catch");

		return array.join();
	}

}
