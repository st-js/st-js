package org.stjs.maven;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
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
	 * Specify where to place generated source files. Cannot be set with &lt;webjar&gt;true&lt;/webjar&gt; <br>
	 * Default value for war: "${project.build.directory}/${project.build.finalName}/generated-js" <br>
	 * Default value for jar: "${project.build.outputDirectory}"
	 * Default value for webjar: "${project.build.outputDirectory}/META-INF/resources/webjar/${project.artifactId}/${project.version}"
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
	 *
	 * The runtime will not be copied if the "webjar" setting is set to "true"
	 *
	 * @parameter default-value="true"
	 */
	private boolean includeStjsSupportFile;

	/**
	 * Sets up ST-JS to generate webjar compliant jars. This option is only compatible with jar packaging and will not
	 * work if you have explicitly set the value of generatedSourcesDirectory.
	 *
	 * @parameter default-value="false"
	 */
	private boolean webjar;

	@Override
	public List<String> getCompileSourceRoots() {
		return compileSourceRoots;
	}

	@Override
	public GenerationDirectory getGeneratedSourcesDirectory() throws MojoExecutionException {

		if (webjar) {
			if (!project.getPackaging().equals("jar")) {
				throw new MojoExecutionException("<webjar>true</webjar> is only compatible with <packaging>jar</packaging>");

			} else if (generatedSourcesDirectory != null) {
				throw new MojoExecutionException("Cannot set <generatedSourcesDirectory> when <webjar>true</webjar> is set");
			}
		}

		Path generatedSourcesPath;
		if (generatedSourcesDirectory == null) {
			if (webjar) {
				// default path for webjar packaging
				generatedSourcesPath = Paths.get( //
						project.getBuild().getOutputDirectory(), //
						"META-INF", //
						"resources", //
						"webjars", //
						project.getArtifactId(), //
						project.getVersion() //
				);

			} else if (project.getPackaging().equals("jar")) {
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

		URI generatedSourcesRuntimePath;
		try {
			if (webjar) {
				// webjar packaging
				generatedSourcesRuntimePath = new URI("webjar:/");

			} else if (project.getPackaging().equals("jar")) {
				// .jar packaging
				Path outputDir = Paths.get(project.getBuild().getOutputDirectory()).toAbsolutePath();
				Path classpathOuputDir = outputDir.relativize(generatedSourcesPath);
				generatedSourcesRuntimePath = new URI("classpath:/").resolve(classpathOuputDir.toString());

			} else {
				// .war packaging
				Path artifactPath = Paths.get(project.getBuild().getDirectory(), project.getBuild().getFinalName());
				String jsPath = artifactPath.relativize(generatedSourcesPath).toString();
				if(!jsPath.startsWith("/")){
					jsPath = "/" + jsPath;
				}
				if(!jsPath.endsWith("/")){
					jsPath = jsPath + "/";
				}
				generatedSourcesRuntimePath = new URI(jsPath);
			}
		}
		catch (URISyntaxException use) {
			throw new MojoExecutionException("Unable to generate runtime path", use);
		}

		GenerationDirectory gendir = new GenerationDirectory( //
				generatedSourcesPath.toFile(), //
				null, //
				generatedSourcesRuntimePath //
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
		return !webjar && includeStjsSupportFile;
	}

	@Override
	protected File getBuildOutputDirectory() {
		return buildOutputDirectory;
	}
}
