package org.stjs.generator.sourcemap;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.stjs.generator.STJSClass;
import org.stjs.generator.STJSRuntimeException;
import org.stjs.generator.utils.PreConditions;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.google.common.io.Resources;
import com.google.debugging.sourcemap.SourceMapConsumerFactory;
import com.google.debugging.sourcemap.SourceMapParseException;
import com.google.debugging.sourcemap.SourceMapping;

public class JavascriptToJava {
	private final static Pattern STACKTRACE_JS_PATTERN = Pattern.compile("\\s*at\\s*(?:(.+)\\.)?(\\w+)\\s*\\((.+)\\)");
	private final static int STACKTRACE_GROUP_METHOD = 2;
	private final static int STACKTRACE_GROUP_LOCATION = 3;

	private final ClassLoader classLoader;

	public JavascriptToJava(ClassLoader testClassLoader) {
		this.classLoader = testClassLoader;
	}

	public int getJavaLine(String path, int lineNumber) {
		String sourceMapFile = path.replaceAll("\\.js$", ".map");
		URL url = classLoader.getResource(sourceMapFile.substring(1));
		if (url == null) {
			return lineNumber;
		}
		String contents;
		try {
			contents = Resources.toString(url, Charsets.UTF_8);
			SourceMapping mapping = SourceMapConsumerFactory.parse(contents);
			return mapping.getMappingForLine(lineNumber, 1).getLineNumber();
		} catch (IOException e) {
			throw new STJSRuntimeException(e);
		} catch (SourceMapParseException e) {
			throw new STJSRuntimeException(e);
		}
	}

	private String getClassName(String propertiesFile) {
		InputStream in = null;
		try {
			in = classLoader.getResourceAsStream(propertiesFile.substring(1));
			if (in == null) {
				return null;
			}
			Properties p = new Properties();
			p.load(in);
			return p.getProperty(STJSClass.CLASS_PROP);
		} catch (IOException e) {
			throw new STJSRuntimeException(e);
		} finally {
			if (in != null) {
				Closeables.closeQuietly(in);
			}
		}
	}

	/**
	 * // the format is the one given by stacktrace.js: // <br>
	 * at prototype.method (url) <br>
	 * where url is http://localhost:xxxx/org/stjs/TestClass.js:row:col
	 * 
	 * @param stacktraceLine
	 * @return
	 */
	private StackTraceElement buildStacktraceElement(String stacktraceLine) {

		Matcher m = STACKTRACE_JS_PATTERN.matcher(stacktraceLine);
		if (!m.matches()) {
			// wrong pattern !?
			throw new STJSRuntimeException("Unknown location format:" + stacktraceLine);
		}
		try {

			String methodName = m.group(STACKTRACE_GROUP_METHOD);

			URL url = new URL(m.group(STACKTRACE_GROUP_LOCATION));
			String file = url.getFile();
			String[] fileParts = file.split(":");

			// source file
			String jsSourceFile = fileParts[0];
			String sourceFile = jsSourceFile.replaceAll("\\.js$", ".java");

			// java line
			String cleanJsPath = url.getPath().split(":")[0];
			int jsLineNumber = Integer.valueOf(fileParts[1]);
			int line = getJavaLine(cleanJsPath, jsLineNumber);
			String stjsPropertyFile = cleanJsPath.replaceAll("\\.js$", ".stjs");

			// class name
			String className = getClassName(stjsPropertyFile);
			if (className == null) {
				className = "<Unknown class>";
				sourceFile = jsSourceFile;
			}
			return new StackTraceElement(className, methodName, sourceFile, line);

		} catch (MalformedURLException e) {
			throw new STJSRuntimeException(e);
		}
	}

	/**
	 * the string is in stacktrace.js format.
	 * 
	 * <pre>
	 *  at prototype.method (url)
	 *  at prototype.method (url)
	 *  at prototype.method (url)
	 * </pre>
	 * 
	 * where url is in the form of http://localhost:xxxx/org/stjs/TestClass.js:row:col
	 * 
	 * @param javascriptStacktrace
	 * @return
	 */
	public StackTraceElement[] buildStacktrace(String javascriptStacktrace, String lineSeparator) {
		PreConditions.checkNotNull(javascriptStacktrace);

		String[] lines = javascriptStacktrace.split(lineSeparator);

		// first line is the message
		StackTraceElement[] stackTrace = new StackTraceElement[lines.length];
		for (int i = 0; i < lines.length; ++i) {
			stackTrace[i] = buildStacktraceElement(lines[i]);
		}
		return stackTrace;
	}
}
