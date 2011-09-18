package org.stjs.generator.utils;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.executor.RhinoExecutor;

import com.google.common.io.Files;

public class GeneratorTestHelper {
	private static final String TMP_FILE = "target/generator-temp.js";

	/**
	 * 
	 * @param clazz
	 * @return the javascript code generator from the given class
	 */
	public static String generate(Class<?> clazz) {
		Generator gen = new Generator();

		File file = new File(TMP_FILE);
		String path = clazz.getName().replace('.', File.separatorChar);
		path = "src/test/java/" + path + ".java";
		gen.generateJavascript(Thread.currentThread().getContextClassLoader(), new File(path), file,
				new GeneratorConfigurationBuilder().allowedPackage("test").allowedPackage("org.stjs.javascript")
						.build());

		try {
			String content = Files.toString(file, Charset.defaultCharset());
			// now generate the remaining imports
			file.delete();
			gen.generateJavascriptWithImports(Thread.currentThread().getContextClassLoader(), new File(path), file,
					new GeneratorConfigurationBuilder()
							//
							.generateMainMethodCall(false).allowedPackage("test").allowedPackage("org.stjs.javascript")
							.build());
			new RhinoExecutor().run(file);
			return content;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void assertCodeContains(Class<?> clazz, String snippet) {
		assertCodeContains(generate(clazz), snippet);
	}

	/**
	 * checks if the searched snippet is found inside the given code. The whitespaces are not taken into account
	 * 
	 * @param code
	 * @param search
	 */
	public static void assertCodeContains(String code, String snippet) {
		String cleanCode = code.replaceAll("\\s+", "");
		String cleanSnippet = snippet.replaceAll("\\s+", "");
		assertTrue("[" + snippet + "] not in \n" + code, cleanCode.contains(cleanSnippet));
		// TODO nice display error
	}

	public static void assertCodeDoesNotContain(Class<?> clazz, String snippet) {
		assertCodeDoesNotContain(generate(clazz), snippet);
	}

	/**
	 * checks if the searched snippet is not found inside the given code. The whitespaces are not taken into account
	 * 
	 * @param code
	 * @param search
	 */
	public static void assertCodeDoesNotContain(String code, String snippet) {
		String cleanCode = code.replaceAll("\\s+", "");
		String cleanSnippet = snippet.replaceAll("\\s+", "");
		assertTrue("[" + snippet + "] in \n" + code, !cleanCode.contains(cleanSnippet));
		// TODO nice display error
	}
}
