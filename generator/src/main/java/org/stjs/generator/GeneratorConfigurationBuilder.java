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
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Use this class to build a configuration needed by the {@link Generator}
 *
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class GeneratorConfigurationBuilder {
	private final Collection<String> allowedPackages = new HashSet<String>();
	private final Set<String> allowedJavaLangClasses = new HashSet<String>();
	private final Set<String> annotations = new HashSet<String>();
	private boolean generateArrayHasOwnProperty = true;
	private boolean generateSourceMap;
	private String sourceEncoding = Charset.defaultCharset().name();
	private boolean isSynchronizedAllowed = false;
	private ClassLoader stjsClassLoader;
	private File targetFolder;
	private GenerationDirectory generationFolder;
	private ClassResolver classResolver;

	public GeneratorConfigurationBuilder() {
		// Set a default value for the source encoding.
		sourceEncoding = Charset.defaultCharset().name();
	}

	public GeneratorConfigurationBuilder(GeneratorConfiguration baseConfig) {
		this();
		if (baseConfig != null) {
			allowedPackages(baseConfig.getAllowedPackages());
			allowedJavaLangClasses(baseConfig.getAllowedJavaLangClasses());
			annotations(baseConfig.getAnnotations());
			generateArrayHasOwnProperty(baseConfig.isGenerateArrayHasOwnProperty());
			generateSourceMap(baseConfig.isGenerateSourceMap());
			sourceEncoding(baseConfig.getSourceEncoding());
			stjsClassLoader(baseConfig.getStjsClassLoader());
			targetFolder(baseConfig.getTargetFolder());
			generationFolder(baseConfig.getGenerationFolder());
			classResolver(baseConfig.getClassResolver());
			setSynchronizedAllowed(baseConfig.isSynchronizedAllowed());
		}
	}

	public GeneratorConfigurationBuilder allowedPackage(String packageName) {
		allowedPackages.add(packageName);
		return this;
	}

	public GeneratorConfigurationBuilder allowedJavaLangClasses(String className) {
		allowedJavaLangClasses.add(className);
		return this;
	}

	public GeneratorConfigurationBuilder allowedPackages(Collection<String> packageNames) {
		allowedPackages.addAll(packageNames);
		return this;
	}

	public GeneratorConfigurationBuilder allowedJavaLangClasses(Collection<String> classNames) {
		allowedJavaLangClasses.addAll(classNames);
		return this;
	}

	public GeneratorConfigurationBuilder generateArrayHasOwnProperty(boolean b) {
		generateArrayHasOwnProperty = b;
		return this;
	}

	public GeneratorConfigurationBuilder generateSourceMap(boolean b) {
		generateSourceMap = b;
		return this;
	}

	public GeneratorConfigurationBuilder sourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
		return this;
	}

	public GeneratorConfigurationBuilder annotations(String... annotationNames) {
		annotations.addAll(Arrays.asList(annotationNames));
		return this;
	}

	public GeneratorConfigurationBuilder annotations(Collection<String> annotationNames) {
		annotations.addAll(annotationNames);
		return this;
	}

	public GeneratorConfigurationBuilder stjsClassLoader(ClassLoader stjsClassLoader) {
		this.stjsClassLoader = stjsClassLoader;
		return this;
	}

	public GeneratorConfigurationBuilder targetFolder(File targetFolder) {
		this.targetFolder = targetFolder;
		return this;
	}

	public GeneratorConfigurationBuilder generationFolder(GenerationDirectory generationFolder) {
		this.generationFolder = generationFolder;
		return this;
	}

	public GeneratorConfigurationBuilder classResolver(ClassResolver classResolver) {
		this.classResolver = classResolver;
		return this;
	}

	public GeneratorConfigurationBuilder setSynchronizedAllowed(boolean synchronizedAllowed) {
		this.isSynchronizedAllowed = synchronizedAllowed;
		return this;
	}


	public GeneratorConfiguration build() {
		allowedJavaLangClasses.add("Object");
		allowedJavaLangClasses.add("Class");
		allowedJavaLangClasses.add("String");
		allowedJavaLangClasses.add("Number");
		allowedJavaLangClasses.add("Double");
		allowedJavaLangClasses.add("Float");
		allowedJavaLangClasses.add("Long");
		allowedJavaLangClasses.add("Integer");
		allowedJavaLangClasses.add("Short");
		allowedJavaLangClasses.add("Boolean");
		allowedJavaLangClasses.add("Character");
		allowedJavaLangClasses.add("Byte");
		allowedJavaLangClasses.add("Void");
		allowedJavaLangClasses.add("Math");

		allowedJavaLangClasses.add("Throwable");
		allowedJavaLangClasses.add("Exception");
		allowedJavaLangClasses.add("RuntimeException");

		allowedJavaLangClasses.add("Iterable");

		allowedPackages.add("java.lang");

		allowedPackage(Iterator.class.getName());

		return new GeneratorConfiguration(//
				allowedPackages,  //
				allowedJavaLangClasses, //
				generateArrayHasOwnProperty, //
				generateSourceMap, //
				sourceEncoding,  //
				annotations,  //
				stjsClassLoader,  //
				targetFolder,  //
				generationFolder, //
				classResolver == null ? new DefaultClassResolver(stjsClassLoader) : classResolver, //
				isSynchronizedAllowed //
		);
	}

}
