package org.stjs.maven;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.stjs.generator.GenerationDirectory;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConstants;

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
	protected GenerationDirectory getGeneratedSourcesDirectory() throws MojoExecutionException {
		try {
			File baseDir = project.getBasedir();
			File classpath = new File(generatedTestSourcesDirectory.getAbsolutePath().substring(
					baseDir.getAbsolutePath().length() + 1));
			URI runtimePath = new URI("classpath:/");

			GenerationDirectory gendir = new GenerationDirectory(generatedTestSourcesDirectory, classpath, runtimePath);
			return gendir;

		}catch(URISyntaxException use){
			throw new MojoExecutionException("Could not generate runtime path");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<String> getClasspathElements() throws DependencyResolutionRequiredException {
		return project.getTestClasspathElements();
	}

	@Override
	protected boolean getCopyStjsSupportFile() {
		return false;
	}

	@Override
	protected File getBuildOutputDirectory() {
		return buildOutputDirectory;
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if ("true".equals(System.getProperty(MAVEN_TEST_SKIP))) {
			getLog().info("Tests are skipped, so javascript generation for tests is also skipped");
		} else {
			super.execute();
		}

	}

	private File getTestTempDirectory() {
		File file = new File(project.getBuild().getDirectory(), GeneratorConstants.STJS_TEST_TEMP_FOLDER);
		file.mkdirs();
		return file;
	}

	@Override
	protected void filesGenerated(Generator generator, GenerationDirectory gendir) throws MojoFailureException,
			MojoExecutionException {
		super.filesGenerated(generator, gendir);
		writeClasspathFolder(gendir);
	}

	@SuppressWarnings("unchecked")
	protected void writeClasspathFolder(GenerationDirectory gendir) throws MojoFailureException {
		File cpFile = new File(getTestTempDirectory(), GeneratorConstants.CLASSPATH_FILE);
		PrintWriter fw = null;
		try {
			fw = new PrintWriter(new FileWriter(cpFile));
			fw.println(gendir.getClasspath().getPath());
			fw.println(project.getBuild().getDirectory() + "/" + project.getBuild().getFinalName());
			for (Artifact art : (Set<Artifact>) project.getArtifacts()) {
				if ("war".equals(art.getType())) {
					fw.println(getWarDirectory(art.getFile()));
				}
			}
		} catch (IOException e) {
			throw new MojoFailureException("Cannot write STJS classpath file " + cpFile, e);
		} finally {
			if (fw != null) {
				fw.close();
			}
		}
	}

	private File getWarDirectory(File warOrTargetClasses) {
		if (warOrTargetClasses.getName().endsWith(".war")) {
			return warOrTargetClasses;
		}
		// when you develop in eclipse, maven would return the target/class of the dependency project, but what's needed
		// is the webapps's folder
		// so look for target/<web-app> folder (i.e. a folder that contains a WEB-INF subfolder)
		File targetFolder = warOrTargetClasses.getParentFile();
		for (File subFolder : targetFolder.listFiles()) {
			if (new File(subFolder, "WEB-INF").exists()) {
				return subFolder;
			}
		}
		// nothing found - maybe a special layout
		return warOrTargetClasses;
	}
}
