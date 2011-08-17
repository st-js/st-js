/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator;

import java.io.File;

public class MyGenerator {
	public static void main(String[] args) {
		Generator gen = new Generator();
		// gen.generateJavascript(Thread.currentThread().getContextClassLoader(), Enums1.class, new File(
		// "src/test/java/test/Enums1.java"), new File("target/Enums1.js"), new GeneratorConfigurationBuilder()
		// .build());

		gen.generateJavascript(Thread.currentThread().getContextClassLoader(), new File(
				"src/test/java/test/Super1.java"), new File("target/Super1.js"), new GeneratorConfigurationBuilder()
				.allowedPackage("test").build());

		gen.generateJavascript(Thread.currentThread().getContextClassLoader(), new File(
				"src/test/java/test/Base1.java"), new File("target/Base1.js"), new GeneratorConfigurationBuilder()
				.allowedPackage("test").build());
	}
}
