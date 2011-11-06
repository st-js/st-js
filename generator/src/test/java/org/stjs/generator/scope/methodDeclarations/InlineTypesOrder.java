package org.stjs.generator.scope.methodDeclarations;

public class InlineTypesOrder {
	public static class Type1 {
		public int field1;
	}

	public static class Type2 {
		public int field2;
	}

	public InlineTypesOrder method1(Type1 t) {
		return this;
	}

	public InlineTypesOrder method2(Type2 t) {
		return this;
	}

	public static void main(String[] args) {
		new InlineTypesOrder().method1(new Type1() {
			{
				field1 = 1;
			}
		}).method2(new Type2() {
			{
				field2 = 2;
			}
		});
	}
}
