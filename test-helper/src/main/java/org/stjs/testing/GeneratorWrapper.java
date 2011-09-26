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

	public File generateCode(final TestClass testClass, final FrameworkMethod method) throws IOException,
			AssertionError {
		Generator generator = new Generator();
		File outputFile = File.createTempFile(testClass.getName(), ".js");
		outputFile.deleteOnExit();
		Pattern exceptions = Pattern.compile("java\\.lang.*|org\\.stjs\\.testing.*|org\\.junit.*|junit.*");
		try {
			Set<String> newImports = Sets.newHashSet();
			newImports.add(testClass.getJavaClass().getName());

			Set<String> convertedClasses = Sets.newHashSet();

			do {
				Iterator<String> iterator = newImports.iterator();
				String nextFile = iterator.next();
				iterator.remove();
				File maybeTestFile = new File("src/test/java/" + nextFile.replaceAll("\\.", "/") + ".java");
				File src;
				if (checkMockType(nextFile)) {
					continue;
				}
				if (nextFile.startsWith("org.stjs.testing") || nextFile.startsWith("org.stjs.javascript")) {
					continue;
				}
				if (nextFile.contains("$")) {
					// this is an inner class
					continue;
				}
				if (maybeTestFile.exists()) {
					src = maybeTestFile;
				} else {
					File maybeAppFile = new File("src/main/java/" + nextFile.replaceAll("\\.", "/") + ".java");
					if (maybeAppFile.exists()) {
						src = maybeAppFile;
					} else {
						throw new IllegalStateException(
								"Unable to locate the source file for type "
										+ nextFile
										+ ". Currently only src/test/java and src/main/java naming schemes are supported. Note that all classes must be defined in the same module as the unit test");
					}
				}
				System.out.println("Added generated class :" + src);

				Set<String> iterationResolvedImports = generator.generateJavascript(
						Thread.currentThread().getContextClassLoader(),
						src,
						outputFile,
						new GeneratorConfigurationBuilder()
								.generateMainMethodCall(false)
								// do not generate the "main" method call
								.allowedPackage(testClass.getJavaClass().getPackage().getName())
								.allowedPackage("org.stjs.javascript").allowedPackage("org.junit")
								.allowedPackage("org.stjs.testing").build(), true);
				for (String iterationResolvedImport : iterationResolvedImports) {

					if (!exceptions.matcher(iterationResolvedImport).matches()
							&& convertedClasses.add(iterationResolvedImport)) {
						newImports.add(iterationResolvedImport);
					}
				}
			} while (!newImports.isEmpty());

		} catch (JavascriptGenerationException e) {
			e.printStackTrace();
			throw new AssertionError(e.getMessage());
		}
		// Files.copy(outputFile, System.out);
		// System.out.flush();
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
