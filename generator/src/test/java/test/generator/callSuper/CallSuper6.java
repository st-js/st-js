package test.generator.callSuper;

public class CallSuper6 extends SuperClass {
	@Override
	public void instanceMethod(String arg) {
		staticMethod(arg);
	}
}
