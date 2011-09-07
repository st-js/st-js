package test.generator.callSuper;

public class CallSuper5 extends SuperClass {
	@Override
	public void instanceMethod(String arg) {
		super.staticMethod(arg);
	}
}
