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

import japa.parser.ast.body.ClassOrInterfaceDeclaration;

import java.io.File;

import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import org.stjs.generator.check.Checks;
import org.stjs.generator.name.JavaScriptNameProvider;
import org.stjs.generator.visitor.TreePathHolder;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

/**
 * This class can resolve an identifier or a method in the given source context. There is one context create for each
 * generation process.
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class GenerationContext implements TreePathHolder {

	private final File inputFile;

	private final GeneratorConfiguration configuration;

	private final JavaScriptNameProvider names;

	private ClassOrInterfaceDeclaration currentType;

	private Trees trees;

	private Elements elements;

	private Types types;

	private TreePath currentPath;

	private final Checks checks;

	public GenerationContext(File inputFile, GeneratorConfiguration configuration, JavaScriptNameProvider names, Trees trees) {
		this.inputFile = inputFile;
		this.configuration = configuration;
		this.names = names;
		this.trees = trees;
		this.checks = new Checks();
	}

	public File getInputFile() {
		return inputFile;
	}

	public GeneratorConfiguration getConfiguration() {
		return configuration;
	}

	public ClassOrInterfaceDeclaration setCurrentType(ClassOrInterfaceDeclaration n) {
		ClassOrInterfaceDeclaration prevType = this.currentType;
		currentType = n;
		return prevType;
	}

	public ClassOrInterfaceDeclaration getCurrentType() {
		return currentType;
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

	public TreePath getCurrentPath() {
		return currentPath;
	}

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

}
