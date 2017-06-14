package org.stjs.generator.writer.innerTypes;

import org.stjs.javascript.annotation.SyntheticType;

public class InnerTypes23 {

	public int outerMethod() {
		return 0;
	}

	@SyntheticType
	public static class Inner {
		public int x;
	}

	public void method() {
		Inner obj = new Inner() {
			{
				x = outerMethod();
			}
		};
	}
}
