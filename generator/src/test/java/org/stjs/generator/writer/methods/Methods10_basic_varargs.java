package org.stjs.generator.writer.methods;


public class Methods10_basic_varargs<T> {
	public static String main(String[] args) {
		Methods10_basic_varargs<String> obj = new Methods10_basic_varargs<>();

		String invokedWithVarArgAsFirstParameters = obj.methodVarArgAsFirstParameter("A", "B", "C");
		String invokedWithVargArgAsSecondParameters = obj.methodVarArgAsSecondParameter(0, "1", "2", "3");

		String missingVarArgInvokedWithAsFirstParameters = obj.methodVarArgAsFirstParameter();
		String missingVarArgInvokedWithAsSecondParameters = obj.methodVarArgAsSecondParameter(0);

		String invokedStaticMethodVarArgAsFirstParameter = staticMethodVarArgAsFirstParameter("D", "E", "F");
		String invokedStaticMethodVarArgAsSecondParameters = staticMethodVarArgAsSecondParameter(0, "H", "I", "J");

		String[] stringArray;
		stringArray = new String[]{"K", "L", "M"};
		String arrayAsVarArgInvokedWithAsFirstParameters = obj.methodVarArgAsFirstParameter(stringArray);

		stringArray = new String[]{"N", "O", "P"};
		String arrayAsVarArgInvokedWithAsSecondParameters = obj.methodVarArgAsSecondParameter(0, stringArray);

		return invokedWithVarArgAsFirstParameters + "-" +
				invokedWithVargArgAsSecondParameters + "-" +
				missingVarArgInvokedWithAsFirstParameters + "-" +
				missingVarArgInvokedWithAsSecondParameters + "-" +
				invokedStaticMethodVarArgAsFirstParameter + "-" +
				invokedStaticMethodVarArgAsSecondParameters + "-" +
				arrayAsVarArgInvokedWithAsFirstParameters + "-" +
				arrayAsVarArgInvokedWithAsSecondParameters + ".";
	}

	public String methodVarArgAsFirstParameter(T... strings) {
		return strings.toString();
	}
	public String methodVarArgAsSecondParameter(int param, T... strings) {
		return strings.toString();
	}

	public static String staticMethodVarArgAsFirstParameter(String... strings) {
		return strings.toString();
	}
	public static String staticMethodVarArgAsSecondParameter(int param, String... strings) {
		return strings.toString();
	}

}
