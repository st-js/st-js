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

import static org.stjs.generator.handlers.utils.Lists.append;
import static org.stjs.generator.handlers.utils.Lists.newArrayList;
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

import org.stjs.generator.handlers.ClassOrInterfaceDeclarationHandler;
import org.stjs.generator.handlers.DefaultHandler;
import org.stjs.generator.handlers.EnumHandler;
import org.stjs.generator.handlers.FieldDeclarationHandler;
import org.stjs.generator.handlers.InlineFunctionHandler;
import org.stjs.generator.handlers.InlineObjectHandler;
import org.stjs.generator.handlers.LiteralExpressionHandler;
import org.stjs.generator.handlers.LoopHandler;
import org.stjs.generator.handlers.MethodDeclarationHandler;
import org.stjs.generator.handlers.NameResolverHandler;
import org.stjs.generator.handlers.RuleBasedVisitor;
import org.stjs.generator.handlers.SkipHandler;
import org.stjs.generator.handlers.VariableDeclarationHandler;
import org.stjs.generator.handlers.VariableTypeHandler;
import org.stjs.generator.scope.DeclarationVisitor;
import org.stjs.generator.scope.FullyQualifiedScope;
import org.stjs.generator.scope.NameResolverVisitor;
import org.stjs.generator.scope.NameScope;
import org.stjs.generator.scope.NameScopeWalker;
import org.stjs.generator.scope.classloader.ClassLoaderWrapper;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

public class Generator {

	private static final String STJS_FILE = "stjs.js";

	private static MatchingRule rule(String name, String xpath, int priority, DefaultHandler handler) {
		return (new MatchingRule(name, xpath, new NodeHandlerWithPriority(handler, priority)));
	}

	private void rules(RuleBasedVisitor ruleVisitor) {
		// to skip
		ruleVisitor.addRule(rule("Parameter Type", "//Parameter/ReferenceType", 100, new SkipHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Parameter Type", "//Parameter/PrimitiveType", 100, new SkipHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Parameter Type", "//PackageDeclaration", 100, new SkipHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Parameter Type", "//ImportDeclaration", 100, new SkipHandler(ruleVisitor)));

		ruleVisitor.addRule(rule("VariableDeclaration", "//VariableDeclaratorId", 100, new VariableDeclarationHandler(
				ruleVisitor)));
		ruleVisitor.addRule(rule("VariableDeclaration", "//VariableDeclarationExpr", 100,
				new VariableDeclarationHandler(ruleVisitor)));

		ruleVisitor.addRule(rule("Variable Type", "//VariableDeclarationExpr/ReferenceType/ClassOrInterfaceType", 100,
				new VariableTypeHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Variable Type", "//VariableDeclarationExpr/PrimitiveType", 100,
				new VariableTypeHandler(ruleVisitor)));

		ruleVisitor.addRule(rule("Annotations", "//MarkerAnnotationExpr", 100, new SkipHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Cast expressions", "//CastExpr", 100, new SkipHandler(ruleVisitor)));

		ruleVisitor
				.addRule(rule("Method", "//MethodDeclaration", 100, new MethodDeclarationHandler(ruleVisitor, false)));
		ruleVisitor.addRule(rule("Constrctor", "//ConstructorDeclaration", 100, new MethodDeclarationHandler(
				ruleVisitor, true)));
		ruleVisitor.addRule(rule("Method Params", "//MethodDeclaration/Parameter", 100, new MethodDeclarationHandler(
				ruleVisitor, false)));
		ruleVisitor.addRule(rule("Method Params", "//ConstructorDeclaration/Parameter", 100,
				new MethodDeclarationHandler(ruleVisitor, false)));
		ruleVisitor.addRule(rule("Class/Interface Declaration", "//ClassOrInterfaceDeclaration", 100,
				new ClassOrInterfaceDeclarationHandler(ruleVisitor)));

		ruleVisitor.addRule(rule("Enum Declaration", "//EnumDeclaration", 100, new EnumHandler(ruleVisitor)));

		// field declaration
		ruleVisitor.addRule(rule("Field", "//FieldDeclaration", 100, new FieldDeclarationHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Field - Variable Type", "//FieldDeclaration/ReferenceType/ClassOrInterfaceType", 100,
				new FieldDeclarationHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Field - Variable Type", "//FieldDeclaration/PrimitiveType", 100,
				new FieldDeclarationHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Field - Variable Type", "//FieldDeclaration/VariableDeclarator", 100,
				new FieldDeclarationHandler(ruleVisitor)));

		// method declaration
		ruleVisitor.addRule(rule("Inline Function", "//ObjectCreationExpr[MethodDeclaration]", 100,
				new InlineFunctionHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Inline Method", "//ObjectCreationExpr/MethodDeclaration", 200,
				new MethodDeclarationHandler(ruleVisitor, true)));

		// inline obj
		ruleVisitor.addRule(rule("Inline Object Decl", "//ObjectCreationExpr[InitializerDeclaration]", 100,
				new InlineObjectHandler(ruleVisitor)));

		ruleVisitor.addRule(rule("Inline Object Decl", "//ObjectCreationExpr/InitializerDeclaration/BlockStmt", 100,
				new InlineObjectHandler(ruleVisitor)));

		ruleVisitor.addRule(rule("Inline Object Decl",
				"//ObjectCreationExpr/InitializerDeclaration/BlockStmt/ExpressionStmt", 100, new InlineObjectHandler(
						ruleVisitor)));

		ruleVisitor.addRule(rule("Inline Object Decl",
				"//ObjectCreationExpr/InitializerDeclaration/BlockStmt/ExpressionStmt/AssignExpr", 100,
				new InlineObjectHandler(ruleVisitor)));

		// names
		NameResolverHandler nameResolveHandler = new NameResolverHandler(ruleVisitor);
		ruleVisitor.addRule(rule("Identifiers", "//NameExpr", 100, nameResolveHandler));
		ruleVisitor.addRule(rule("Identifiers", "//ClassOrInterfaceType", 100, nameResolveHandler));

		ruleVisitor
				.addRule(rule("Identifiers", "//IntegerLiteralExpr", 100, new LiteralExpressionHandler(ruleVisitor)));
		ruleVisitor.addRule(rule("Identifiers", "//LongLiteralExpr", 100, new LiteralExpressionHandler(ruleVisitor)));

		ruleVisitor.addRule(rule("Method calls", "//MethodCallExpr", 100, nameResolveHandler));
		ruleVisitor.addRule(rule("Constructor Calls", "//ExplicitConstructorInvocationStmt", 100, nameResolveHandler));

		// loops
		ruleVisitor.addRule(rule("For Each", "//ForeachStmt", 100, new LoopHandler(ruleVisitor)));
	}

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

			RuleBasedVisitor ruleVisitor = new RuleBasedVisitor();

			rules(ruleVisitor);

			GenerationContext context = new GenerationContext(inputFile);
			NameScope rootScope = new FullyQualifiedScope(inputFile, new ClassLoaderWrapper(builtProjectClassLoader));

			CompilationUnit cu = null;
			// parse the file
			cu = JavaParser.parse(in);

			// resolve all the calls to methods and variables
			Collection<String> allowedPackages = configuration.getAllowedPackages();
			if (cu.getPackage() != null && !cu.getPackage().getName().toString().isEmpty()) {
				allowedPackages = append(newArrayList(allowedPackages), cu.getPackage().getName().toString());
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
			ruleVisitor.generate(cu, context);

			System.out.println("----------------------------");

			writer.write(ruleVisitor.getSource());
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
			throw new RuntimeException("Could not copy the " + STJS_FILE + " file to the folder " + folder);
		}
	}

}