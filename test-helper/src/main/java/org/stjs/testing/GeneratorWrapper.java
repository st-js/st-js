package org.stjs.testing;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.utils.ClassUtils;

import com.google.common.collect.Sets;

public class GeneratorWrapper {

	public File generateCode(final TestClass testClass,
			final FrameworkMethod method) throws IOException, AssertionError {
		Generator generator = new Generator();
		File outputFile = File.createTempFile(testClass.getName(), ".js");
		outputFile.deleteOnExit();

		generator
				.generateJavascriptWithImports(
						Thread.currentThread().getContextClassLoader(),
						testClass.getJavaClass().getName(),
						outputFile,
						new GeneratorConfigurationBuilder()
								.generateMainMethodCall(false)
								// do not generate the "main" method call
								.allowedPackage(
										testClass.getJavaClass().getPackage()
												.getName())
								.allowedPackage("org.stjs.javascript")
								.allowedPackage("org.junit")
								.allowedPackage("org.stjs.testing").build());

		return outputFile;
	}

	private boolean checkMockType(String className) {
		try {
			return ClassUtils.isMockType(Class.forName(className));
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
