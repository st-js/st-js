package test.generator.inlineObjects;

public class InlineObjects4 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		final Pojo other = new Pojo();
		Pojo o = new Pojo() {
			{
				this.a = other.a;
			}
		};
	}
}
