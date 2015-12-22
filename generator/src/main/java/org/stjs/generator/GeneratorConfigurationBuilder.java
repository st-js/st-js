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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Use this class to build a configuration needed by the {@link Generator}
 *
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class GeneratorConfigurationBuilder {
	private static final Logger LOG = Logger.getLogger(Generator.class.getName());
	private static final String CONFIG_PROPERTIES_RESOURCES_FILENAME = "config.properties";
	private static final String CONFIG_PROPERTIES_SPLIT_REGEX = "\\s*,\\s*";
	private static final String CONFIG_PROPERTIES_MAP_SPLIT_REGEX = "\\s*:\\s*";
	private static final String ALLOWED_JAVA_LANG_CLASSES_CONFIG_KEY = "allowedJavaLangClasses";
	private static final String ALLOWED_PACKAGES_CONFIG_KEY = "allowedPackages";
	private static final String RENAMED_METHOD_SIGNATURES_CONFIG_KEY = "renamedMethodSignatures";
	private static final String NAMESPACES_CONFIG_KEY = "namespaces";
	private static final String FORBIDDEN_METHOD_INVOCATIONS_CONFIG_KEY = "forbiddenMethodInvocations";
	private final Collection<String> allowedPackages = new HashSet<>();
	private final Collection<String> allowedJavaLangClasses = new HashSet<>();
	private final Collection<String> annotations = new HashSet<>();
	private final Collection<String> forbiddenMethodInvocations = new HashSet<>();
	private final Map<String, String> namespaces = new HashMap<>();
	private final Map<String, String> renamedMethodSignatures = new HashMap<>();
	private boolean generateArrayHasOwnProperty = true;
	private boolean generateSourceMap;
	private String sourceEncoding = Charset.defaultCharset().name();
	private boolean isSynchronizedAllowed = true;
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
			forbiddenMethodInvocations(baseConfig.getForbiddenMethodInvocations());
			namespaces(baseConfig.getNamespaces());
			annotations(baseConfig.getAnnotations());
			renamedMethodSignatures(baseConfig.getRenamedMethodSignatures());
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

	public GeneratorConfigurationBuilder allowedJavaLangClass(String className) {
		allowedJavaLangClasses.add(className);
		return this;
	}

	public GeneratorConfigurationBuilder forbiddenMethodInvocation(String methodInvocation) {
		forbiddenMethodInvocations.add(methodInvocation);
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

	public GeneratorConfigurationBuilder forbiddenMethodInvocations(Collection<String> methodInvocations) {
		forbiddenMethodInvocations.addAll(methodInvocations);
		return this;
	}

	public GeneratorConfigurationBuilder namespaces(Map<String, String> namespacesMap) {
		namespaces.putAll(namespacesMap);
		return this;
	}

	public GeneratorConfigurationBuilder renamedMethodSignatures(Map<String, String> renamedMethodSignaturesMap) {
		renamedMethodSignatures.putAll(renamedMethodSignaturesMap);
		return this;
	}

	public GeneratorConfigurationBuilder annotations(Collection<String> annotationNames) {
		annotations.addAll(annotationNames);
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
		try {
			buildFromConfigFile();
		}
		catch (IOException e) {
			LOG.log(Level.SEVERE, "IOException should not have been thrown.", e);
		}

		return new GeneratorConfiguration(//
				allowedPackages,  //
				allowedJavaLangClasses, //
				forbiddenMethodInvocations, //
				namespaces, //
				renamedMethodSignatures, //
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

	private void buildFromConfigFile() throws IOException {
		Properties prop = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_PROPERTIES_RESOURCES_FILENAME);

			if (inputStream == null) {
				throw new FileNotFoundException("property file '" + CONFIG_PROPERTIES_RESOURCES_FILENAME + "' not found in the resources.");
			}
			prop.load(inputStream);

			String allowedJavaLangClassesConfig = prop.getProperty(ALLOWED_JAVA_LANG_CLASSES_CONFIG_KEY);
			String allowedPackagesConfig = prop.getProperty(ALLOWED_PACKAGES_CONFIG_KEY);
			String forbiddenMethodInvocationsConfig = prop.getProperty(FORBIDDEN_METHOD_INVOCATIONS_CONFIG_KEY);
			String namespacesConfig = prop.getProperty(NAMESPACES_CONFIG_KEY);
			String renamedMethodSignaturesConfig = prop.getProperty(RENAMED_METHOD_SIGNATURES_CONFIG_KEY);

			readConfigKey(allowedJavaLangClassesConfig, allowedJavaLangClasses);
			readConfigKey(allowedPackagesConfig, allowedPackages);
			readConfigKey(forbiddenMethodInvocationsConfig, forbiddenMethodInvocations);
			readConfigKeyAsMap(namespacesConfig, namespaces);
			readConfigKeyAsMap(renamedMethodSignaturesConfig, renamedMethodSignatures);
		}
		finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	private void readConfigKeyAsMap(String config, Map<String, String> map) {
		if (config != null && !config.isEmpty()) {
			Map<String, String> namespacesMap = processConfigKeyAsMap(config);
			map.putAll(namespacesMap);
		}
	}

	private void readConfigKey(String config, Collection<String> collection) {
		if (config != null && !config.isEmpty()) {
			collection.addAll(Arrays.asList(config.split(CONFIG_PROPERTIES_SPLIT_REGEX)));
        }
	}

	private Map<String, String> processConfigKeyAsMap(String config) {
		Map<String, String> map = new HashMap<>();
		String[] pairs = config.split(CONFIG_PROPERTIES_SPLIT_REGEX);
		for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(CONFIG_PROPERTIES_MAP_SPLIT_REGEX);
			map.put(keyValue[0], keyValue[1]);
        }
		return map;
	}
}
