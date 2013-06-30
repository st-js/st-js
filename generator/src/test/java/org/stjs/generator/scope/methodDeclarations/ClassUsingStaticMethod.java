package org.stjs.generator.scope.methodDeclarations;

import static org.stjs.generator.scope.methodDeclarations.ClassDefiningStaticMethod.doSth;

public class ClassUsingStaticMethod {

	static int classSth() {
		return org.stjs.generator.scope.methodDeclarations.ClassDefiningStaticMethod.doSth()
				+ ClassDefiningStaticMethod.doSth() + doSth();
	}
}
