package org.stjs.generator.exec.json;

import org.stjs.javascript.Array;

public class Class1 {
	public static class Inner {
		public String type;
		public int i;

		public Inner() {
			type = "Inner";
		}
	}

	public String type;
	public Array<Inner> children;
	public int a;

	public Class1() {
		this.type = "Class1";
	}

}
