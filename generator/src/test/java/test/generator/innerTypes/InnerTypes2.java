package test.generator.innerTypes;


public class InnerTypes2 {
	static class InnerType {

	}

	public InnerType method() {
		return new InnerTypes2.InnerType();
	}
}
