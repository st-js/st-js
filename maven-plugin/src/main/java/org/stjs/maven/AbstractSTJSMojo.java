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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;
import org.codehaus.plexus.util.DirectoryScanner;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.type.TypeWrappers;

/**
 * This is the Maven plugin that launches the Javascript generator. The plugin needs a list of packages containing the
 * Java classes that will processed to generate the corresponding Javascript classes. The Javascript files are generated
 * in a configured target folder.
 * 
 * 
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
abstract public class AbstractSTJSMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;

	/**
	 * The list of packages that can be referenced from the classes that will be processed by the generator
	 * 
	 * @parameter
	 */
	protected List<String> allowedPackages;

	/**
	 * A list of inclusion filters for the compiler.
	 * 
	 * @parameter
	 */
	protected Set<String> includes = new HashSet<String>();

	/**
	 * A list of exclusion filters for the compiler.
	 * 
	 * @parameter
	 */
	protected Set<String> excludes = new HashSet<String>();

	/**
	 * Sets the granularity in milliseconds of the last modification date for testing whether a source needs
	 * recompilation.
	 * 
	 * @parameter expression="${lastModGranularityMs}" default-value="0"
	 */
	protected int staleMillis;

	/**
	 * If true the check, if (array.hasOwnProperty(index)) continue; is added in each "for" array iteration
	 * 
	 * @parameter expression="${generateArrayHasOwnProperty}" default-value="true"
	 */
	protected boolean generateArrayHasOwnProperty;

	abstract protected List<String> getCompileSourceRoots();

	abstract protected File getGeneratedSourcesDirectory();

	abstract protected File getBuildOutputDirectory();

	abstract protected List<String> getClasspathElements() throws DependencyResolutionRequiredException;

	private ClassLoader getBuiltProjectClassLoader() throws MojoExecutionException {
		try {
			List<String> runtimeClasspathElements = getClasspathElements();
			URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
			for (int i = 0; i < runtimeClasspathElements.size(); i++) {
				String element = runtimeClasspathElements.get(i);
				getLog().debug("Classpath:" + element);
				runtimeUrls[i] = new File(element).toURI().toURL();
			}
			return new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());
		} catch (Exception ex) {
			throw new MojoExecutionException("Cannot get builtProjectClassLoader");
		}
	}

	@Override
	public void execute() throws MojoExecutionException {
		getLog().info("Generating javascript files");

		// clear cache before each execution
		TypeWrappers.clearCache();

		ClassLoader builtProjectClassLoader = getBuiltProjectClassLoader();
		Generator generator = new Generator();

		GeneratorConfigurationBuilder configBuilder = new GeneratorConfigurationBuilder();
		configBuilder.generateArrayHasOwnProperty(generateArrayHasOwnProperty);
		// configBuilder.allowedPackage("org.stjs.javascript");
		configBuilder.allowedPackage("org.junit");
		// configBuilder.allowedPackage("org.stjs.testing");

		if (allowedPackages != null) {
			configBuilder.allowedPackages(allowedPackages);
		}

		// scan all the packages
		for (String sourceRoot : getCompileSourceRoots()) {
			File sourceDir = new File(sourceRoot);
			Collection<String> packages = accumulatePackages(sourceDir);
			configBuilder.allowedPackages(packages);
		}

		boolean atLeastOneFileGenerated = false;
		// scan the modified sources
		for (String sourceRoot : getCompileSourceRoots()) {
			File sourceDir = new File(sourceRoot);
			List<File> sources = new ArrayList<File>();
			SourceMapping mapping = new SuffixMapping(".java", ".js");
			SourceMapping stjsMapping = new SuffixMapping(".java", ".stjs");
			sources = accumulateSources(sourceDir, mapping, stjsMapping);
			for (File source : sources) {
				try {
					// File absoluteSource = new File(sourceDir, source.getPath());
					File absoluteTarget = (File) mapping
							.getTargetFiles(getGeneratedSourcesDirectory(), source.getPath()).iterator().next();
					getLog().info("Generating " + absoluteTarget);
					if (!absoluteTarget.getParentFile().exists() && !absoluteTarget.getParentFile().mkdirs()) {
						getLog().error("Cannot create output directory:" + absoluteTarget.getParentFile());
						continue;
					}
					String className = getClassNameForSource(source.getPath());
					generator.generateJavascript(builtProjectClassLoader, className, sourceDir,
							getGeneratedSourcesDirectory(), getBuildOutputDirectory(), configBuilder.build());
					atLeastOneFileGenerated = true;

				} catch (InclusionScanException e) {
					throw new MojoExecutionException("Cannot scan the source directory:" + e, e);
				} catch (Exception e) {
					getLog().error(e.toString());
					e.printStackTrace();
					throw new MojoExecutionException("Error generating javascript:" + e, e);
				}
			}
		}

		if (atLeastOneFileGenerated) {
			// copy the javascript support
			generator.copyJavascriptSupport(getGeneratedSourcesDirectory());
		}
	}

	/**
	 * @return the list of Java source files to processed (those which are older than the corresponding Javascript
	 *         file). The returned files are relative to the given source directory.
	 */
	private Collection<String> accumulatePackages(File sourceDir) throws MojoExecutionException {
		final Collection<String> result = new HashSet<String>();
		if (sourceDir == null) {
			return result;
		}

		DirectoryScanner ds = new DirectoryScanner();
		ds.setFollowSymlinks(true);
		ds.addDefaultExcludes();
		ds.setBasedir(sourceDir);
		ds.setIncludes(new String[] { "**/*.java" });
		ds.scan();
		for (String fileName : ds.getIncludedFiles()) {
			File file = new File(fileName);
			result.add(file.getParent().replace(File.separatorChar, '.'));
		}

		/*
		 * // Trim root path from file paths for (File file : staleFiles) { String filePath = file.getPath(); String
		 * basePath = sourceDir.getAbsoluteFile().toString(); result.add(new File(filePath.substring(basePath.length() +
		 * 1))); }
		 */
		return result;
	}

	private String getClassNameForSource(String sourcePath) {
		// remove ending .java and replace / by .
		return sourcePath.substring(0, sourcePath.length() - 5).replace(File.separatorChar, '.');
	}

	/**
	 * @return the list of Java source files to processed (those which are older than the corresponding Javascript
	 *         file). The returned files are relative to the given source directory.
	 */
	@SuppressWarnings("unchecked")
	private List<File> accumulateSources(File sourceDir, SourceMapping jsMapping, SourceMapping stjsMapping)
			throws MojoExecutionException {
		final List<File> result = new ArrayList<File>();
		if (sourceDir == null) {
			return result;
		}
		SourceInclusionScanner jsScanner = getSourceInclusionScanner(staleMillis);
		jsScanner.addSourceMapping(jsMapping);

		SourceInclusionScanner stjsScanner = getSourceInclusionScanner(staleMillis);
		stjsScanner.addSourceMapping(stjsMapping);

		final Set<File> staleFiles = new LinkedHashSet<File>();

		for (File f : sourceDir.listFiles()) {
			if (!f.isDirectory()) {
				continue;
			}

			try {
				staleFiles.addAll(jsScanner.getIncludedSources(f.getParentFile(), getGeneratedSourcesDirectory()));
				staleFiles.addAll(stjsScanner.getIncludedSources(f.getParentFile(), getBuildOutputDirectory()));
			} catch (InclusionScanException e) {
				throw new MojoExecutionException("Error scanning source root: \'" + sourceDir.getPath() + "\' "
						+ "for stale files to recompile.", e);
			}
		}

		// Trim root path from file paths
		for (File file : staleFiles) {
			String filePath = file.getPath();
			String basePath = sourceDir.getAbsoluteFile().toString();
			result.add(new File(filePath.substring(basePath.length() + 1)));
		}
		return result;
	}

	protected SourceInclusionScanner getSourceInclusionScanner(int staleMillis) {
		SourceInclusionScanner scanner;

		if (includes.isEmpty() && excludes.isEmpty()) {
			scanner = new StaleClassSourceScanner(staleMillis, getBuildOutputDirectory());
		} else {
			if (includes.isEmpty()) {
				includes.add("**/*.java");
			}
			scanner = new StaleClassSourceScanner(staleMillis, includes, excludes, getBuildOutputDirectory());
		}

		return scanner;
	}
}
