package org.stjs.maven;

import java.io.File;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.stjs.generator.GenerationDirectory;

/**
 * 
 * @goal generate
 * @phase process-classes
 * @requiresDependencyResolution compile
 * @author acraciun
 * 
 */
public class MainSTJSMojo extends AbstractSTJSMojo {

	/**
	 * The source directories containing the sources to be compiled.
	 * 
	 * @parameter default-value="${project.compileSourceRoots}"
	 * @required
	 */
	private List<String> compileSourceRoots;

	/**
	 * <p>
	 * Specify where to place generated source files
	 * </p>
	 * 
	 * @parameter default-value="${project.build.directory}/${project.build.finalName}/generated-js"
	 */
	private File generatedSourcesDirectory;

	/**
	 * @parameter default-value="${project.build.outputDirectory}"
	 */
	private File buildOutputDirectory;

	@Override
	public List<String> getCompileSourceRoots() {
		return compileSourceRoots;
	}

	@Override
	public GenerationDirectory getGeneratedSourcesDirectory() {
		File baseDir = project.getBasedir();
		File artifactPath = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName());
		File classpath = new File(artifactPath.getAbsolutePath().substring(baseDir.getAbsolutePath().length() + 1));
		File relativeToClasspath = new File(generatedSourcesDirectory.getAbsolutePath().substring(
				artifactPath.getAbsolutePath().length() + 1));
		GenerationDirectory gendir = new GenerationDirectory(generatedSourcesDirectory, classpath, relativeToClasspath);
		return gendir;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<String> getClasspathElements() throws DependencyResolutionRequiredException {
		return project.getCompileClasspathElements();
	}

	@Override
	protected File getBuildOutputDirectory() {
		return buildOutputDirectory;
	}
}
