package org.stjs.example.lib.bridge;

import org.stjs.javascript.annotation.STJSBridge;

/**
 * This is a bridge for the module found in the stjs-lib-example.js
 * 
 * @author acraciun
 * 
 */
@STJSBridge(sources = "/stjs/example/stjs-lib-example.js")
abstract public class STJSExampleModuleBridge {
	abstract public int multiply(int x, int y);
}
