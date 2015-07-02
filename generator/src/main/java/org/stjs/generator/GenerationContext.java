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
import java.lang.annotation.Annotation;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import org.stjs.generator.check.Checks;
import org.stjs.generator.javac.AnnotationHelper;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.name.JavaScriptNameProvider;
import org.stjs.generator.visitor.TreePathHolder;

import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

/**
 * This class can resolve an identifier or a method in the given source context. There is one context create for each generation process.
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class GenerationContext<JS> implements TreePathHolder {

	private static final Object NULL = new Object();

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

	private final Map<AnnotationCacheKey, Object> cacheAnnotations;
	private final Map<Tree, TreeWrapper<?, JS>> cacheWrappers = Maps.newIdentityHashMap();
	private final Map<Element, TreeWrapper<?, JS>> cacheWrappersByElement = Maps.newIdentityHashMap();

	public GenerationContext(File inputFile, GeneratorConfiguration configuration, JavaScriptNameProvider names, Trees trees,
			Map<AnnotationCacheKey, Object> cacheAnnotations, JavaScriptBuilder<JS> javaScriptBuilder) {
		this.inputFile = inputFile;
		this.configuration = configuration;
		this.names = names;
		this.trees = trees;
		this.checks = new Checks();
		this.javaScriptBuilder = javaScriptBuilder;
		this.cacheAnnotations = cacheAnnotations;
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
		SourcePosition pos =
				startPos >= 0 ? new SourcePosition(inputFile, (int) compilationUnit.getLineMap().getLineNumber(startPos), (int) compilationUnit
						.getLineMap().getColumnNumber(startPos)) : new SourcePosition(inputFile, 0, 0);
		JavascriptFileGenerationException ex = new JavascriptFileGenerationException(pos, message);
		checks.addError(ex);
		return ex;
	}

	/**
	 * add the position of the Java original node in the generated node.
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

		long endPos = trees.getSourcePositions().getEndPosition(compilationUnit, tree);
		int endLine = endPos >= 0 ? (int) compilationUnit.getLineMap().getLineNumber(endPos) : 0;
		int endColumn = endPos >= 0 ? (int) compilationUnit.getLineMap().getColumnNumber(endPos) : 0;

		return javaScriptBuilder.position(node, line, column, endLine, endColumn);
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
		return wrap(currentPath);
	}

	@SuppressWarnings("unchecked")
	public <T extends Tree> TreeWrapper<T, JS> wrap(TreePath path) {
		TreeWrapper<T, JS> tw = (TreeWrapper<T, JS>) cacheWrappers.get(path.getLeaf());
		if (tw == null) {
			tw = new TreeWrapper<T, JS>(path, this);
			cacheWrappers.put(path.getLeaf(), tw);
		}
		return tw;
	}

	private TreePath getTreePath(Element enclosingElement) {
		TreePath path = trees.getPath(enclosingElement);
		if (path == null) {
			Tree tree = trees.getTree(enclosingElement);
			if (tree == null) {
				tree = new DummyTree();
			}
			// XXX: this is ugly, null is better here?
			path = new TreePath(new TreePath(compilationUnit), tree);
		}
		return path;
	}

	@SuppressWarnings("unchecked")
	public <T extends Tree> TreeWrapper<T, JS> wrap(Element enclosingElement) {
		TreeWrapper<T, JS> tw = (TreeWrapper<T, JS>) cacheWrappersByElement.get(enclosingElement);
		if (tw == null) {
			tw = new TreeWrapper<T, JS>(enclosingElement, getTreePath(enclosingElement), this);
			cacheWrappersByElement.put(enclosingElement, tw);
		}

		return tw;
	}

	public ClassLoader getBuiltProjectClassLoader() {
		return configuration.getStjsClassLoader();
	}

	@SuppressWarnings("unchecked")
	public <T extends Annotation> T getAnnotation(Element element, Class<T> annotationType) {
		AnnotationCacheKey key = new AnnotationCacheKey(annotationType, element);
		Object ret = cacheAnnotations.get(key);
		if (ret != null) {
			return NULL.equals(ret) ? null : (T) ret;
		}
		ret = AnnotationHelper.getAnnotation(elements, element, annotationType);
		if (ret == null) {
			cacheAnnotations.put(key, NULL);
		} else {
			cacheAnnotations.put(key, ret);
		}
		return (T) ret;
	}

	public static class AnnotationCacheKey {
		private final Class<? extends Annotation> annotationType;
		private final Element element;

		public AnnotationCacheKey(Class<? extends Annotation> annotationType, Element element) {
			this.annotationType = annotationType;
			this.element = element;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (annotationType == null ? 0 : annotationType.hashCode());
			result = prime * result + (element == null ? 0 : element.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			AnnotationCacheKey other = (AnnotationCacheKey) obj;
			if (annotationType == null) {
				if (other.annotationType != null) {
					return false;
				}
			} else if (!annotationType.equals(other.annotationType)) {
				return false;
			}
			if (element == null) {
				if (other.element != null) {
					return false;
				}
			} else if (!element.equals(other.element)) {
				return false;
			}
			return true;
		}

	}

	/**
	 * this is a dummy try just to have the wrapper thing working for cases where only the element is available
	 */
	public static class DummyTree implements Tree {
		@Override
		public <R, D> R accept(TreeVisitor<R, D> arg0, D arg1) {
			return null;
		}

		@Override
		public Kind getKind() {
			return Kind.ERRONEOUS;
		}

	}

}
