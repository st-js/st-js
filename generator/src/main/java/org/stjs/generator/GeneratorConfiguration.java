/**
 * Copyright 2011 Alexandru Craciun, Eyal Kaspi
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.stjs.generator;

import java.io.File;
import java.util.Collection;
import java.util.Set;

public class GeneratorConfiguration {
	private final Collection<String> allowedPackages;
	private final Set<String> allowedJavaLangClasses;
	private final boolean generateArrayHasOwnProperty;
	private final boolean generateSourceMap;
	private final String sourceEncoding;
	private final Set<String> annotations;
	private final ClassLoader stjsClassLoader;
	private final File targetFolder;
	private final GenerationDirectory generationFolder;
	private final ClassResolver classResolver;
	private final boolean isSynchronizedAllowed;

	// We actually have a builder for that, so the number of parameters warning doesn't apply
	@SuppressWarnings("PMD.ExcessiveParameterList")
	GeneratorConfiguration(Collection<String> allowedPackages, Set<String> allowedJavaLangClasses, boolean generateArrayHasOwnProperty,
			boolean generateSourceMap, String sourceEncoding, Set<String> annotations, ClassLoader stjsClassLoader, File targetFolder,
			GenerationDirectory generationFolder, ClassResolver classResolver, boolean isSynchronizedAllowed) {
		this.allowedPackages = allowedPackages;
		this.allowedJavaLangClasses = allowedJavaLangClasses;
		this.generateArrayHasOwnProperty = generateArrayHasOwnProperty;
		this.generateSourceMap = generateSourceMap;
		this.sourceEncoding = sourceEncoding;
		this.annotations = annotations;
		this.stjsClassLoader = stjsClassLoader;
		this.targetFolder = targetFolder;
		this.generationFolder = generationFolder;
		this.classResolver = classResolver;
		this.isSynchronizedAllowed = isSynchronizedAllowed;
	}

	/**
	 * @return the parent packages that contain the classes that can be called from the processed source file. Note that sub-packages of a
	 *         package from this collection are also allowed. java.lang is implicit
	 */
	public Collection<String> getAllowedPackages() {
		return allowedPackages;
	}

	public Set<String> getAllowedJavaLangClasses() {
		return allowedJavaLangClasses;
	}

	public boolean isGenerateArrayHasOwnProperty() {
		return generateArrayHasOwnProperty;
	}

	public boolean isGenerateSourceMap() {
		return generateSourceMap;
	}

	public String getSourceEncoding() {
		return sourceEncoding;
	}

	/**
	 * these are annotations to be generated
	 * @return
	 */
	public Set<String> getAnnotations() {
		return annotations;
	}

	public ClassLoader getStjsClassLoader() {
		return stjsClassLoader;
	}

	public GenerationDirectory getGenerationFolder() {
		return generationFolder;
	}

	public File getTargetFolder() {
		return targetFolder;
	}

	public ClassResolver getClassResolver() {
		return classResolver;
	}

	public boolean isSynchronizedAllowed() {
		return isSynchronizedAllowed;
	}
}
