package org.stjs.generator;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

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

		String path = clazz.getName().replace('.', File.separatorChar);
		path = "src/test/java/" + path + ".java";
		gen.generateJavascript(Thread.currentThread().getContextClassLoader(), new File(path), new File(TMP_FILE),
				new GeneratorConfigurationBuilder().allowedPackage("test").build());

		try {
			return Files.toString(new File(TMP_FILE), Charset.defaultCharset());
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
}
