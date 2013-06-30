package org.stjs.generator.writer.innerTypes;

public class InnerTypes16 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Object o = new Object() {
			public void denver() {
				InnerDeep deep = new InnerDeep();
			}

			class InnerDeep {
				private String a = null;
			}
		};
	}
}
