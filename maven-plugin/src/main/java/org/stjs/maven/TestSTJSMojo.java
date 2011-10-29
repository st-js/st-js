package org.stjs.maven;

import java.io.File;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * 
 * @goal generate-test
 * @phase process-test-classes
 * @requiresDependencyResolution test
 * @author acraciun
 * 
 */
public class TestSTJSMojo extends AbstractSTJSMojo {
	private static final String MAVEN_TEST_SKIP = "maven.test.skip";

	/**
	 * The source directories containing the sources to be compiled.
	 * 
	 * @parameter default-value="${project.testCompileSourceRoots}"
	 * @required
	 */
	private List<String> compileSourceRoots;

	/**
	 * <p>
	 * Specify where to place generated source files
	 * </p>
	 * 
	 * @parameter default-value="${project.build.directory}/generated-test-js"
	 */
	private File generatedTestSourcesDirectory;

	/**
	 * @parameter default-value="${project.build.testOutputDirectory}"
	 */
	private File buildOutputDirectory;

	@Override
	protected List<String> getCompileSourceRoots() {
		return compileSourceRoots;
	}

	@Override
	protected File getGeneratedSourcesDirectory() {
		return generatedTestSourcesDirectory;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<String> getClasspathElements() throws DependencyResolutionRequiredException {
		return project.getTestClasspathElements();
	}

	@Override
	protected File getBuildOutputDirectory() {
		return buildOutputDirectory;
	}

	@Override
	public void execute() throws MojoExecutionException {
		if ("true".equals(System.getProperty(MAVEN_TEST_SKIP))) {
			getLog().info("Tests are skipped, so javascript generation for tests is also skipped");
		} else {
			super.execute();
		}
	}

}
