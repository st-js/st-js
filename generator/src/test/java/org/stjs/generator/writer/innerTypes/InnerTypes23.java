package org.stjs.generator.writer.innerTypes;

public class InnerTypes23 {

	public int outerMethod() {
		return 0;
	}

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
