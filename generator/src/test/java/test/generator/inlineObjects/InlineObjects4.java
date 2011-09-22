package test.generator.inlineObjects;

public class InlineObjects4 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Pojo o = new Pojo() {
			{
				this.a = 1;
			}
		};
	}
}
