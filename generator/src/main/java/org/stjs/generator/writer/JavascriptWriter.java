/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator.writer;

import japa.parser.ast.Node;

import java.io.IOException;
import java.io.Writer;

import org.stjs.generator.GenerationContext;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;

public class JavascriptWriter {
	private static final String NUMERIC_LITERAL_ENDING = "[a-zA-Z]$";

	private static final String INDENT = "    ";
	private int level;

	private boolean indented;

	@SuppressWarnings("PMD.AvoidStringBufferField")
	private final StringBuilder buf = new StringBuilder();

	private final SourceMapGenerator sourceMapGenerator;
	private final boolean generateSourceMap;

	private int currentLine;
	private int currentColumn;

	private FilePosition sourcePosition;
	private FilePosition startOutputPosition;

	public JavascriptWriter(boolean generateSourceMap) {
		sourceMapGenerator = generateSourceMap ? SourceMapGeneratorFactory.getInstance(SourceMapFormat.V3) : null;
		this.generateSourceMap = generateSourceMap;
	}

	public JavascriptWriter indent() {
		level++;
		return this;
	}

	public JavascriptWriter unindent() {
		level--;
		return this;
	}

	private void makeIndent() {
		for (int i = 0; i < level; i++) {
			buf.append(INDENT);
			currentColumn += INDENT.length();
		}
	}

	public JavascriptWriter printLiteral(String value) {
		//
		print(value);
		return this;
	}

	public JavascriptWriter printNumberLiteral(String value) {

		// remove and ending type coercion. i.e 123L -> 123
		print(value.replaceAll(NUMERIC_LITERAL_ENDING, ""));
		return this;
	}

	public JavascriptWriter printStringLiteral(String value) {
		print("\"");
		print(value);
		print("\"");
		return this;
	}

	public JavascriptWriter printCharLiteral(String value) {
		print("'");
		print(value);
		print("'");
		return this;
	}

	public JavascriptWriter print(String arg) {
		if (!indented) {
			makeIndent();
			indented = true;
		}
		buf.append(arg);
		// TODO check for newlines in the string
		currentColumn += arg.length();
		return this;
	}

	public JavascriptWriter printLn(String arg) {
		print(arg);
		printLn();
		return this;
	}

	public JavascriptWriter printLn() {
		buf.append('\n');
		indented = false;
		currentLine++;
		currentColumn = 0;
		return this;
	}

	public String getSource() {
		return buf.toString();
	}

	@Override
	public String toString() {
		return getSource();
	}

	public void setSourceNode(Node n) {
		if (generateSourceMap) {
			sourcePosition = new FilePosition(n.getBeginLine() - 1, n.getBeginColumn() - 1);
			startOutputPosition = new FilePosition(currentLine, currentColumn);
		}
	}

	public void addSouceMapping(GenerationContext context) {
		if (generateSourceMap) {
			FilePosition endOutputPosition = new FilePosition(currentLine, currentColumn);
			sourceMapGenerator.addMapping(context.getInputFile().getName(), null, sourcePosition, startOutputPosition,
					endOutputPosition);
		}
	}

	public void addSourceMapURL(GenerationContext context) {
		if (generateSourceMap) {
			buf.append("//@ sourceMappingURL=").append(context.getInputFile().getName().replaceAll("\\.java$", ".map"));
		}
	}

	public void writeSourceMap(GenerationContext context, Writer sourceMapWriter) throws IOException {
		if (!generateSourceMap) {
			throw new IllegalStateException("Cannot call this method for writer that do not generate source maps");
		}
		sourceMapGenerator.appendTo(sourceMapWriter, context.getInputFile().getName().replaceAll("\\.java$", ".js"));
	}

}
