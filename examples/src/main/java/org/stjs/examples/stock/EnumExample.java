package org.stjs.examples.stock;

public class EnumExample {

	enum MyEnum {
		A, B
	}

	int getNum(MyEnum m) {
		MyEnum n = m;
		switch (n) {
		case A:
			return 1;
		case B:
			return 2;
		default:
			return 3;
		}
	}
}
