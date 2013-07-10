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

/**
 * This class is used to provide the different parts of the target directory: the absolute directory (during
 * generation), the part relative to the project's directory used when the classpath for tests is built and the part
 * that is saved within the .stjs files, that should be used later one (for example when this project is used as a
 * dependency - jar/war).<br>
 * The 3 parts are different for the files generated for the main classes compared to the test classes
 * @author acraciun
 */
public class GenerationDirectory {
	private final File absolutePath;
	private final File classpath;
	private final File relativeToClasspath;

	public GenerationDirectory(File absolutePath, File classpath, File relativeToClasspath) {
		this.absolutePath = absolutePath;
		this.classpath = classpath;
		this.relativeToClasspath = relativeToClasspath;
	}

	public File getAbsolutePath() {
		return absolutePath;
	}

	public File getClasspath() {
		return classpath;
	}

	public File getRelativeToClasspath() {
		return relativeToClasspath;
	}

	@Override
	public String toString() {
		return "GenerationDirectory [absolutePath=" + absolutePath + ", classpath=" + classpath
				+ ", relativeToClasspath=" + relativeToClasspath + "]";
	}

}
