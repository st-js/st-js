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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;
import org.codehaus.plexus.util.DirectoryScanner;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfigurationBuilder;

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
	private static final String AUTO_ROOT_PACKAGE = "auto";

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
	 * 
	 * @return the part of the packages for which the folder will not be generated. For example if the value is
	 *         org.stjs.javascript, for all the classes in the org.stjsjavascript package the javascript files will be
	 *         generated directly in the {@link #generatedSourcesDirectory}. If the value is "auto", it will be used the
	 *         longest package without at least class. If the value is empty (or null) the full packages structure is
	 *         generated
	 * @parameter
	 */
	protected String rootPackage = null;

	abstract List<String> getCompileSourceRoots();

	abstract File getGeneratedSourcesDirectory();

	private ClassLoader getBuiltProjectClassLoader() throws MojoExecutionException {
		try {
			@SuppressWarnings("unchecked")
			List<String> runtimeClasspathElements = project.getRuntimeClasspathElements();
			URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
			for (int i = 0; i < runtimeClasspathElements.size(); i++) {
				String element = runtimeClasspathElements.get(i);
				runtimeUrls[i] = new File(element).toURI().toURL();
			}
			return new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());
		} catch (Exception ex) {
			throw new MojoExecutionException("Cannot get builtProjectClassLoader");
		}
	}

	public void execute() throws MojoExecutionException {
		getLog().info("Generating javascript files");

		ClassLoader builtProjectClassLoader = getBuiltProjectClassLoader();
		Generator generator = new Generator();

		GeneratorConfigurationBuilder configBuilder = new GeneratorConfigurationBuilder();
		// TODO use config to add this package
		configBuilder.allowedPackage("org.stjs.javascript");
		configBuilder.allowedPackage("org.junit");
		configBuilder.allowedPackage("org.stjs.testing");

		if (allowedPackages != null) {
			configBuilder.allowedPackages(allowedPackages);
		}

		String targetRootPackage = rootPackage;
		boolean autoRootPackage = AUTO_ROOT_PACKAGE.equals(targetRootPackage);

		// scan all the packages
		for (String sourceRoot : getCompileSourceRoots()) {
			File sourceDir = new File(sourceRoot);
			Collection<String> packages = accumulatePackages(sourceDir);
			configBuilder.allowedPackages(packages);
			if (autoRootPackage) {
				for (String pack : packages) {
					if (targetRootPackage.equals(AUTO_ROOT_PACKAGE)) {
						targetRootPackage = pack;
					} else {
						targetRootPackage = maximumCommonPackage(targetRootPackage, pack);
					}
				}
			}
		}

		String targetRootPath = targetRootPackage != null ? targetRootPackage.replace('.', File.separatorChar) : "";

		boolean atLeastOneFileGenerated = false;
		// scan the modified sources
		for (String sourceRoot : getCompileSourceRoots()) {
			File sourceDir = new File(sourceRoot);
			List<File> sources = new ArrayList<File>();
			SourceMapping mapping = targetRootPackage != null ? new SuffixMappingWithCompressedTarget(targetRootPath,
					".java", ".js") : new SuffixMapping(".java", ".js");
			sources = accumulateSources(sourceDir, mapping);
			for (File source : sources) {
				try {
					File absoluteSource = new File(sourceDir, source.getPath());
					File absoluteTarget = (File) mapping
							.getTargetFiles(getGeneratedSourcesDirectory(), source.getPath()).iterator().next();
					getLog().info("Generating " + absoluteTarget);
					if (!absoluteTarget.getParentFile().exists() && !absoluteTarget.getParentFile().mkdirs()) {
						getLog().error("Cannot create output directory:" + absoluteTarget.getParentFile());
						continue;
					}
					generator.generateJavascript(builtProjectClassLoader, absoluteSource, absoluteTarget,
							configBuilder.build());
					atLeastOneFileGenerated = true;

					/*
					 * NodeJSExecutor executor = new NodeJSExecutor(); ExecutionResult execution =
					 * executor.run(absoluteTarget); if (execution.getExitValue() == 0) {
					 * getLog().info("NodeJS execution sucessfull"); } else {
					 * getLog().error("Error in NodeJS execution "+execution.getStdOut()+execution.getStdErr()); }
					 */

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

	private String maximumCommonPackage(String p1, String p2) {
		String[] items1 = p1.split("\\.");
		String[] items2 = p2.split("\\.");
		int common = -1;
		for (int i = 0; i < items1.length && i < items2.length; ++i) {
			if (!items1[i].equals(items2[i])) {
				break;
			}
			common = i;
		}
		StringBuilder commonPackage = new StringBuilder();
		for (int i = 0; i < common; ++i) {
			if (i > 0) {
				commonPackage.append('.');
			}
			commonPackage.append(items1[i]);
		}
		return commonPackage.toString();
	}

	private Class<?> getClassForSource(ClassLoader builtProjectClassLoader, String sourcePath)
			throws ClassNotFoundException {
		// remove ending .java and replace / by .
		String className = sourcePath.substring(0, sourcePath.length() - 5).replace(File.separatorChar, '.');
		return builtProjectClassLoader.loadClass(className);
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

	/**
	 * @return the list of Java source files to processed (those which are older than the corresponding Javascript
	 *         file). The returned files are relative to the given source directory.
	 */
	@SuppressWarnings("unchecked")
	private List<File> accumulateSources(File sourceDir, SourceMapping mapping) throws MojoExecutionException {
		final List<File> result = new ArrayList<File>();
		if (sourceDir == null) {
			return result;
		}
		SourceInclusionScanner scanner = getSourceInclusionScanner(staleMillis);

		scanner.addSourceMapping(mapping);

		final Set<File> staleFiles = new LinkedHashSet<File>();

		for (File f : sourceDir.listFiles()) {
			if (!f.isDirectory()) {
				continue;
			}

			try {
				staleFiles.addAll(scanner.getIncludedSources(f.getParentFile(), getGeneratedSourcesDirectory()));
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
			scanner = new StaleSourceScanner(staleMillis);
		} else {
			if (includes.isEmpty()) {
				includes.add("**/*.java");
			}
			scanner = new StaleSourceScanner(staleMillis, includes, excludes);
		}

		return scanner;
	}
}
