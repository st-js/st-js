/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.lang.model.element.TypeElement;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import org.stjs.generator.GenerationContext.AnnotationCacheKey;
import org.stjs.generator.javac.CustomClassloaderJavaFileManager;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.rhino.RhinoJavaScriptBuilder;
import org.stjs.generator.name.DefaultJavaScriptNameProvider;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.name.JavaScriptNameProvider;
import org.stjs.generator.plugin.GenerationPlugins;
import org.stjs.generator.utils.ClassUtils;
import org.stjs.generator.utils.Timers;

import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTool;

/**
 * This class parses a Java source file, launches several visitors and finally generate the corresponding Javascript.
 *
 * @author acraciun
 */
public class Generator {
	private static final Logger LOG = Logger.getLogger(Generator.class.getName());
	private static final int EXECUTOR_TERMINAL_TIMEOUT = 10;
	private static final String STJS_FILE = "stjs.js";
	private final GenerationPlugins<Object> plugins;
	private StandardJavaFileManager fileManager;
	private JavaFileManager classLoaderFileManager;
	private final Map<AnnotationCacheKey, Object> cacheAnnotations = Maps.newHashMap();
	private Executor taskExecutor;
	private String sourceEncoding;

	public Generator() {
		plugins = new GenerationPlugins<Object>();
	}

	@SuppressWarnings("PMD.DoNotUseThreads")
	public void init(ClassLoader builtProjectClassLoader, String sourceEncoding) {
		// nothing to do
		cacheAnnotations.clear();
		// taskExecutor = Executors.newFixedThreadPool(4);
		taskExecutor = new Executor() {
			@Override
			public void execute(Runnable command) {
				command.run();
			}
		};
		if (sourceEncoding == null) {
			this.sourceEncoding = Charset.defaultCharset().name();
		} else {
			this.sourceEncoding = sourceEncoding;
		}
	}

	@edu.umd.cs.findbugs.annotations.SuppressWarnings("BC_UNCONFIRMED_CAST")
	public void close() {
		try {
			Closeables.close(fileManager, true);
		}
		catch (IOException e) {
			LOG.log(Level.SEVERE, "IOException should not have been thrown.", e);
		}
		if (taskExecutor instanceof ExecutorService) {
			ExecutorService es = (ExecutorService) taskExecutor;
			es.shutdown();
			try {
				es.awaitTermination(EXECUTOR_TERMINAL_TIMEOUT, TimeUnit.SECONDS);
			}
			catch (InterruptedException e) {
				return;
			}
		}
	}

	public File getOutputFile(File generationFolder, String className) {
		return getOutputFile(generationFolder, className, true);
	}

	public File getSourceMapFile(File generationFolder, String className) {
		return new File(generationFolder, className.replace('.', File.separatorChar) + ".map");
	}

	public File getOutputFile(File generationFolder, String className, boolean generateDirectory) {
		File output = new File(generationFolder, className.replace('.', File.separatorChar) + ".js");
		if (generateDirectory && !output.getParentFile().exists() && !output.getParentFile().mkdirs()) {
			throw new STJSRuntimeException("Unable to create parent folder for the output file:" + output);
		}
		return output;
	}

	public File getInputFile(File sourceFolder, String className) {
		return new File(sourceFolder, className.replace('.', File.separatorChar) + ".java");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JavaScriptBuilder<Object> getJavaScriptBuilder() {
		// TODO: here we my return SourceJavaScript builder as well.
		return (JavaScriptBuilder) new RhinoJavaScriptBuilder();
	}

	/**
	 * @param builtProjectClassLoader
	 * @param inputFile
	 * @param outputFile
	 * @param configuration
	 * @return the list of imports needed by the generated class
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ClassWithJavascript generateJavascript(ClassLoader builtProjectClassLoader, String className, File sourceFolder,
			GenerationDirectory generationFolder, File targetFolder, GeneratorConfiguration configuration)
			throws JavascriptFileGenerationException {

		DependencyResolver dependencyResolver = new GeneratorDependencyResolver(builtProjectClassLoader, sourceFolder, generationFolder,
				targetFolder, configuration);

		Class<?> clazz = ClassUtils.getClazz(builtProjectClassLoader, className);
		if (ClassUtils.isBridge(builtProjectClassLoader, clazz)) {
			return new BridgeClass(dependencyResolver, clazz);
		}
		if (ClassUtils.isSynthetic(builtProjectClassLoader, clazz)) {
			return new SyntheticClass(dependencyResolver, clazz);
		}

		File inputFile = getInputFile(sourceFolder, className);
		File outputFile = getOutputFile(generationFolder.getAbsolutePath(), className);
		JavaScriptNameProvider names = new DefaultJavaScriptNameProvider();
		GenerationPlugins<Object> currentClassPlugins = plugins.forClass(clazz);

		GenerationContext<Object> context = new GenerationContext<Object>(inputFile, configuration, names, null, builtProjectClassLoader,
				cacheAnnotations, getJavaScriptBuilder());

		CompilationUnitTree cu = parseAndResolve(inputFile, context, builtProjectClassLoader, configuration.getSourceEncoding());

		// check the code
		Timers.start("check-java");
		currentClassPlugins.getCheckVisitor().scan(cu, (GenerationContext) context);
		context.getChecks().check();
		Timers.end("check-java");

		// generate the javascript code
		Timers.start("write-js-ast");
		Object javascriptRoot = currentClassPlugins.getWriterVisitor().scan(cu, context);
		// check for any error arriving during writing
		context.getChecks().check();
		Timers.end("write-js-ast");

		STJSClass stjsClass = new STJSClass(dependencyResolver, targetFolder, className);
		Map<String, DependencyType> resolvedClasses = new LinkedHashMap<String, DependencyType>(names.getResolvedTypes());
		resolvedClasses.remove(className);
		stjsClass.setDependencies(resolvedClasses);
		stjsClass.setGeneratedJavascriptFile(relative(generationFolder, className));

		TypeElement classElement = context.getElements().getTypeElement(clazz.getCanonicalName());
		stjsClass.setJavascriptNamespace(context.wrap(classElement).getNamespace());

		// dump the ast to a file
		taskExecutor.execute(new DumpFilesTask<Object>(outputFile, context, javascriptRoot, stjsClass, generationFolder, configuration
				.isGenerateSourceMap()));

		return stjsClass;
	}

	private URI relative(GenerationDirectory generationFolder, String className) {
		// FIXME temporary have to remove the full path from the file name.
		// it should be different depending on whether the final artifact is a war or a jar.
		// String path = generationFolder.getPath();
		// int pos = path.lastIndexOf("target");
		try {
			File file = getOutputFile(generationFolder.getRelativeToClasspath(), className, false);
			String path = file.getPath().replace('\\', '/');
			// make it "absolute"
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			return new URI(path);

			// if (pos >= 0) {
			// File file = getOutputFile(new File(path.substring(pos)), className);
			// return new URI(file.getPath().replace('\\', '/'));
			// }
			// return getOutputFile(generationFolder, className).toURI();
		}
		catch (URISyntaxException e) {
			throw new JavascriptClassGenerationException(className, e);
		}
	}

	private JavaCompiler getCompiler(ClassLoader builtProjectClassLoader, String sourceEncoding) {
		// create it directly to avoid ClassLoader problems
		JavaCompiler compiler = JavacTool.create();
		if (compiler == null) {
			throw new STJSRuntimeException(
					"A Java compiler is not available for this project. You may have configured your environment to run with JRE instead of a JDK");
		}
		if (fileManager == null) {
			fileManager = compiler.getStandardFileManager(null, null, Charset.forName(sourceEncoding));
			classLoaderFileManager = new CustomClassloaderJavaFileManager(builtProjectClassLoader, fileManager);
		}
		return compiler;
	}

	@SuppressWarnings("PMD.AvoidCatchingThrowable") // JavaCompiler throws an Error, so catching Throwable is intentional
	private <JS> CompilationUnitTree parseAndResolve(File inputFile, GenerationContext<JS> context, ClassLoader builtProjectClassLoader,
			String sourceEncoding) {
		JavaCompiler.CompilationTask task = null;
		JavacTask javacTask = null;
		try {
			JavaCompiler compiler = getCompiler(builtProjectClassLoader, sourceEncoding);
			Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjectsFromFiles(Collections.singleton(inputFile));
			List<String> options = Arrays.asList("-proc:none");
			task = compiler.getTask(null, classLoaderFileManager, null, options, null, fileObjects);
			javacTask = (JavacTask) task;

			context.setTrees(Trees.instance(javacTask));
			context.setElements(javacTask.getElements());
			context.setTypes(javacTask.getTypes());

			Timers.start("parse-java");
			CompilationUnitTree cu = javacTask.parse().iterator().next();
			Timers.end("parse-java");

			Timers.start("analyze-java");
			javacTask.analyze();
			Timers.end("analyze-java");

			context.setCompilationUnit(cu);

			return cu;
		}
		catch (Throwable e) {
			throw new JavascriptFileGenerationException(new SourcePosition(context.getInputFile(), 0, 0), "Cannot parse the Java file:" + e);
		}

	}

	/**
	 * This method copies the Javascript support file (stjs.js currently) to the desired folder. This method should be
	 * called after the processing of all the files.
	 *
	 * @param folder
	 */
	public void copyJavascriptSupport(File folder) {
		final InputStream stjs = Thread.currentThread().getContextClassLoader().getResourceAsStream(STJS_FILE);
		if (stjs == null) {
			throw new STJSRuntimeException(STJS_FILE + " is missing from the Generator's classpath");
		}
		File outputFile = new File(folder, STJS_FILE);
		try {
			Files.copy(new InputStreamSupplier(stjs), outputFile);
		}
		catch (IOException e) {
			throw new STJSRuntimeException("Could not copy the " + STJS_FILE + " file to the folder " + folder + ":" + e.getMessage(), e);
		}
		finally {
			Closeables.closeQuietly(stjs);
		}
	}

	private static final class InputStreamSupplier implements InputSupplier<InputStream> {
		private final InputStream input;

		public InputStreamSupplier(InputStream input) {
			this.input = input;
		}

		@Override
		public InputStream getInput() throws IOException {
			return input;
		}

	}

	/**
	 * this class lazily generates the dependencies
	 */
	private class GeneratorDependencyResolver implements DependencyResolver {
		private final ClassLoader builtProjectClassLoader;
		private final File sourceFolder;
		private final GenerationDirectory generationFolder;
		private final File targetFolder;
		private final GeneratorConfiguration configuration;

		public GeneratorDependencyResolver(ClassLoader builtProjectClassLoader, File sourceFolder, GenerationDirectory generationFolder,
				File targetFolder, GeneratorConfiguration configuration) {
			this.builtProjectClassLoader = builtProjectClassLoader;
			this.sourceFolder = sourceFolder;
			this.targetFolder = targetFolder;
			this.generationFolder = generationFolder;
			this.configuration = configuration;
		}

		private void checkFolders(String parentClassName) {
			if (generationFolder == null || sourceFolder == null || targetFolder == null) {
				throw new IllegalStateException("This resolver assumed that the javascript for the class [" + parentClassName
						+ "] was already generated");
			}
		}

		private ClassWithJavascript getSTJSClass(String parentClassName) {
			// check if it has already generated
			STJSClass stjsClass = new STJSClass(this, builtProjectClassLoader, parentClassName);
			if (stjsClass.getJavascriptFiles().isEmpty()) {
				checkFolders(parentClassName);
				stjsClass = (STJSClass) generateJavascript(builtProjectClassLoader, parentClassName, sourceFolder, generationFolder,
						targetFolder, configuration);
			}
			return stjsClass;
		}

		@Override
		public ClassWithJavascript resolve(String className) {
			String parentClassName = className;
			int pos = parentClassName.indexOf('$');
			if (pos > 0 && parentClassName.charAt(pos - 1) != '.') {
				// avoid classes like angularjs.$Timeout
				parentClassName = parentClassName.substring(0, pos);
			}
			// try first if to see if it's a bridge class
			Class<?> clazz;
			try {
				clazz = builtProjectClassLoader.loadClass(parentClassName);
			}
			catch (ClassNotFoundException e) {
				throw new STJSRuntimeException(e);
			}

			if (ClassUtils.isBridge(builtProjectClassLoader, clazz)) {
				return new BridgeClass(this, clazz);
			}

			if (ClassUtils.isSynthetic(builtProjectClassLoader, clazz)) {
				return new SyntheticClass(this, clazz);
			}

			return getSTJSClass(parentClassName);
		}

	}

	/**
	 * This method assumes the javascript code for the given class was already generated
	 *
	 * @param testClass
	 */
	public ClassWithJavascript getExistingStjsClass(ClassLoader classLoader, Class<?> testClass) {
		return new GeneratorDependencyResolver(classLoader, null, null, null, null).resolve(testClass.getName());
	}

	@SuppressWarnings("PMD.DoNotUseThreads")
	private class DumpFilesTask<JS> implements Runnable {
		private final File outputFile;
		private final GenerationContext<JS> context;
		private final JS javascriptRoot;
		private final STJSClass stjsClass;
		private final GenerationDirectory generationFolder;
		private final boolean generateSourceMap;

		public DumpFilesTask(File outputFile, GenerationContext<JS> context, JS javascriptRoot, STJSClass stjsClass,
				GenerationDirectory generationFolder, boolean generateSourceMap) {
			this.outputFile = outputFile;
			this.context = context;
			this.javascriptRoot = javascriptRoot;
			this.stjsClass = stjsClass;
			this.generationFolder = generationFolder;
			this.generateSourceMap = generateSourceMap;
		}

		@Override
		public void run() {
			writeJavaScript();
			writePropertiesFile();
			writeSourceMap();
		}

		private void writeJavaScript() {
			BufferedWriter writer = null;
			try {
				Timers.start("dump-js");
				writer = Files.newWriter(outputFile, Charset.forName(sourceEncoding));
				context.writeJavaScript(javascriptRoot, writer);
				writer.flush();
				Timers.end("dump-js");
			}
			catch (IOException e) {
				throw new STJSRuntimeException("Could not open output file " + outputFile + ":" + e, e);
			}
			finally {
				try {
					Closeables.close(writer, true);
				}
				catch (IOException e) {
					LOG.log(Level.SEVERE, "IOException should not have been thrown.", e);
				}
			}
		}

		// write properties

		private void writePropertiesFile() {
			Timers.start("write-props");
			stjsClass.store();
			Timers.end("write-props");
		}

		private void writeSourceMap() {
			if (generateSourceMap) {
				BufferedWriter sourceMapWriter = null;

				try {
					// write the source map
					sourceMapWriter = Files.newWriter(getSourceMapFile(generationFolder.getAbsolutePath(), stjsClass.getClassName()),
							Charset.forName(sourceEncoding));
					context.writeSourceMap(sourceMapWriter);
					sourceMapWriter.flush();

					// copy the source aside the generated js to be able to have it delivered to the browser for
					// debugging
					Files.copy(context.getInputFile(), new File(outputFile.getParentFile(), context.getInputFile().getName()));
					// copy the STJS properties file in the same folder as the Javascript file (if this folder is
					// different
					// to be
					// able to do backward analysis: i.e fine the class name corresponding to a JS)
					File stjsPropFile = stjsClass.getStjsPropertiesFile();
					File copyStjsPropFile = new File(generationFolder.getAbsolutePath(), ClassUtils.getPropertiesFileName(stjsClass
							.getClassName()));
					if (!stjsPropFile.equals(copyStjsPropFile)) {
						Files.copy(stjsPropFile, copyStjsPropFile);
					}
				}
				catch (IOException e) {
					throw new STJSRuntimeException("Could generate source map:" + e, e);
				}
				finally {
					if (sourceMapWriter != null) {
						try {
							Closeables.close(sourceMapWriter, true);
						}
						catch (IOException e) {
							LOG.log(Level.SEVERE, "IOException should not have been thrown.", e);
						}
					}
				}
			}
		}
	}

}
