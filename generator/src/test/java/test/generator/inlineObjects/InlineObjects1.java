package test.generator.inlineObjects;

public class InlineObjects1 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Pojo o = new Pojo() {
			{
				a = 1;
				b = "x";
			}
		};
	}
}
