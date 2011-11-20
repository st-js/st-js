package org.stjs.generator.writer.innerTypes;

public class InnerTypes11 {

	private enum InnerEnum {
		a(new MyBean());

		private final MyBean value;

		private InnerEnum(MyBean bean) {
			this.value = bean;
		}

		public MyBean getValue() {
			return value;
		}

	}

}
