package org.stjs.generator.scope;

import static org.stjs.generator.scope.ClassDefiningStaticMethod.doSth;

public class ClassUsingStaticMethod {

	static int classSth() {
		return org.stjs.generator.scope.ClassDefiningStaticMethod.doSth() + ClassDefiningStaticMethod.doSth() + doSth();
	}
}
