package org.stjs.generator.writer.innerTypes;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class InnerTypes21 {

	public interface Inner {
		int getValue();
	}

	public static int main(String[] args) {
		Inner obj = new Inner() {
			public int getValue() {
				return 5;
			}
		};
		int result = obj.getValue();

		$js("console.log(result)");
		return result;
	}
}
