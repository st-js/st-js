package org.stjs.generator.writer.methods;

import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

public class Methods34_StjsArray_push_varArgs {

	public void doIt() {
		Array stjsArray = JSCollections.$array();
		stjsArray.push("a");
		stjsArray.push("b", "c");
	}

}
