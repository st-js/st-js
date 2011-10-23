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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.stjs.generator.utils.Lists.append;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.stjs.generator.scope.CompilationUnitScope;
import org.stjs.generator.scope.ScopeBuilder;
import org.stjs.generator.type.ClassLoaderWrapper;
import org.stjs.generator.visitor.SetParentVisitor;
import org.stjs.generator.writer.JavascriptWriterVisitor;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

/**
 * This class parses a Java source file, launches several visitors and finally generate the corresponding Javascript.
 * 
 * @author acraciun
 * 
 */
public class Generator {

	private static final String STJS_FILE = "stjs.js";

	/**
	 * 
	 * @param builtProjectClassLoader
	 * @param inputFile
	 * @param outputFile
	 * @param configuration
	 * @return the list of imports needed by the generated class
	 */
	public Set<String> generateJavascript(ClassLoader builtProjectClassLoader, File inputFile, File outputFile,
			GeneratorConfiguration configuration) throws JavascriptGenerationException {
		return generateJavascript(builtProjectClassLoader, inputFile, outputFile, configuration, false);
	}

	/**
	 * 
	 * @param builtProjectClassLoader
	 * @param inputFile
	 * @param outputFile
	 * @param configuration
	 * @param append
	 * @return the list of imports needed by the generated class
	 * @throws JavascriptGenerationException
	 */
	public Set<String> generateJavascript(ClassLoader builtProjectClassLoader, File inputFile, File outputFile,
			GeneratorConfiguration configuration, boolean append) throws JavascriptGenerationException {
		GenerationContext context = new GenerationContext(inputFile);
		CompilationUnitScope unitScope = new CompilationUnitScope(new ClassLoaderWrapper(builtProjectClassLoader),
				context);

		CompilationUnit cu = parseAndResolve(builtProjectClassLoader, inputFile, configuration, context, unitScope);

		FileWriter writer = null;
		try {
			// 3. generate the javascript code

			JavascriptWriterVisitor generatorVisitor = new JavascriptWriterVisitor();
			generatorVisitor.visit(cu, context);

			writer = new FileWriter(outputFile, append);
			writer.write(generatorVisitor.getGeneratedSource());

		} catch (IOException e1) {
			throw new RuntimeException("Could not open output file " + outputFile);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				// silent
			}
		}
		return context.getResolvedImports();
	}

	private CompilationUnit parseAndResolve(ClassLoader builtProjectClassLoader, File inputFile,
			GeneratorConfiguration configuration, GenerationContext context, CompilationUnitScope rootScope) {
		CompilationUnit cu = null;
		InputStream in = null;
		try {

			try {
				in = new FileInputStream(inputFile);
			} catch (FileNotFoundException e) {
				throw new JavascriptGenerationException(inputFile, null, e);
			}

			// parse the file
			cu = JavaParser.parse(in);

			// set the parent of each node
			cu.accept(new SetParentVisitor(), context);

			Collection<String> allowedPackages = configuration.getAllowedPackages();
			if ((cu.getPackage() != null) && !cu.getPackage().getName().toString().isEmpty()) {
				allowedPackages = append(Lists.newArrayList(allowedPackages), cu.getPackage().getName().toString());
			}

			// ASTUtils.dumpXML(cu);

			// 1. read the scope of all declared variables and methods
			ScopeBuilder scopes = new ScopeBuilder(new ClassLoaderWrapper(builtProjectClassLoader), context);
			scopes.visit(cu, rootScope);
			// rootScope.dump(" ");

		} catch (ParseException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// silent
			}

		}
		return cu;
	}

	public void generateJavascriptWithImports(ClassLoader builtProjectClassLoader, String inputFileName,
			File outputFile, GeneratorConfiguration configuration) throws JavascriptGenerationException {

		Pattern exceptions = Pattern.compile("java\\.lang.*|org\\.stjs\\.testing.*|org\\.junit.*|junit.*");
		List<File> resolvedImports = newArrayList();
		Set<String> resolvedImportsNames = newHashSet();
		resolveJavascriptWithImports(builtProjectClassLoader, inputFileName, outputFile, configuration,
				resolvedImports, resolvedImportsNames, exceptions);
		Collections.reverse(resolvedImports);
		for (File srcFile : resolvedImports) {
			generateJavascript(builtProjectClassLoader, srcFile, outputFile, configuration, true);
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
			throw new RuntimeException(STJS_FILE + " is missing from the Generator's classpath");
		}
		File outputFile = new File(folder, STJS_FILE);
		try {
			Files.copy(new InputSupplier<InputStream>() {
				@Override
				public InputStream getInput() throws IOException {
					return stjs;
				}
			}, outputFile);
		} catch (IOException e) {
			throw new RuntimeException("Could not copy the " + STJS_FILE + " file to the folder " + folder, e);
		} finally {
			try {
				stjs.close();
			} catch (IOException e) {
				// silent
			}
		}
	}

	private void resolveJavascriptWithImports(ClassLoader builtProjectClassLoader, File inputFile, File outputFile,
			GeneratorConfiguration configuration, List<File> resolvedImports, Set<String> resolvedImportsNames,
			Pattern exceptions) {
		resolvedImports.add(inputFile);
		GenerationContext context = new GenerationContext(inputFile);
		CompilationUnitScope unitScope = new CompilationUnitScope(new ClassLoaderWrapper(builtProjectClassLoader),
				context);
		parseAndResolve(builtProjectClassLoader, inputFile, configuration, context, unitScope);

		for (String iterationResolvedImport : context.getResolvedImports()) {
			if (!exceptions.matcher(iterationResolvedImport).matches()
					&& resolvedImportsNames.add(iterationResolvedImport)) {
				resolveJavascriptWithImports(builtProjectClassLoader, iterationResolvedImport, outputFile,
						configuration, resolvedImports, resolvedImportsNames, exceptions);
			}
		}
	}

	private void resolveJavascriptWithImports(ClassLoader builtProjectClassLoader, String inputFileName,
			File outputFile, GeneratorConfiguration configuration, List<File> resolvedImports,
			Set<String> resolvedImportsNames, Pattern exceptions) {

		File src;
		if (inputFileName.startsWith("org.stjs.testing") || inputFileName.startsWith("org.stjs.javascript")) {
			return;
		}
		if (inputFileName.contains("$")) {
			// this is an inner class
			return;
		}

		File maybeTestFile = new File("src/test/java/" + inputFileName.replaceAll("\\.", "/") + ".java");

		if (maybeTestFile.exists()) {
			src = maybeTestFile;
		} else {
			File maybeAppFile = new File("src/main/java/" + inputFileName.replaceAll("\\.", "/") + ".java");
			if (maybeAppFile.exists()) {
				src = maybeAppFile;
			} else {
				throw new IllegalStateException(
						"Unable to locate the source file for type "
								+ inputFileName
								+ ". Currently only src/test/java and src/main/java naming schemes are supported. Note that all classes must be defined in the same module as the unit test");
			}
		}
		resolveJavascriptWithImports(builtProjectClassLoader, src, outputFile, configuration, resolvedImports,
				resolvedImportsNames, exceptions);
	}

}