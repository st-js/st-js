package test.generator.statements;

public class Statements9 {

	public void method(Object... args) {

	}

	public void main() {
		method("abc", "\"", "'", 'a', '\'', 1D, 2f, 1l);
	}
}
