package org.stjs.command.line;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.stjs.generator.GenerationDirectory;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfiguration;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.STJSRuntimeException;

import com.google.common.base.Throwables;

public class CommandLine {

	public static void main(String[] args) throws URISyntaxException, IOException, ClassNotFoundException {
		if (args.length != 2) {
			System.err.println("Usage: CommandLine <srcDir> <classQualifiedName>");
			return;
		}
		String path = args[0];
		String fileName = args[1];
		File sourceFile = new File(path + "/" + fileName.replaceAll("\\.", "/") + ".java");
		compile(path, Collections.singletonList(sourceFile), Collections.<File>emptyList());
		generate(path, fileName, Collections.<File>emptyList(), path);
	}

	static void generate(final String path, final String className, List<File> dependencies, String outputDir) {
		Generator gen = new Generator();
		try {
			List<URL> classpathElements = new ArrayList<URL>();
			classpathElements.add(new File(path).toURI().toURL());
			for (File dep : dependencies) {
				classpathElements.add(dep.toURI().toURL());
			}

			ClassLoader builtProjectClassLoader = new URLClassLoader(classpathElements.toArray(new URL[classpathElements.size()]), Thread
					.currentThread().getContextClassLoader());
			File sourceFolder = new File(path);
			GenerationDirectory targetFolder = new GenerationDirectory(new File(outputDir), null, null);
			File generationFolder = targetFolder.getGeneratedSourcesAbsolutePath();

			GeneratorConfigurationBuilder configBuilder = new GeneratorConfigurationBuilder();
			configBuilder.allowedPackage(builtProjectClassLoader.loadClass(className).getPackage().getName());
			GeneratorConfiguration configuration = configBuilder.build();

			gen.init(builtProjectClassLoader, configuration.getSourceEncoding());
			gen.generateJavascript(builtProjectClassLoader, className, sourceFolder, targetFolder, generationFolder, configuration);
		}
		catch (Exception e) {
			throw Throwables.propagate(e);
		}
		finally {
			gen.close();
		}
	}

	static void compile(final String path, final List<File> sourceFiles, List<File> dependencies) {
		try {
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			if (compiler == null) {
				throw new STJSRuntimeException("A Java compiler is not available for this project. "
						+ "You may have configured your environment to run with a JRE instead of a JDK");
			}
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			fileManager.setLocation(StandardLocation.CLASS_PATH, dependencies);

			Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(sourceFiles);
			compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();

			fileManager.close();
		}
		catch (Exception e) {
			throw Throwables.propagate(e);
		}

	}
}