package org.stjs.generator.scope;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfiguration;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.ast.ASTNodeData;
import org.stjs.generator.name.DefaultJavaScriptNameProvider;
import org.stjs.generator.type.ClassLoaderWrapper;
import org.stjs.generator.type.FieldWrapper;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.type.TypeWrappers;
import org.stjs.generator.variable.Variable;
import org.stjs.generator.visitor.SetParentVisitor;

public class ScopeTestHelper {

	private static String getSourceFile(Class<?> clazz) {
		return new Generator().getInputFile(new File("src/test/java"), clazz.getName()).getPath();
	}

	public static CompilationUnit resolveName(Class<?> clazz) {
		return resolveName(clazz, Collections.<String>emptyList());
	}

	public static CompilationUnit resolveName(Class<?> clazz, Collection<String> packages) {
		InputStream in = null;
		try {
			in = new FileInputStream(getSourceFile(clazz));
			CompilationUnit cu = JavaParser.parse(in);
			GeneratorConfiguration config = new GeneratorConfigurationBuilder().allowedPackages(packages).allowedPackage("org.stjs.javascript")
					.allowedPackage("org.stjs.generator").build();

			ClassLoaderWrapper classLoader = new ClassLoaderWrapper(Thread.currentThread().getContextClassLoader(), config.getAllowedPackages(),
					config.getAllowedJavaLangClasses());
			GenerationContext context = new GenerationContext(new File(getSourceFile(clazz)), config, new DefaultJavaScriptNameProvider(), null);
			// set the parent of each node
			cu.accept(new SetParentVisitor(), context);
			ScopeBuilder builder = new ScopeBuilder(classLoader, context);
			CompilationUnitScope scope = new CompilationUnitScope(classLoader, context);

			builder.visit(cu, scope);
			return cu;
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (IOException e) {
					// silent
				}
			}
		}

	}

	public static void assertResolvedName(Class<?> clazz, String name, Class<? extends Scope> scopeClass, int level) {
		assertResolvedName(clazz, name, 1, scopeClass, level);
	}

	public static void assertResolvedName(Class<?> clazz, final String name, final int occurence, Class<? extends Scope> scopeClass, int level) {
		CompilationUnit cu = resolveName(clazz);
		final AtomicReference<Node> nodePointer = new AtomicReference<Node>();
		cu.accept(new VoidVisitorAdapter<Boolean>() {
			private int crtOccurrence = 0;

			@Override
			public void visit(NameExpr n, Boolean arg) {
				if (n.getName().equals(name)) {
					if (++crtOccurrence == occurence) {
						nodePointer.set(n);
					}
				}
				super.visit(n, arg);
			}
		}, null);

		assertNotNull(nodePointer.get());
		Scope varScope = ASTNodeData.resolvedVariableScope(nodePointer.get());
		assertNotNull(varScope);
		assertTrue(varScope.getClass() == scopeClass);
		int scopeLevel = 0;
		for (Scope s = varScope.getParent(); s != null; s = s.getParent()) {
			scopeLevel++;
		}
		assertEquals(level, scopeLevel);
	}

	public static void assertResolvedField(Class<?> clazz, final String name, Class<?> ownerClass) {
		assertResolvedField(clazz, name, 1, ownerClass);
	}

	public static void assertResolvedField(Class<?> clazz, final String name, final int occurence, Class<?> ownerClass) {
		CompilationUnit cu = resolveName(clazz);
		final AtomicReference<Node> nodePointer = new AtomicReference<Node>();
		cu.accept(new VoidVisitorAdapter<Boolean>() {
			private int crtOccurrence = 0;

			@Override
			public void visit(FieldAccessExpr n, Boolean arg) {
				if (n.getField().equals(name)) {
					if (++crtOccurrence == occurence) {
						nodePointer.set(n);
					}
					super.visit(n, arg);
				}
			}
		}, null);

		assertNotNull(nodePointer.get());
		Variable var = ASTNodeData.resolvedVariable(nodePointer.get());
		assertNotNull(var);
		assertTrue(var instanceof FieldWrapper);
		assertTrue(TypeWrappers.wrap(ownerClass).isAssignableFrom(((FieldWrapper) var).getOwnerType()));
	}

	public static void assertResolvedMethod(Class<?> clazz, final String name, Class<?> ownerClass) {
		assertResolvedMethod(clazz, name, 1, ownerClass);
	}

	public static void assertResolvedMethod(Class<?> clazz, final String name, final int occurence, Class<?> ownerClass) {
		CompilationUnit cu = resolveName(clazz);
		final AtomicReference<Node> nodePointer = new AtomicReference<Node>();
		cu.accept(new VoidVisitorAdapter<Boolean>() {
			private int crtOccurrence = 0;

			@Override
			public void visit(MethodCallExpr n, Boolean arg) {
				if (n.getName().equals(name)) {
					if (++crtOccurrence == occurence) {
						nodePointer.set(n);
					}
				}
				super.visit(n, arg);
			}
		}, null);

		assertNotNull(nodePointer.get());
		MethodWrapper method = ASTNodeData.resolvedMethod(nodePointer.get());
		assertNotNull(method);
		assertTrue(TypeWrappers.wrap(ownerClass).isAssignableFrom(method.getOwnerType()));
	}

	public static void assertResolvedAnonymousClass(Class<?> clazz, final int occurence, Class<?> parentClass) {
		CompilationUnit cu = resolveName(clazz);
		final AtomicReference<Node> nodePointer = new AtomicReference<Node>();
		cu.accept(new VoidVisitorAdapter<Boolean>() {
			private int crtOccurrence = 0;

			@Override
			public void visit(ObjectCreationExpr n, Boolean arg) {
				if (n.getAnonymousClassBody() != null) {
					if (++crtOccurrence == occurence) {
						nodePointer.set(n);
					}
				}
				super.visit(n, arg);
			}
		}, null);

		assertNotNull(nodePointer.get());
		TypeWrapper type = ASTNodeData.resolvedType(nodePointer.get());
		assertNotNull(type);
		assertTrue(TypeWrappers.wrap(parentClass).isAssignableFrom(type));
	}
}
