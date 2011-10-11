package org.stjs.generator.scope.simple;

import org.stjs.generator.scope.classloader.ClassWrapper;

public interface Variable {

	String getName();
	
	ClassWrapper getType();

}
