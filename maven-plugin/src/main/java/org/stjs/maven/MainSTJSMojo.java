package org.stjs.maven;

import java.io.File;
import java.util.List;

/**
 * 
 * @goal generate
 * @phase process-classes
 * @requiresDependencyResolution provided
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

	@Override
	public List<String> getCompileSourceRoots() {
		return compileSourceRoots;
	}

	@Override
	public File getGeneratedSourcesDirectory() {
		return generatedSourcesDirectory;
	}

}
