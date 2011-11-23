package org.stjs.generator.scope.inner;

import org.stjs.javascript.Array;

public class ClassDeclaringInnerClass2 {

	private InnerClass inner;

	public int method() {
		return inner.doSth();
	}

	public class InnerClass {
		public InnerClass(Array<String> n) {

		}

		int doSth() {
			return 1;
		}
	}

}
