package org.stjs.generator.utils;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollection;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.STJSClass;
import org.stjs.generator.executor.RhinoExecutor;

import com.google.common.base.Strings;
import com.google.common.io.Files;

public class GeneratorTestHelper {
	private static final String TEMP_GENERATION_PATH = "temp-generated-js";

	/**
	 * 
	 * @param clazz
	 * @return the javascript code generator from the given class
	 */
	public static String generate(Class<?> clazz) {
		Generator gen = new Generator();

		String sourcePath = "src/test/java";
		STJSClass stjsClass = gen.generateJavascript(
				Thread.currentThread().getContextClassLoader(),
				clazz.getName(),
				new File(sourcePath),
				new File("target", TEMP_GENERATION_PATH),
				new File("target", "test-classes"),
				new GeneratorConfigurationBuilder().allowedPackage("org.stjs.javascript")
						.allowedPackage("org.stjs.generator").build());

		File jsFile = new File(stjsClass.getJavascriptFiles().get(0).getPath().substring(1));
		try {
			String content = Files.toString(jsFile, Charset.defaultCharset());
			List<File> javascriptFiles = new ArrayList<File>();
			for (ClassWithJavascript dep : new DependencyCollection(stjsClass).orderAllDependencies()) {
				for (URI js : dep.getJavascriptFiles()) {
					javascriptFiles.add(new File(js.getPath().substring(1)));
				}
			}
			new RhinoExecutor().run(javascriptFiles, true);
			return content;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ScriptException ex) {
			// display the generated code in case of exception
			for (URI file : stjsClass.getJavascriptFiles()) {
				displayWithLines(new File(file.getPath().substring(1)));
			}
			throw new RuntimeException(ex);
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
	}

	private static void displayWithLines(File file) {
		BufferedReader in = null;
		try {
			System.out.println("//---" + file);
			in = new BufferedReader(new FileReader(file));
			int lineNo = 1;
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.print(Strings.padEnd(lineNo + "", 5, ' '));
				System.out.println(line);
				lineNo++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// silent
			}

		}
	}
}
