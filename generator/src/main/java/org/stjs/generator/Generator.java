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

import static org.stjs.generator.utils.Lists.append;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.stjs.generator.handlers.GeneratorVisitor;
import org.stjs.generator.handlers.SetParentVisitor;
import org.stjs.generator.scope.DeclarationVisitor;
import org.stjs.generator.scope.FullyQualifiedScope;
import org.stjs.generator.scope.NameResolverVisitor;
import org.stjs.generator.scope.NameScope;
import org.stjs.generator.scope.NameScopeWalker;
import org.stjs.generator.scope.classloader.ClassLoaderWrapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
		FileWriter writer = null;
		InputStream in = null;
		try {
			writer = new FileWriter(outputFile, append);

			try {
				in = new FileInputStream(inputFile);
			} catch (FileNotFoundException e) {
				throw new JavascriptGenerationException(inputFile, null, e);
			}

			GenerationContext context = new GenerationContext(inputFile);
			NameScope rootScope = new FullyQualifiedScope(inputFile, new ClassLoaderWrapper(builtProjectClassLoader));

			CompilationUnit cu = null;
			// parse the file
			cu = JavaParser.parse(in);

			// set the parent of each node
			cu.accept(new SetParentVisitor(), context);

			Collection<String> allowedPackages = configuration.getAllowedPackages();
			if (cu.getPackage() != null && !cu.getPackage().getName().toString().isEmpty()) {
				allowedPackages = append(Lists.newArrayList(allowedPackages), cu.getPackage().getName().toString());
			}

			// ASTUtils.dumpXML(cu);

			// 1. read the scope of all declared variables and methods
			DeclarationVisitor scopes = new DeclarationVisitor(inputFile, builtProjectClassLoader, allowedPackages);
			scopes.visit(cu, rootScope);
			// rootScope.dump(" ");

			// 2. resolve all the calls to methods and variables
			NameResolverVisitor resolver = new NameResolverVisitor(rootScope, allowedPackages,
					configuration.getAllowedJavaLangClasses());

			resolver.visit(cu, new NameScopeWalker(rootScope));

			// 3. generate the javascript code
			GeneratorVisitor generatorVisitor = new GeneratorVisitor(configuration.isGenerateMainMethodCall(),
					configuration.getAdapterClassNames());
			generatorVisitor.visit(cu, context);

			writer.write(generatorVisitor.getGeneratedSource());

			return resolver.getResolvedImports();
		} catch (IOException e1) {
			throw new RuntimeException("Could not open output file " + outputFile);
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

			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				// silent
			}

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
		}
	}

	/**
	 * 
	 * @param builtProjectClassLoader
	 * @param inputFile
	 * @param outputFile
	 * @param configuration
	 * @throws JavascriptGenerationException
	 */
	public void generateJavascriptWithImports(ClassLoader builtProjectClassLoader, File inputFile, File outputFile,
			GeneratorConfiguration configuration) throws JavascriptGenerationException {
		Generator generator = new Generator();

		int generatedFiles = 0;
		Pattern exceptions = Pattern.compile("java\\.lang.*|org\\.stjs\\.testing.*|org\\.junit.*|junit.*");
		try {
			Set<String> newImports = Sets.newHashSet();

			File src = inputFile;
			Set<String> convertedClasses = Sets.newHashSet();

			do {
				if (src == null) {
					Iterator<String> iterator = newImports.iterator();
					String nextImport = iterator.next();
					iterator.remove();

					if (nextImport.startsWith("org.stjs.testing") || nextImport.startsWith("org.stjs.javascript")) {
						continue;
					}
					if (nextImport.contains("$")) {
						// this is an inner class
						continue;
					}

					File maybeTestFile = new File("src/test/java/" + nextImport.replaceAll("\\.", "/") + ".java");

					if (maybeTestFile.exists()) {
						src = maybeTestFile;
					} else {
						File maybeAppFile = new File("src/main/java/" + nextImport.replaceAll("\\.", "/") + ".java");
						if (maybeAppFile.exists()) {
							src = maybeAppFile;
						} else {
							throw new IllegalStateException(
									"Unable to locate the source file for type "
											+ nextImport
											+ ". Currently only src/test/java and src/main/java naming schemes are supported. Note that all classes must be defined in the same module as the unit test");
						}
					}
				}

				File tmpOutputFile = new File(outputFile.getPath() + "." + generatedFiles);
				generatedFiles++;

				Set<String> iterationResolvedImports = generator.generateJavascript(Thread.currentThread()
						.getContextClassLoader(), src, tmpOutputFile, configuration, true);
				src = null;// set it to null to search a new import

				for (String iterationResolvedImport : iterationResolvedImports) {
					if (!exceptions.matcher(iterationResolvedImport).matches()
							&& convertedClasses.add(iterationResolvedImport)) {
						newImports.add(iterationResolvedImport);
					}
				}
			} while (!newImports.isEmpty());

		} catch (JavascriptGenerationException e) {
			e.printStackTrace();
			throw new AssertionError(e.getMessage());
		} finally {
			cleanupOutputFiles(outputFile, generatedFiles);
		}

		// try {
		// Files.copy(outputFile, System.out);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.flush();

	}

	/**
	 * copy all the generated file in the output file in reverse order and then delete them
	 * 
	 * @param outputFile
	 * @param generatedFiles
	 */
	private void cleanupOutputFiles(File outputFile, int generatedFiles) {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(outputFile));
			for (int n = generatedFiles - 1; n >= 0; --n) {
				File tmpFile = new File(outputFile.getPath() + "." + n);
				Files.copy(tmpFile, out);
				tmpFile.delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				// silent
			}
		}

	}
}