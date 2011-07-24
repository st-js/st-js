package org.stjs.generator;

import java.io.File;

import test.Enums1;

public class MyGenerator {
	public static void main(String[] args) {
		Generator gen = new Generator();
		// gen.generateJavascript(Thread.currentThread().getContextClassLoader(), Enums1.class, new File(
		// "src/test/java/test/Enums1.java"), new File("target/Enums1.js"), new GeneratorConfigurationBuilder()
		// .build());

		gen.generateJavascript(Thread.currentThread().getContextClassLoader(), Enums1.class, new File(
				"src/test/java/test/Super1.java"), new File("target/Super1.js"), new GeneratorConfigurationBuilder()
				.build());
	}
}
