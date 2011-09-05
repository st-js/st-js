package test.generator.innerTypes;

public class InnerTypes1 {
	class InnerType {

	}

	public InnerType method() {
		return new InnerTypes1.InnerType();
	}
}
