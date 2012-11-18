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
package org.stjs.maven;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * This is the Maven plugin that launches the Javascript generator. The plugin needs a list of packages containing the
 * Java classes that will processed to generate the corresponding Javascript classes. The Javascript files are generated
 * in a configured target folder.
 * 
 * @goal copy-js
 * @phase prepare-package
 * @requiresDependencyResolution compile
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class CopySTJSMojo extends AbstractMojo {

	private static final String JS_PATH_ENTRY = "Javascript-Path";

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;

	/**
	 * @component
	 */
	protected BuildContext buildContext;

	/**
	 * Sets the granularity in milliseconds of the last modification date for testing whether a source needs
	 * recompilation.
	 * 
	 * @parameter expression="${lastModGranularityMs}" default-value="0"
	 */
	protected int staleMillis;

	/**
	 * <p>
	 * Specify where to place generated source files
	 * </p>
	 * 
	 * @parameter default-value="${project.build.directory}/${project.build.finalName}/generated-js"
	 */
	private File generatedSourcesDirectory;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Copying javascript files from dependencies to this artifact");

		try {
			@SuppressWarnings("unchecked")
			List<String> runtimeClasspathElements = project.getCompileClasspathElements();
			for (int i = 0; i < runtimeClasspathElements.size(); i++) {
				String element = runtimeClasspathElements.get(i);
				URL dep = new File(element).toURI().toURL();
				try {
					if (dep.getPath().endsWith(".jar")) {// TODO: is this enough !?
						Manifest manifest = new JarInputStream(dep.openStream()).getManifest();
						String jsDirectoryNames = manifest.getMainAttributes().getValue(JS_PATH_ENTRY);
						if (jsDirectoryNames != null) {
							String[] jsDirectories = jsDirectoryNames.split(";");
							// these are directories relative to the jar's root
							for (String jsDirectory : jsDirectories) {
								URL jsDirectoryURL = new URL("jar:" + dep + "!/" + jsDirectory);
								getLog().info("Copy directory:" + jsDirectoryURL);
								FileCopier.copyResourcesRecursively(jsDirectoryURL, new File(generatedSourcesDirectory,
										jsDirectory));
							}
						}
					}
				} catch (IOException notfound) {
					// skip to the next
				}
			}
		} catch (Exception ex) {
			throw new MojoExecutionException("Cannot get builtProjectClassLoader:" + ex);
		}

	}
}
