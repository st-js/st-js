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
	 * Specify where to place generated source files <br>
	 * Default value for war: "${project.build.directory}/${project.build.finalName}/generated-js" <br>
	 * Default value for jar: "${project.build.outputDirectory}"
	 * </p>
	 * 
	 * @parameter
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
		File artifactPath = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName());
		File generatedSourcesDirectoryVar = generatedSourcesDirectory;
		if (generatedSourcesDirectoryVar == null) {
			if (project.getPackaging().equals("jar")) {
				generatedSourcesDirectoryVar = new File(project.getBuild().getOutputDirectory());
			} else {
				generatedSourcesDirectoryVar = new File(artifactPath, "generated-js");
			}
		}
		File baseDir = project.getBasedir();
		File classpath = new File(artifactPath.getAbsolutePath().substring(baseDir.getAbsolutePath().length() + 1));

		File relativeToClasspath = new File("/");
		if (generatedSourcesDirectoryVar.getAbsolutePath().length() > artifactPath.getAbsolutePath().length()) {
			relativeToClasspath = new File(generatedSourcesDirectoryVar.getAbsolutePath().substring(
					artifactPath.getAbsolutePath().length() + 1));
		}
		GenerationDirectory gendir = new GenerationDirectory(generatedSourcesDirectoryVar, classpath,
				relativeToClasspath);
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
