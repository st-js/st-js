package org.stjs.maven;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.stjs.generator.GenerationDirectory;

/**
 * @author acraciun
 * @goal generate
 * @phase process-classes
 * @requiresDependencyResolution compile
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

	/**
	 * Specifies if the ST-JS runtime support file (stjs.js) must be included in your build output. Default value is true.
	 * @parameter default-value="true"
	 */
	private boolean includeStjsSupportFile;

	@Override
	public List<String> getCompileSourceRoots() {
		return compileSourceRoots;
	}

	@Override
	public GenerationDirectory getGeneratedSourcesDirectory() {

		Path generatedSourcesPath;
		if (generatedSourcesDirectory == null) {
			if (project.getPackaging().equals("jar")) {
				// default path for .jar packaging
				generatedSourcesPath = Paths.get(project.getBuild().getOutputDirectory());
			} else {
				// default path for .war packaging
				generatedSourcesPath = Paths.get(project.getBuild().getDirectory(), project.getBuild().getFinalName(), "generated-js");
			}

		} else {
			// user specified path
			generatedSourcesPath = generatedSourcesDirectory.toPath();
		}
		generatedSourcesPath = generatedSourcesPath.toAbsolutePath();

		Path generatedSourcesPathInClasspath;
		if (project.getPackaging().equals("jar")) {
			// .jar packaging
			Path outputDir = Paths.get(project.getBuild().getOutputDirectory()).toAbsolutePath();
			generatedSourcesPathInClasspath = outputDir.relativize(generatedSourcesPath);

		} else {
			// .war packaging
			Path artifactPath = Paths.get(project.getBuild().getDirectory(), project.getBuild().getFinalName());
			generatedSourcesPathInClasspath = artifactPath.relativize(generatedSourcesPath);
		}

		GenerationDirectory gendir = new GenerationDirectory( //
				generatedSourcesPath.toFile(), //
				null, //
				generatedSourcesPathInClasspath.toFile() //
		);
		return gendir;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<String> getClasspathElements() throws DependencyResolutionRequiredException {
		return project.getCompileClasspathElements();
	}

	@Override
	protected boolean getCopyStjsSupportFile() {
		return this.includeStjsSupportFile;
	}

	@Override
	protected File getBuildOutputDirectory() {
		return buildOutputDirectory;
	}
}
