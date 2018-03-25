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
 * Use this class to build a configuration needed by the {@link org.stjs.generator.Generator}
 *
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * @version $Id: $Id
 */
public class GeneratorConfigurationBuilder {
	private final Collection<String> allowedPackages = new HashSet<String>();
	private final Set<String> allowedJavaLangClasses = new HashSet<String>();
	private final Set<String> annotations = new HashSet<String>();
	private boolean generateArrayHasOwnProperty = true;
	private boolean generateSourceMap;
	private String sourceEncoding = Charset.defaultCharset().name();
	private ClassLoader stjsClassLoader;
	private File targetFolder;
	private GenerationDirectory generationFolder;
	private ClassResolver classResolver;

	/**
	 * <p>Constructor for GeneratorConfigurationBuilder.</p>
	 */
	public GeneratorConfigurationBuilder() {
		// Set a default value for the source encoding.
		sourceEncoding = Charset.defaultCharset().name();
	}

	/**
	 * <p>Constructor for GeneratorConfigurationBuilder.</p>
	 *
	 * @param baseConfig a {@link org.stjs.generator.GeneratorConfiguration} object.
	 */
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
		}
	}

	/**
	 * <p>allowedPackage.</p>
	 *
	 * @param packageName a {@link java.lang.String} object.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder allowedPackage(String packageName) {
		allowedPackages.add(packageName);
		return this;
	}

	/**
	 * <p>allowedJavaLangClasses.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder allowedJavaLangClasses(String className) {
		allowedJavaLangClasses.add(className);
		return this;
	}

	/**
	 * <p>allowedPackages.</p>
	 *
	 * @param packageNames a {@link java.util.Collection} object.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder allowedPackages(Collection<String> packageNames) {
		allowedPackages.addAll(packageNames);
		return this;
	}

	/**
	 * <p>allowedJavaLangClasses.</p>
	 *
	 * @param classNames a {@link java.util.Collection} object.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder allowedJavaLangClasses(Collection<String> classNames) {
		allowedJavaLangClasses.addAll(classNames);
		return this;
	}

	/**
	 * <p>generateArrayHasOwnProperty.</p>
	 *
	 * @param b a boolean.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder generateArrayHasOwnProperty(boolean b) {
		generateArrayHasOwnProperty = b;
		return this;
	}

	/**
	 * <p>generateSourceMap.</p>
	 *
	 * @param b a boolean.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder generateSourceMap(boolean b) {
		generateSourceMap = b;
		return this;
	}

	/**
	 * <p>sourceEncoding.</p>
	 *
	 * @param sourceEncoding a {@link java.lang.String} object.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder sourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
		return this;
	}

	/**
	 * <p>annotations.</p>
	 *
	 * @param annotationNames a {@link java.lang.String} object.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder annotations(String... annotationNames) {
		annotations.addAll(Arrays.asList(annotationNames));
		return this;
	}

	/**
	 * <p>annotations.</p>
	 *
	 * @param annotationNames a {@link java.util.Collection} object.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder annotations(Collection<String> annotationNames) {
		annotations.addAll(annotationNames);
		return this;
	}

	/**
	 * <p>stjsClassLoader.</p>
	 *
	 * @param stjsClassLoader a {@link java.lang.ClassLoader} object.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder stjsClassLoader(ClassLoader stjsClassLoader) {
		this.stjsClassLoader = stjsClassLoader;
		return this;
	}

	/**
	 * <p>targetFolder.</p>
	 *
	 * @param targetFolder a {@link java.io.File} object.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder targetFolder(File targetFolder) {
		this.targetFolder = targetFolder;
		return this;
	}

	/**
	 * <p>generationFolder.</p>
	 *
	 * @param generationFolder a {@link org.stjs.generator.GenerationDirectory} object.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder generationFolder(GenerationDirectory generationFolder) {
		this.generationFolder = generationFolder;
		return this;
	}

	/**
	 * <p>classResolver.</p>
	 *
	 * @param classResolver a {@link org.stjs.generator.ClassResolver} object.
	 * @return a {@link org.stjs.generator.GeneratorConfigurationBuilder} object.
	 */
	public GeneratorConfigurationBuilder classResolver(ClassResolver classResolver) {
		this.classResolver = classResolver;
		return this;
	}

	/**
	 * <p>build.</p>
	 *
	 * @return a {@link org.stjs.generator.GeneratorConfiguration} object.
	 */
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
				classResolver == null ? new DefaultClassResolver(stjsClassLoader) : classResolver //
		);
	}

}
