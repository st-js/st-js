package org.stjs.generator.writer.innerTypes;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class InnerTypes20 {

	private static class Holder {
		private static final int VALUE = 2;
	}

	private static int currentValue = Holder.VALUE;

	public static int main(String[] args) {
		int result = currentValue;
		$js("console.log(result)");
		return result;
	}
}
