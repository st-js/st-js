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
 * @goal generate
 * @phase process-classes
 * @requiresDependencyResolution runtime
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class STJSMavenPlugin extends AbstractMojo {
	private static final String AUTO_ROOT_PACKAGE = "auto";

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The source directories containing the sources to be compiled.
	 * 
	 * @parameter default-value="${project.compileSourceRoots}"
	 * @required
	 * @readonly
	 */
	private List<String> compileSourceRoots;

	/**
	 * The list of packages that can be referenced from the classes that will be processed by the generator
	 * 
	 * @parameter
	 * @readonly
	 */
	private List<String> allowedPackages;

	/**
	 * <p>
	 * Specify where to place generated source files
	 * </p>
	 * 
	 * @parameter default-value="${project.build.directory}/${project.build.finalName}/generated-js"
	 */
	private File generatedSourcesDirectory;

	/**
	 * A list of inclusion filters for the compiler.
	 * 
	 * @parameter
	 */
	private Set<String> includes = new HashSet<String>();

	/**
	 * A list of exclusion filters for the compiler.
	 * 
	 * @parameter
	 */
	private Set<String> excludes = new HashSet<String>();

	/**
	 * Sets the granularity in milliseconds of the last modification date for testing whether a source needs
	 * recompilation.
	 * 
	 * @parameter expression="${lastModGranularityMs}" default-value="0"
	 */
	private int staleMillis;

	/**
	 * 
	 * @return the part of the packages for which the folder will not be generated. For example if the value is
	 *         org.stjs.javascript, for all the classes in the org.stjsjavascript package the javascript files will be
	 *         generated directly in the {@link #generatedSourcesDirectory}. If the value is "auto", it will be used the
	 *         longest package without at least class. If the value is empty (or null) the full packages structure is
	 *         generated
	 * @parameter
	 */
	private String rootPackage = null;

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

	private String getRelativeTarget(String targetRootPath, String sourcePath) {
		if (sourcePath.startsWith(targetRootPath)) {
			return sourcePath.substring(targetRootPath.length());
		}
		return sourcePath;
	}

	public void execute() throws MojoExecutionException {
		getLog().info("Generating javascript files");
		ClassLoader builtProjectClassLoader = getBuiltProjectClassLoader();
		Generator generator = new Generator();
		SourceMapping mapping = new SuffixMapping(".java", ".js");
		GeneratorConfigurationBuilder configBuilder = new GeneratorConfigurationBuilder();
		configBuilder.allowedPackage("org.stjs.javascript");
		if (allowedPackages != null) {
			configBuilder.allowedPackages(allowedPackages);
		}

		String targetRootPackage = rootPackage;
		boolean autoRootPackage = AUTO_ROOT_PACKAGE.equals(targetRootPackage);

		// scan all the packages
		for (String sourceRoot : compileSourceRoots) {
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

		// scan the modified sources
		for (String sourceRoot : compileSourceRoots) {
			File sourceDir = new File(sourceRoot);
			List<File> sources = new ArrayList<File>();
			sources = accumulateSources(sourceDir);
			for (File source : sources) {
				try {
					File absoluteSource = new File(sourceDir, source.getPath());
					File absoluteTarget = (File) mapping
							.getTargetFiles(generatedSourcesDirectory,
									getRelativeTarget(targetRootPath, source.getPath())).iterator().next();
					getLog().info("Generating " + absoluteTarget);
					if (!absoluteTarget.getParentFile().exists() && !absoluteTarget.getParentFile().mkdirs()) {
						getLog().error("Cannot create output directory:" + absoluteTarget.getParentFile());
						continue;
					}
					generator.generateJavascript(builtProjectClassLoader,
							getClassForSource(builtProjectClassLoader, source.getPath()), absoluteSource,
							absoluteTarget, configBuilder.build());
				} catch (InclusionScanException e) {
					throw new MojoExecutionException("Cannot scan the source directory:" + e, e);
				} catch (ClassNotFoundException e) {
					throw new MojoExecutionException("Cannot find corresponding class for [" + source.getPath() + "]:"
							+ e, e);
				} catch (Exception e) {
					getLog().error(e.toString());
					throw new MojoExecutionException("Error generating javascript:" + e, e);
				}
			}
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
	private List<File> accumulateSources(File sourceDir) throws MojoExecutionException {
		final List<File> result = new ArrayList<File>();
		if (sourceDir == null) {
			return result;
		}
		SourceInclusionScanner scanner = getSourceInclusionScanner(staleMillis);
		SourceMapping mapping = new SuffixMapping(".java", ".js");

		scanner.addSourceMapping(mapping);

		final Set<File> staleFiles = new LinkedHashSet<File>();

		for (File f : sourceDir.listFiles()) {
			if (!f.isDirectory()) {
				continue;
			}

			try {
				staleFiles.addAll(scanner.getIncludedSources(f.getParentFile(), generatedSourcesDirectory));
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
