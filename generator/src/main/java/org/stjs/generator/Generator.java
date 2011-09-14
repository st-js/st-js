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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;

import org.stjs.generator.handlers.GeneratorVisitor;
import org.stjs.generator.handlers.SetParentVisitor;
import org.stjs.generator.scope.DeclarationVisitor;
import org.stjs.generator.scope.FullyQualifiedScope;
import org.stjs.generator.scope.NameResolverVisitor;
import org.stjs.generator.scope.NameScope;
import org.stjs.generator.scope.NameScopeWalker;
import org.stjs.generator.scope.classloader.ClassLoaderWrapper;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

public class Generator {

	private static final String STJS_FILE = "stjs.js";

	public Set<String> generateJavascript(ClassLoader builtProjectClassLoader, File inputFile, File outputFile,
			GeneratorConfiguration configuration) {
		return generateJavascript(builtProjectClassLoader, inputFile, outputFile, configuration, false);
	}

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

			// RuleBasedVisitor ruleVisitor = new RuleBasedVisitor();
			//
			// rules(ruleVisitor);

			GenerationContext context = new GenerationContext(inputFile);
			NameScope rootScope = new FullyQualifiedScope(inputFile, new ClassLoaderWrapper(builtProjectClassLoader));

			CompilationUnit cu = null;
			// parse the file
			cu = JavaParser.parse(in);

			// set the parent of each node
			cu.accept(new SetParentVisitor(), context);

			// resolve all the calls to methods and variables
			Collection<String> allowedPackages = configuration.getAllowedPackages();
			if (cu.getPackage() != null && !cu.getPackage().getName().toString().isEmpty()) {
				allowedPackages = append(Lists.newArrayList(allowedPackages), cu.getPackage().getName().toString());
			}

			// ASTUtils.dumpXML(cu);

			// read the scope of all declared variables and methods
			DeclarationVisitor scopes = new DeclarationVisitor(inputFile, builtProjectClassLoader, allowedPackages);
			scopes.visit(cu, rootScope);
			// rootScope.dump(" ");

			NameResolverVisitor resolver = new NameResolverVisitor(rootScope, allowedPackages,
					configuration.getAllowedJavaLangClasses());

			resolver.visit(cu, new NameScopeWalker(rootScope));

			System.out.println("----------------------------");
			// ruleVisitor.generate(cu, context);

			GeneratorVisitor generatorVisitor = new GeneratorVisitor();
			generatorVisitor.visit(cu, context);

			System.out.println("----------------------------");

			writer.write(generatorVisitor.getGeneratedSource());
			writer.flush();
			writer.close();
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

}