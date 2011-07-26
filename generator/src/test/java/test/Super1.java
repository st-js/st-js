package test;

public class Super1 extends Base1 {
	public Super1() {
		super("abc");
	}

	@Override
	public String method(String param1) {
		return super.method(param1);
	}

}
