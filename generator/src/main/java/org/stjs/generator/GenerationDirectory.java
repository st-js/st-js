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

/**
 * This class is used to provide the different parts of the target directory:
 * <ul>
 * <li>
 *     the absolute path to the directory in which the generated .js source files are placed (during generation)
 * </li>
 * <li>
 *     the part relative to the project's directory used when the classpath for tests is built
 * </li>
 * <li>
 *     the path in the classpath of the final artifact in which the generated .js source files are placed. That path can then be used
 *     to load the generated .js files using Class.getResource() or ClassLoader.getResource(). That path is saved within the .stjs files
 *     and will be used later on, for example when this project is used as a dependency - jar/war
 * </li>
 * </ul>
 * The 3 parts are different for the files generated for the main classes compared to the test classes
 * @author acraciun
 */
public class GenerationDirectory {
	private final File generatedSourcesAbsolutePath;
	private final File generatedSourcesPathInClasspath;
	private final File classpath;

	public GenerationDirectory(File generatedSourcesAbsolutePath, File classpath, File generatedSourcesPathInClasspath) {
		this.generatedSourcesAbsolutePath = generatedSourcesAbsolutePath;
		this.generatedSourcesPathInClasspath = generatedSourcesPathInClasspath;
		this.classpath = classpath;
	}

	public File getGeneratedSourcesAbsolutePath() {
		return generatedSourcesAbsolutePath;
	}

	public File getGeneratedSourcesPathInClasspath() {
		return generatedSourcesPathInClasspath;
	}

	public File getClasspath() {
		return classpath;
	}

	@Override
	public String toString() {
		return "GenerationDirectory [generatedSourcesAbsolutePath=" + generatedSourcesAbsolutePath + ", classpath=" + classpath
				+ ", generatedSourcesPathInClasspath=" + generatedSourcesPathInClasspath
				+ "]";
	}

}
