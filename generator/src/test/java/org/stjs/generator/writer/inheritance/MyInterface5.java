package org.stjs.generator.writer.inheritance;

import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.SyntheticType;

@SyntheticType
public interface MyInterface5 {
	boolean someMethod();

	String someMethodWithParams(Integer number, Array someArray);

	// TODO :: forbid interface values
	String test = null;
}
