package org.stjs.maven;

import java.io.File;
import java.util.List;

/**
 * 
 * @goal generate-test
 * @phase process-test-classes
 * @requiresDependencyResolution runtime+test
 * @author acraciun
 * 
 */
public class TestSTJSMojo extends AbstractSTJSMojo {
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

	@Override
	public List<String> getCompileSourceRoots() {
		return compileSourceRoots;
	}

	@Override
	public File getGeneratedSourcesDirectory() {
		return generatedTestSourcesDirectory;
	}
}
