package org.stjs.generator.writer.innerTypes;

public class InnerTypes20 {

	private static class Holder {
		private static final int VALUE = 2;
	}

	private static int currentValue = Holder.VALUE;

	public static int main(String[] args) {
		return currentValue;
	}
}
