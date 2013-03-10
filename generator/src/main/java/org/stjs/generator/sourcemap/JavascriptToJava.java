package org.stjs.generator.sourcemap;

import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.debugging.sourcemap.SourceMapConsumerFactory;
import com.google.debugging.sourcemap.SourceMapping;

public class JavascriptToJava {
	private final ClassLoader classLoader;

	public JavascriptToJava(ClassLoader testClassLoader) {
		this.classLoader = testClassLoader;
	}

	public int getJavaLine(String path, int lineNumber) {
		String sourceMapFile = path.replaceAll("\\.js$", ".map");
		URL url = classLoader.getResource(sourceMapFile.substring(1));
		if (url == null) {
			throw new RuntimeException("SourceMap file [" + sourceMapFile + "] was not found");
		}
		String contents;
		try {
			contents = Resources.toString(url, Charsets.UTF_8);
			SourceMapping mapping = SourceMapConsumerFactory.parse(contents);
			return mapping.getMappingForLine(lineNumber, 1).getLineNumber();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
