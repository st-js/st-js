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
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.stjs.generator.check.CheckContributors;
import org.stjs.generator.javac.CustomClassloaderJavaFileManager;
import org.stjs.generator.name.DefaultJavaScriptNameProvider;
import org.stjs.generator.name.JavaScriptNameProvider;
import org.stjs.generator.type.ClassLoaderWrapper;
import org.stjs.generator.type.ClassWrapper;
import org.stjs.generator.utils.ClassUtils;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.writer.JavascriptWriterContributors;
import org.stjs.generator.writer.JavascriptWriterVisitor;

import com.google.common.base.Throwables;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;

/**
 * This class parses a Java source file, launches several visitors and finally generate the corresponding Javascript.
 * @author acraciun
 */
public class Generator {

	private static final String STJS_FILE = "stjs.js";

	public Generator() {

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

	/**
	 * @param builtProjectClassLoader
	 * @param inputFile
	 * @param outputFile
	 * @param configuration
	 * @return the list of imports needed by the generated class
	 */
	public ClassWithJavascript generateJavascript(ClassLoader builtProjectClassLoader, String className, File sourceFolder,
			GenerationDirectory generationFolder, File targetFolder, GeneratorConfiguration configuration)
			throws JavascriptFileGenerationException {

		ClassLoaderWrapper classLoaderWrapper =
				new ClassLoaderWrapper(builtProjectClassLoader, configuration.getAllowedPackages(), configuration.getAllowedJavaLangClasses());
		DependencyResolver dependencyResolver =
				new GeneratorDependencyResolver(builtProjectClassLoader, sourceFolder, generationFolder, targetFolder, configuration);

		ClassWrapper clazz = classLoaderWrapper.loadClass(className).getOrThrow();
		if (ClassUtils.isBridge(clazz.getClazz())) {
			return new BridgeClass(dependencyResolver, clazz.getClazz());
		}

		File inputFile = getInputFile(sourceFolder, className);
		File outputFile = getOutputFile(generationFolder.getAbsolutePath(), className);
		JavaScriptNameProvider names = new DefaultJavaScriptNameProvider();
		GenerationContext context = new GenerationContext(inputFile, configuration, names, null);

		CompilationUnitTree cu = parseAndResolve(classLoaderWrapper, inputFile, context, configuration.getSourceEncoding());
		BufferedWriter writer = null;

		try {
			// check the code
			TreePathScannerContributors<Void, GenerationContext> astChecks = new TreePathScannerContributors<Void, GenerationContext>();
			astChecks.setContinueScanning(true);
			CheckContributors.addContributors(astChecks);
			astChecks.scan(cu, context);
			context.getChecks().check();

			// generate the javascript code
			TreePathScannerContributors<List<AstNode>, GenerationContext> javascriptAstWriter =
					new TreePathScannerContributors<List<AstNode>, GenerationContext>();
			JavascriptWriterContributors.addContributors(javascriptAstWriter);
			AstRoot javascriptRoot = (AstRoot) javascriptAstWriter.scan(cu, context).get(0);

			writer = Files.newWriter(outputFile, Charset.forName(configuration.getSourceEncoding()));
			writer.write(javascriptRoot.toSource());
			writer.flush();

		}
		catch (IOException e1) {
			throw new STJSRuntimeException("Could not open output file " + outputFile + ":" + e1, e1);
		}
		finally {
			Closeables.closeQuietly(writer);
		}

		// write properties
		STJSClass stjsClass = new STJSClass(dependencyResolver, targetFolder, className);
		Set<String> resolvedClasses = new LinkedHashSet<String>(names.getResolvedTypes());
		resolvedClasses.remove(className);
		stjsClass.setDependencies(resolvedClasses);
		stjsClass.setGeneratedJavascriptFile(relative(generationFolder, className));
		stjsClass.store();

		if (configuration.isGenerateSourceMap()) {
			// XXX: generateSourceMap(generationFolder, configuration, context, generatorVisitor, outputFile,
			// stjsClass);
		}
		return stjsClass;
	}

	/**
	 * generate the source map for the given class
	 */
	private void generateSourceMap(GenerationDirectory generationFolder, GeneratorConfiguration configuration, GenerationContext context,
			JavascriptWriterVisitor generatorVisitor, File outputFile, STJSClass stjsClass) {
		BufferedWriter sourceMapWriter = null;

		try {
			// write the source map
			sourceMapWriter =
					Files.newWriter(getSourceMapFile(generationFolder.getAbsolutePath(), stjsClass.getClassName()),
							Charset.forName(configuration.getSourceEncoding()));
			generatorVisitor.writeSourceMap(context, sourceMapWriter);
			sourceMapWriter.flush();

			// copy the source aside the generated js to be able to have it delivered to the browser for debugging
			Files.copy(context.getInputFile(), new File(outputFile.getParentFile(), context.getInputFile().getName()));
			// copy the STJS properties file in the same folder as the Javascript file (if this folder is different
			// to be
			// able to do backward analysis: i.e fine the class name corresponding to a JS)
			File stjsPropFile = stjsClass.getStjsPropertiesFile();
			File copyStjsPropFile = new File(generationFolder.getAbsolutePath(), ClassUtils.getPropertiesFileName(stjsClass.getClassName()));
			if (!stjsPropFile.equals(copyStjsPropFile)) {
				Files.copy(stjsPropFile, copyStjsPropFile);
			}
		}
		catch (IOException e) {
			throw new STJSRuntimeException("Could generate source map:" + e, e);
		}
		finally {
			if (sourceMapWriter != null) {
				Closeables.closeQuietly(sourceMapWriter);
			}
		}
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

	private CompilationUnitTree parseAndResolve(ClassLoaderWrapper builtProjectClassLoader, File inputFile, GenerationContext context,
			String sourceEncoding) {
		try {
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			if (compiler == null) {
				throw new JavascriptFileGenerationException(inputFile, null,
						"A Java compiler is not available for this project. You may have configured your environment to run with JRE instead of a JDK");
			}
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, Charset.forName(sourceEncoding));
			JavaFileManager classLoaderFileManager = new CustomClassloaderJavaFileManager(builtProjectClassLoader.getClassLoader(), fileManager);

			Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjectsFromFiles(Collections.singleton(inputFile));
			JavaCompiler.CompilationTask task = compiler.getTask(null, classLoaderFileManager, null, null, null, fileObjects);
			JavacTask javacTask = (JavacTask) task;

			context.setTrees(Trees.instance(javacTask));
			context.setElements(javacTask.getElements());
			context.setTypes(javacTask.getTypes());

			CompilationUnitTree cu = javacTask.parse().iterator().next();
			javacTask.analyze();

			context.setCompilationUnit(cu);

			fileManager.close();
			return cu;
		}
		catch (Exception e) {
			throw Throwables.propagate(e);
		}

	}

	/**
	 * This method copies the Javascript support file (stjs.js currently) to the desired folder. This method should be
	 * called after the processing of all the files.
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

		@Override
		public ClassWithJavascript resolve(String className) {
			String parentClassName = className;
			int pos = parentClassName.indexOf('$');
			if (pos > 0) {
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
			if (ClassUtils.isBridge(clazz)) {
				return new BridgeClass(this, clazz);
			}

			// check if it has already generated
			STJSClass stjsClass = new STJSClass(this, builtProjectClassLoader, parentClassName);
			if (stjsClass.getJavascriptFiles().isEmpty()) {
				checkFolders(parentClassName);
				stjsClass =
						(STJSClass) generateJavascript(builtProjectClassLoader, parentClassName, sourceFolder, generationFolder, targetFolder,
								configuration);
			}
			return stjsClass;
		}

	}

	/**
	 * This method assumes the javascript code for the given class was already generated
	 * @param testClass
	 */
	public ClassWithJavascript getExistingStjsClass(ClassLoader classLoader, Class<?> testClass) {
		return new GeneratorDependencyResolver(classLoader, null, null, null, null).resolve(testClass.getName());
	}

	@SuppressWarnings("PMD.SystemPrintln")
	public static void main(String[] args) throws URISyntaxException {
		if (args.length == 0) {
			System.out.println("Usage: Generator <class.name> [<allow package>] [<allow package>] .. ");
			return;
		}
		GeneratorConfigurationBuilder builder = new GeneratorConfigurationBuilder();
		for (int i = 1; i < args.length; ++i) {
			builder.allowedPackage(args[i]);
		}
		// new Generator().generateJavascript(Thread.currentThread().getContextClassLoader(), args[0], new File(
		// "src/main/java"), new File("target", "generate-js"), new File("target"), builder.build());
	}
}
