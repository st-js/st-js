package org.stjs.generator.utils;

import static org.junit.Assert.assertTrue;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSCollections.$map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.script.ScriptException;

import org.stjs.generator.BridgeClass;
import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollector;
import org.stjs.generator.GenerationDirectory;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfiguration;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.STJSRuntimeException;
import org.stjs.generator.executor.ExecutionResult;
import org.stjs.generator.executor.RhinoExecutor;
import org.stjs.generator.utils.LazyGenerationClassResolver.LazyGenerator;
import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.Map;

import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

@SuppressWarnings("PMD")
public abstract class AbstractStjsTest {

	private static final String TEMP_GENERATION_PATH = "temp-generated-js";

	private Generator generator;

	public AbstractStjsTest() {
	}

	/**
	 * @return the javascript code generator from the given class
	 */
	public String generate(Class<?> clazz) {
		return (String) executeOrGenerate(clazz, false, false);
	}

	/**
	 * @return the javascript code generator from the given class
	 */
	public String generate(Class<?> clazz, GeneratorConfiguration config) {
		return (String) executeOrGenerate(clazz, false, false, config);
	}

	/**
	 * @return the javascript code generator from the given class
	 */
	public String generateWithSourcemap(Class<?> clazz) {
		return (String) executeOrGenerate(clazz, false, true);
	}

	/**
	 * @return the javascript code generator from the given class
	 */
	public Object execute(Class<?> clazz) {
		return convert(executeOrGenerate(clazz, true, false));
	}

	public double executeAndReturnNumber(Class<?> clazz) {
		return executeAndReturnNumber(clazz, null);
	}

	public double executeAndReturnNumber(Class<?> clazz, GeneratorConfiguration extraConfig) {
		Object obj = convert(executeOrGenerate(clazz, true, false, extraConfig));
		if (obj == null) {
			return Double.NaN;
		}
		if (obj instanceof Number) {
			return ((Number) obj).doubleValue();
		}
		return Double.NaN;
	}

	public Object execute(String preGeneratedJs) {
		try {
			File jsfile = new File(preGeneratedJs);
			ExecutionResult execResult = new RhinoExecutor().run(Collections.singletonList(jsfile), false);
			return convert(execResult.getResult());
		}
		catch (ScriptException se) {
			throw new RuntimeException(se);
		}
	}

	private Object convert(Object result) {
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
		if (result.getClass().getName().contains("ScriptObjectMirror")) {
			// java 8 specific
			return convertScriptObject(result);
		}
		return result;
	}

	private Object invoke(Object obj, String method, int argCount, Object... args) {
		try {
			for (Method m : obj.getClass().getMethods()) {
				if (m.getName().equals(method) && m.getParameterTypes().length == argCount) {
					return m.invoke(obj, args);
				}
			}
			throw new STJSRuntimeException("Method " + method + " not found in class " + obj.getClass());
		}
		catch (InvocationTargetException e) {
			throw new STJSRuntimeException(e);
		}
		catch (IllegalArgumentException e) {
			throw new STJSRuntimeException(e);
		}
		catch (IllegalAccessException e) {
			throw new STJSRuntimeException(e);
		}
	}

	private static final int CALL_METHOD_ARG_COUNT = 3;

	private Object convertToDate(Object result) {
		Double time = (Double) invoke(result, "callMethod", CALL_METHOD_ARG_COUNT, result, "getTime", null);
		return new Date(time.longValue());
	}

	private static final int ARRAY_GET_METHOD_ARG_COUNT = 2;

	private Array<Object> convertToArray(Object result) {
		Array<Object> js = $array();
		long length = (Long) invoke(result, "getLength", 0);
		for (int i = 0; i < length; ++i) {
			Object value = invoke(result, "get", ARRAY_GET_METHOD_ARG_COUNT, i, null);
			js.push(convert(value));
		}
		return js;
	}

	private Map<String, Object> convertToMap(Object result) {
		Map<String, Object> js = $map();
		Object[] ids = (Object[]) invoke(result, "getIds", 0);
		for (Object key : ids) {
			Object value = invoke(result, "get", 2, key.toString(), null);
			js.$put(key.toString(), convert(value));
		}
		return js;
	}

	private static final int CALL_MEMBER_METHOD_ARG_COUNT = 2;

	@SuppressWarnings("unchecked")
	private Object convertScriptObject(Object result) {
		String cls = (String) invoke(result, "getClassName", 0);
		if ("Date".equals(cls)) {
			Double time = (Double) invoke(result, "callMember", CALL_MEMBER_METHOD_ARG_COUNT, "getTime", null);
			return new Date(time.longValue());
		}
		Map<String, Object> js = $map();
		Set<String> ids = (Set<String>) invoke(result, "keySet", 0);
		for (String key : ids) {
			Object value = invoke(result, "get", 1, key);
			js.$put(key, convert(value));
		}
		return js;
	}

	private Object executeOrGenerate(Class<?> clazz, boolean execute, boolean withSourceMap) {
		return executeOrGenerate(clazz, execute, withSourceMap, null);
	}

	/**
	 * @return the javascript code generator from the given class
	 */
	private Object executeOrGenerate(Class<?> clazz, boolean execute, boolean withSourceMap, GeneratorConfiguration extraConfig) {

		File generationPath = new File("target", TEMP_GENERATION_PATH);
		// We must create the output directory before building paths, or file.toURI will not include a trailing slash,
		// which fucks up URI.resolve
		if (!generationPath.exists() && !generationPath.mkdirs()) {
			throw new STJSRuntimeException("Unable to create generation directory");
		}
		GenerationDirectory generationFolder = new GenerationDirectory(generationPath, new File(TEMP_GENERATION_PATH), generationPath.toURI());
		final File sourcePath = new File("src/test/java");
		File resourcePath = new File("src/test/resources");
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		GeneratorConfiguration config = new GeneratorConfigurationBuilder(extraConfig) //
				.allowedPackage("org.stjs.javascript") //
				.allowedPackage("org.stjs.generator") //
				.allowedPackage(clazz.getPackage().getName()) //
				.generateSourceMap(withSourceMap) //
				.stjsClassLoader(classLoader) //
				.generationFolder(generationFolder)
				.targetFolder(new File("target", "test-classes"))
				.classResolver(new LazyGenerationClassResolver(classLoader, new LazyGenerator() {
					@Override
					public ClassWithJavascript generateJavaScript(String className) {
						return generator.generateJavascript(className, sourcePath);
					}
				})) //
				.build(); //

		this.generator = new Generator(config);

		ClassWithJavascript stjsClass = this.generator.generateJavascript(clazz.getName(), sourcePath);

		Timers.start("js-exec");
		List<File> javascriptFiles = new ArrayList<File>();
		try {
			File jsFile = new File(stjsClass.getJavascriptFiles().get(0).getPath());
			String content = Files.toString(jsFile, Charset.defaultCharset());
			List<ClassWithJavascript> allDeps =
					new DependencyCollector().orderAllDependencies(stjsClass);
			for (ClassWithJavascript dep : allDeps) {
				for (URI js : dep.getJavascriptFiles()) {
					if (dep instanceof BridgeClass) {
						javascriptFiles.add(new File(resourcePath, js.getPath()));
					} else {
						javascriptFiles.add(new File(js.getPath()));
					}
				}
			}
			ExecutionResult execResult = new RhinoExecutor().run(javascriptFiles, !execute);
			if (execute) {
				return execResult.getResult();
			}
			return content;
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		catch (ScriptException ex) {
			// display the generated code in case of exception
			for (File file : javascriptFiles) {
				displayWithLines(file);
			}
			throw new STJSRuntimeException(ex);
		}
		finally {
			Timers.end("js-exec");
		}
	}

	public ClassWithJavascript stjsClass(Class<?> clazz) {
		return this.generator.getExistingStjsClass(Thread.currentThread().getContextClassLoader(), clazz);
	}

	public void assertCodeContains(Class<?> clazz, String snippet) {
		assertCodeContains(generate(clazz), snippet);
	}

	public void assertCodeContains(Class<?> clazz, String snippet, GeneratorConfiguration extraConfig) {
		assertCodeContains(generate(clazz, extraConfig), snippet);
	}

	/**
	 * checks if the searched snippet is found inside the given code. The whitespaces are not taken into account
	 */
	public void assertCodeContains(String code, String snippet) {
		String cleanCode = code.replaceAll("\\s+", "");
		String cleanSnippet = snippet.replaceAll("\\s+", "");
		assertTrue("[" + snippet + "] not in \n" + code, cleanCode.contains(cleanSnippet));
	}

	public void assertCodeDoesNotContain(Class<?> clazz, String snippet) {
		assertCodeDoesNotContain(generate(clazz), snippet);
	}

	public void assertCodeDoesNotContain(Class<?> clazz, String snippet, GeneratorConfiguration extraConfig) {
		assertCodeDoesNotContain(generate(clazz, extraConfig), snippet);
	}

	/**
	 * checks if the searched snippet is not found inside the given code. The whitespaces are not taken into account
	 */
	public void assertCodeDoesNotContain(String code, String snippet) {
		String cleanCode = code.replaceAll("\\s+", "");
		String cleanSnippet = snippet.replaceAll("\\s+", "");
		assertTrue("[" + snippet + "] in \n" + code, !cleanCode.contains(cleanSnippet));
	}

	private static final int PAD = 5;

	private void displayWithLines(File file) {
		BufferedReader in = null;
		try {
			System.out.println("//---" + file);

			in = Files.newReader(file, Charset.defaultCharset());
			int lineNo = 1;
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.print(Strings.padEnd(lineNo + "", PAD, ' '));
				System.out.println(line);
				lineNo++;
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			Closeables.closeQuietly(in);
		}
	}

	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED",
			justification = "this is for tests only")
	public ClassLoader buildClassLoader() {
		File generationPath = new File("target", TEMP_GENERATION_PATH);
		try {
			URL[] urls = {generationPath.toURI().toURL()};
			return new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
