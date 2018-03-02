package org.stjs.generator.writer.typedarrays;

public class ArrayInstanceOf {
	public static int intArray() {
		byte[] b = new byte[1];
		Object o = b;
        if (o instanceof Object[]) {
            return 43;
        }
        if (o instanceof byte[]) {
            return 44;
        }
		return 42;
	}
	
	public static int main(String[] args) {
        return intArray();
    }

}
