package test;

public class CheckPackages5 {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Object o = new RuntimeException();// this class is not a java.lang accepted class
	}
}
