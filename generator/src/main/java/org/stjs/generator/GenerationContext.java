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

/**
 * This class can resolve an identifier or a method in the given source context. There is one context create for each
 * generation process.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class GenerationContext {

	private final File inputFile;

	private final GeneratorConfiguration configuration;

	private ClassOrInterfaceDeclaration currentType = null;

	public GenerationContext(File inputFile, GeneratorConfiguration configuration) {
		this.inputFile = inputFile;
		this.configuration = configuration;
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

}
