package org.stjs.generator.writer.methods;


public class Methods10_basic_varargs<T> {
	public static String main(String[] args) {
		Methods10_basic_varargs<String> obj = new Methods10_basic_varargs<>();

		String invokedWithVarArgAsFirstParameters = obj.methodVarArgAsFirstParameter("A", "B", "C");
		String invokedWithVargArgAsSecondParameters = obj.methodVarArgAsSecondParameter(0, "1", "2", "3");

		String invokedWithMissingVarArgAsFirstParameters = obj.methodVarArgAsFirstParameter();
		String invokedWithMissingVarArgAsSecondParameters = obj.methodVarArgAsSecondParameter(0);

		String invokedStaticMethodVarArgAsFirstParameter = staticMethodVarArgAsFirstParameter("D", "E", "F");
		String invokedStaticMethodVarArgAsSecondParameters = staticMethodVarArgAsSecondParameter(0, "H", "I", "J");

		return invokedWithVarArgAsFirstParameters + "-" +
				invokedWithVargArgAsSecondParameters + "-" +
				invokedWithMissingVarArgAsFirstParameters + "-" +
				invokedWithMissingVarArgAsSecondParameters + "-" +
				invokedStaticMethodVarArgAsFirstParameter + "-" +
				invokedStaticMethodVarArgAsSecondParameters + ".";
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
