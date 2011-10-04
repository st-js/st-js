package org.stjs.testing;

import java.io.File;
import java.io.IOException;

import org.junit.runners.model.TestClass;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfigurationBuilder;

public class GeneratorWrapper {

	public File generateCode(final TestClass testClass) throws IOException, AssertionError {
		Generator generator = new Generator();
		File outputFile = File.createTempFile(testClass.getName(), ".js");
		outputFile.deleteOnExit();

		generator.generateJavascriptWithImports(Thread.currentThread().getContextClassLoader(), testClass
				.getJavaClass().getName(), outputFile, new GeneratorConfigurationBuilder()
				.generateMainMethodCall(false)
				// do not generate the "main" method call
				.allowedPackage(testClass.getJavaClass().getPackage().getName()).allowedPackage("org.stjs.javascript")
				.allowedPackage("org.junit").allowedPackage("org.stjs.testing").build());

		return outputFile;
	}

}
