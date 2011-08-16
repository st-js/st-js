package test.innerclasses;

import static test.innerclasses.ClassDeclaringInnerClass.InnerClass.doSth;
import test.innerclasses.ClassDeclaringInnerClass.InnerClass;

public class ClassUsingInnerClass {

	static int method() {
		return ClassDeclaringInnerClass.InnerClass.doSth() + InnerClass.doSth() + doSth();
	}
}
