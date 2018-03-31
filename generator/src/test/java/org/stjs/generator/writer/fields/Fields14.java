package org.stjs.generator.writer.fields;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Fields14 {

	public static final int ONE = 1;
	public static final int TWO = ONE + 1;

	public static int main(@SuppressWarnings("unused") String[] args) {
		int result = TWO;
		$js("console.log(result)");
		return result;
	}
}
