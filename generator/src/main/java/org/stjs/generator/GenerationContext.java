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
package org.stjs.generator;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import org.stjs.generator.check.Checks;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.rhino.RhinoJavaScriptBuilder;
import org.stjs.generator.name.JavaScriptNameProvider;
import org.stjs.generator.visitor.TreePathHolder;

import com.google.debugging.sourcemap.SourceMapGenerator;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

/**
 * This class can resolve an identifier or a method in the given source context. There is one context create for each
 * generation process.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class GenerationContext<JS> implements TreePathHolder {

	private final File inputFile;

	private final GeneratorConfiguration configuration;

	private final JavaScriptNameProvider names;

	private Trees trees;

	private Elements elements;

	private Types types;

	private TreePath currentPath;

	private final Checks checks;

	private CompilationUnitTree compilationUnit;

	private final JavaScriptBuilder<JS> javaScriptBuilder;

	private SourceMapGenerator sourceMapGenerator;

	private final ClassLoader builtProjectClassLoader;

	public GenerationContext(File inputFile, GeneratorConfiguration configuration, JavaScriptNameProvider names, Trees trees,
			ClassLoader builtProjectClassLoader) {
		this.inputFile = inputFile;
		this.configuration = configuration;
		this.names = names;
		this.trees = trees;
		this.checks = new Checks();
		this.javaScriptBuilder = (JavaScriptBuilder<JS>) new RhinoJavaScriptBuilder();
		this.builtProjectClassLoader = builtProjectClassLoader;
	}

	public File getInputFile() {
		return inputFile;
	}

	public GeneratorConfiguration getConfiguration() {
		return configuration;
	}

	public JavaScriptNameProvider getNames() {
		return names;
	}

	public Trees getTrees() {
		return trees;
	}

	public void setTrees(Trees trees) {
		this.trees = trees;
	}

	@Override
	public TreePath getCurrentPath() {
		return currentPath;
	}

	@Override
	public void setCurrentPath(TreePath currentPath) {
		this.currentPath = currentPath;
	}

	public Elements getElements() {
		return elements;
	}

	public void setElements(Elements elements) {
		this.elements = elements;
	}

	public Types getTypes() {
		return types;
	}

	public void setTypes(Types types) {
		this.types = types;
	}

	public Checks getChecks() {
		return checks;
	}

	public CompilationUnitTree getCompilationUnit() {
		return compilationUnit;
	}

	public void setCompilationUnit(CompilationUnitTree compilationUnit) {
		this.compilationUnit = compilationUnit;
	}

	public JavascriptFileGenerationException addError(Tree tree, String message) {
		if (compilationUnit == null) {
			return new JavascriptFileGenerationException(new SourcePosition(inputFile, 0, 0), message);
		}
		long startPos = trees.getSourcePositions().getStartPosition(compilationUnit, tree);
		SourcePosition pos = startPos >= 0 ? new SourcePosition(inputFile, (int) compilationUnit.getLineMap().getLineNumber(startPos),
				(int) compilationUnit.getLineMap().getColumnNumber(startPos)) : new SourcePosition(inputFile, 0, 0);
		JavascriptFileGenerationException ex = new JavascriptFileGenerationException(pos, message);
		checks.addError(ex);
		return ex;
	}

	/**
	 * add the position of the Java original node in the generated node.
	 * 
	 * @param tree
	 * @param node
	 * @return
	 */
	public JS withPosition(Tree tree, JS node) {
		if (compilationUnit == null) {
			return node;
		}
		long startPos = trees.getSourcePositions().getStartPosition(compilationUnit, tree);
		int line = startPos >= 0 ? (int) compilationUnit.getLineMap().getLineNumber(startPos) : 0;
		int column = startPos >= 0 ? (int) compilationUnit.getLineMap().getColumnNumber(startPos) : 0;
		return javaScriptBuilder.position(node, line, column);
	}

	/**
	 * @param tree
	 * @return the starting line of the given tree node
	 */
	public int getStartLine(Tree tree) {
		if (compilationUnit == null) {
			return -1;
		}
		long startPos = trees.getSourcePositions().getStartPosition(compilationUnit, tree);
		return (int) (startPos >= 0 ? compilationUnit.getLineMap().getLineNumber(startPos) : 0);
	}

	public JavaScriptBuilder<JS> js() {
		return javaScriptBuilder;
	}

	public void writeJavaScript(JS astRoot, Writer writer) {
		sourceMapGenerator = javaScriptBuilder.writeJavaScript(astRoot, inputFile, configuration.isGenerateSourceMap(), writer);
	}

	public void writeSourceMap(Writer sourceMapWriter) throws IOException {
		// you need to call first writeJavaScript
		if (sourceMapGenerator == null) {
			throw new IllegalStateException("Cannot call this method for writer that do not generate source maps");
		}
		sourceMapGenerator.appendTo(sourceMapWriter, inputFile.getName().replaceAll("\\.java$", ".js"));
	}

	public <T extends Tree> TreeWrapper<T, JS> getCurrentWrapper() {
		// TODO use cache
		return new TreeWrapper<T, JS>(currentPath, this);
	}

	public <T extends Tree> TreeWrapper<T, JS> wrap(TreePath path) {
		// TODO use cache
		return new TreeWrapper<T, JS>(path, this);
	}

	public ClassLoader getBuiltProjectClassLoader() {
		return builtProjectClassLoader;
	}

}
