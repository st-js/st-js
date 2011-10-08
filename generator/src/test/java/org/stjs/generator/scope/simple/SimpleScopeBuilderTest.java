package org.stjs.generator.scope.simple;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.Test;
import org.stjs.generator.scope.classloader.ClassLoaderWrapper;

import test.generator.scopes.SimpleClass;
import test.generator.scopes.SimpleClass.AmbiguousName;
import test.generator.scopes.SimpleClass.AmbiguousName.InnerClassLevel2;
import test.generator.scopes.SimpleClass.InnerClass2;
import test.generator.scopes.p.ClassWithCrazyImports;

public class SimpleScopeBuilderTest {

	@Test
	public void test() throws ParseException, IOException {
		String path = ClassWithCrazyImports.class.getName().replace('.', File.separatorChar);
		path = "src/test/java/" + path + ".java";
		CompilationUnit compilationUnit = JavaParser.parse(new File(path));
		SimpleScopeBuilder builder = new SimpleScopeBuilder(new ClassLoaderWrapper(Thread.currentThread()
				.getContextClassLoader()));
		CompilationUnitScope scope = new CompilationUnitScope();
		builder.visit(compilationUnit, scope);
		// TODO : assert all imports have been resolved
		
		assertFieldEquals(scope, Integer.class, SimpleClass.class, "field");
		assertMethodsEquals(scope, 2, SimpleClass.class, "method");
		assertFieldEquals(scope, Integer.class, InnerClassLevel2.class, "innerField");
		assertTypeEquals(scope, SimpleClass.class, "SimpleClass");
		
		assertTypeEquals(scope, AmbiguousName.class, "AmbiguousName");
		assertFieldEquals(scope, AmbiguousName.class, SimpleClass.class, "AmbiguousName");
		assertMethodsEquals(scope, 1, SimpleClass.class, "AmbiguousName");
		
		assertTypeEquals(scope, InnerClassLevel2.class, "InnerClassLevel2");
		assertTypeEquals(scope, InnerClass2.class, "InnerClass2");
		
		assertEquals("test.generator", getOnlyElement(scope.getTypeImportOnDemandSet()).toString());
	}

	private void assertTypeEquals(CompilationUnitScope scope, Class<?> type, String name) {
		assertEquals(type, scope.getType(name).getClazz());
	}

	private void assertMethodsEquals(CompilationUnitScope scope, int size, Class<?> declaringClass, String name) {
		Collection<Method> methods = scope.getMethods(name);
		assertNotNull(methods);
		assertEquals(size, methods.size());
		for (Method method : methods) {
			assertSame(declaringClass, method.getDeclaringClass());
		}
		
	}

	private void assertFieldEquals(CompilationUnitScope scope, Class<?> type,
			Class<?> declaringClass,
			String name) {
		Field field = scope.getField(name);
		assertNotNull(field);
		assertSame(type, field.getType());
		assertSame(declaringClass, field.getDeclaringClass());
	}

}