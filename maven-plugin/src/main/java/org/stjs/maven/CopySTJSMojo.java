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
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * This Maven plugin copies the Javascript (generated or bridged) from dependencies to the final artifact
 *
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * @version $Id: $Id
 */
@Mojo(
		name = "copy-js", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class CopySTJSMojo extends AbstractMojo {

	private static final String STJS_LIBRARY_ENTRY = "STJS-Library";

	@Parameter(
			defaultValue = "${project}", readonly = true, required = true)
	protected MavenProject project;

	/**
	 * Sets the granularity in milliseconds of the last modification date for testing whether a source needs
	 * recompilation.
	 * 
	 */
	@Parameter(
			defaultValue = "0")
	protected int staleMillis;

	/**
	 * <p>
	 * Specify where to place generated source files
	 * </p>
	 * 
	 */
	@Parameter(
			defaultValue = "${project.build.directory}/${project.build.finalName}/generated-js")
	private File generatedSourcesDirectory;

	/** {@inheritDoc} */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Copying javascript files from dependencies to this artifact");

		try {
			FilenameFilter skipClasses = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					name = name.toLowerCase();
					return !name.endsWith(".class");
				}
			};

			@SuppressWarnings("unchecked")
			List<String> runtimeClasspathElements = project.getCompileClasspathElements();
			for (int i = 0; i < runtimeClasspathElements.size(); i++) {
				String element = runtimeClasspathElements.get(i);
				URL dep = new File(element).toURI().toURL();
				try {
					if (dep.getPath().endsWith(".jar")) {// TODO: is this enough !?
						Manifest manifest = new JarInputStream(dep.openStream()).getManifest();
						if (manifest != null) {
							String isStjsLibrary = manifest.getMainAttributes().getValue(STJS_LIBRARY_ENTRY);
							if ("true".equals(isStjsLibrary)) {
								// these are directories relative to the jar's root
								URL jsDirectoryURL = new URL("jar:" + dep + "!/");
								getLog().info("Copy directory:" + jsDirectoryURL);
								FileCopier.copyResourcesRecursively(jsDirectoryURL, generatedSourcesDirectory, skipClasses);
							}
						}
					}
				}
				catch (IOException notfound) {
					// skip to the next
				}
			}
		}
		catch (Exception ex) {
			throw new MojoExecutionException("Cannot get builtProjectClassLoader:" + ex, ex);
		}

	}
}
