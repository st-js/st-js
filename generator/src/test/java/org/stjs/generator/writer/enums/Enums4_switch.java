package org.stjs.generator.writer.enums;

public class Enums4_switch {

	public enum Enums4 {
		a, b, c;
	}

	public void main() {
		Enums4 x = Enums4.c;
		switch (x) {
			case a:
				break;
			case b:
				break;
		}
	}

}
