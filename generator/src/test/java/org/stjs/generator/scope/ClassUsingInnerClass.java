package org.stjs.generator.scope;

import static org.stjs.generator.scope.ClassDeclaringInnerClass.InnerClass.doSth;

import org.stjs.generator.scope.ClassDeclaringInnerClass.InnerClass;

public class ClassUsingInnerClass {

	static int method() {
		return ClassDeclaringInnerClass.InnerClass.doSth() + InnerClass.doSth() + doSth();
	}
}
