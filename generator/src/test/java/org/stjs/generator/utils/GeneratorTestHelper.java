package org.stjs.generator.utils;

import static org.junit.Assert.assertTrue;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSCollections.$map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptException;

import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollection;
import org.stjs.generator.GenerationDirectory;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.executor.ExecutionResult;
import org.stjs.generator.executor.RhinoExecutor;
import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.Map;

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
		return (String) executeOrGenerate(clazz, false);
	}

	/**
	 * 
	 * @param clazz
	 * @return the javascript code generator from the given class
	 */
	public static Object execute(Class<?> clazz) {
		return convert(executeOrGenerate(clazz, true));
	}
	
	public static Object execute(String preGeneratedJs){
		try {
			File jsfile = new File(preGeneratedJs);
			ExecutionResult execResult = new RhinoExecutor().run(Collections.singletonList(jsfile), false);
			return convert(execResult.getResult());
		}catch(ScriptException se){
			throw new RuntimeException(se);
		}
	}

	private static Object convert(Object result) {
		if (result == null) {
			return null;
		}
		if (result.getClass().getName().contains("NativeObject")) {
			return convertToMap(result);
		}
		if (result.getClass().getName().contains("NativeArray")) {
			return convertToArray(result);
		}
		if (result.getClass().getName().contains("NativeDate")) {
			return convertToDate(result);
		}
		return result;
	}

	private static Object invoke(Object obj, String method, int argCount, Object... args) {
		try {
			for (Method m : obj.getClass().getMethods()) {
				if (m.getName().equals(method) && (m.getParameterTypes().length == argCount)) {
					return m.invoke(obj, args);
				}
			}
			throw new RuntimeException("Method " + method + " not found in class " + obj.getClass());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Object convertToDate(Object result) {
		Double time = (Double) invoke(result, "callMethod", 3, result, "getTime", null);
		return new Date(time.longValue());
	}

	private static Array<Object> convertToArray(Object result) {
		Array<Object> js = $array();
		long length = (Long) invoke(result, "getLength", 0);
		for (int i = 0; i < length; ++i) {
			Object value = invoke(result, "get", 2, i, null);
			js.push(convert(value));
		}
		return js;
	}

	private static Map<String, Object> convertToMap(Object result) {
		Map<String, Object> js = $map();
		Object[] ids = (Object[]) invoke(result, "getIds", 0);
		for (Object key : ids) {
			Object value = invoke(result, "get", 2, key.toString(), null);
			js.$put(key.toString(), convert(value));
		}
		return js;
	}

	/**
	 * 
	 * @param clazz
	 * @return the javascript code generator from the given class
	 */
	private static Object executeOrGenerate(Class<?> clazz, boolean execute) {
		Generator gen = new Generator();

		File generationPath = new File("target", TEMP_GENERATION_PATH);
		GenerationDirectory generationFolder = new GenerationDirectory(generationPath, new File(TEMP_GENERATION_PATH),
				new File(""));
		String sourcePath = "src/test/java";
		ClassWithJavascript stjsClass = gen.generateJavascript(
				Thread.currentThread().getContextClassLoader(),
				clazz.getName(),
				new File(sourcePath),
				generationFolder,
				new File("target", "test-classes"),
				new GeneratorConfigurationBuilder().allowedPackage("org.stjs.javascript")
						.allowedPackage("org.stjs.generator").build());

		File jsFile = new File(generationPath, stjsClass.getJavascriptFiles().get(0).getPath());
		try {
			String content = Files.toString(jsFile, Charset.defaultCharset());
			List<File> javascriptFiles = new ArrayList<File>();
			List<ClassWithJavascript> allDeps = new DependencyCollection(stjsClass).orderAllDependencies(Thread
					.currentThread().getContextClassLoader());
			for (ClassWithJavascript dep : allDeps) {
				for (URI js : dep.getJavascriptFiles()) {
					javascriptFiles.add(new File(generationPath, js.getPath()));
				}
			}
			ExecutionResult execResult = new RhinoExecutor().run(javascriptFiles, !execute);
			if (execute) {
				return execResult != null ? execResult.getResult() : null;
			}
			return content;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ScriptException ex) {
			// display the generated code in case of exception
			for (URI file : stjsClass.getJavascriptFiles()) {
				displayWithLines(new File(generationPath, file.getPath()));
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
