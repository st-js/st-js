package org.stjs.generator.scope.inner;

import static org.stjs.generator.scope.inner.ClassDeclaringInnerClass.InnerClass.doSth;

import org.stjs.generator.scope.inner.ClassDeclaringInnerClass.InnerClass;

public class ClassUsingInnerClass {

	static int method() {
		return ClassDeclaringInnerClass.InnerClass.doSth() + InnerClass.doSth() + doSth();
	}
}
