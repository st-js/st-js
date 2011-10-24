package test.generator.methods;

public class Methods13<T extends Methods13<?>> {
	public T parent() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		Methods13<?> m = new Methods13();
		m.parent().parent();
	}
}
