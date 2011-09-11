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

import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;

import java.io.File;

import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.QualifiedName;

/**
 * This class can resolve an identifier or a method in the given source context. There is one context create for each
 * generation process.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class GenerationContext {

	private boolean skipHandlers = false;

	private final File inputFile;

	private ClassOrInterfaceDeclaration currentType = null;

	private boolean inlineObjectCreation = false;

	public GenerationContext(File inputFile) {
		this.inputFile = inputFile;
	}

	@SuppressWarnings("unchecked")
	public QualifiedName<MethodName> resolveMethod(Node node) {
		// TODO : why not resolving here? what's the point of having an other pass?
		return (QualifiedName<MethodName>) node.getData();
	}

	@SuppressWarnings("unchecked")
	public QualifiedName<IdentifierName> resolveIdentifier(Node node) {
		// TODO : why not resolving here? what's the point of having an other pass?
		return (QualifiedName<IdentifierName>) node.getData();
	}

	public GenerationContext skipHandlers() {
		skipHandlers = true;
		return this;
	}

	public GenerationContext checkHandlers() {
		skipHandlers = false;
		return this;
	}

	public boolean isSkipHandlers() {
		return skipHandlers;
	}

	public File getInputFile() {
		return inputFile;
	}

	public ClassOrInterfaceDeclaration setCurrentType(ClassOrInterfaceDeclaration n) {
		ClassOrInterfaceDeclaration prevType = this.currentType;
		currentType = n;
		return prevType;
	}

	public ClassOrInterfaceDeclaration getCurrentType() {
		return currentType;
	}

	public boolean isInlineObjectCreation() {
		return inlineObjectCreation;
	}

	/**
	 * set true if inside an inline object initialization
	 * 
	 * @param inlineObjectCreation
	 * @return
	 */
	public boolean setInlineObjectCreation(boolean inlineObjectCreation) {
		boolean prev = this.inlineObjectCreation;
		this.inlineObjectCreation = inlineObjectCreation;
		return prev;
	}

}
