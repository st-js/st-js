package org.stjs.example.lib.bridge;

import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.STJSBridge;

/**
 * This class is a bridge for the stjs-lib-example.js javascript file. Use this class in a Java code (the is used as
 * input source to STJS).
 * 
 * @author acraciun
 * 
 */
@GlobalScope
@STJSBridge(sources = "/stjs/example/stjs-lib-example.js")
public class STJSExampleBridgeGlobal {
	public static int globalFunc(int x) {
		throw new UnsupportedOperationException();
	}

	public static STJSExampleModuleBridge STJSExampleModule;
}
