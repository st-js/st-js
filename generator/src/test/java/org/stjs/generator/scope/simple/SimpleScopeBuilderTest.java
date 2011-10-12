package org.stjs.generator.scope.simple;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static test.generator.scopes.SimpleClass.method;
import static test.generator.scopes.SimpleClass.AmbiguousName.InnerClassLevel2.innerField;
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
import test.generator.scopes.p.ClassWithCrazyImports.InnerClassC;
import test.innerclasses.ClassDeclaringInnerClass.InnerClass;

public class SimpleScopeBuilderTest {

	@Test
	public void test() throws ParseException, IOException {
		String path = ClassWithCrazyImports.class.getName().replace('.', File.separatorChar);
		path = "src/test/java/" + path + ".java";
		CompilationUnit compilationUnit = JavaParser.parse(new File(path));
		ClassLoaderWrapper classLoader = new ClassLoaderWrapper(Thread.currentThread().getContextClassLoader());
		SimpleScopeBuilder builder = new SimpleScopeBuilder(classLoader);
		CompilationUnitScope scope = new CompilationUnitScope(classLoader);
		
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

		NameScopeWalker walker = new NameScopeWalker(scope, null); 
		NameScopeWalker classScope = walker.nextChild();
		NameScopeWalker methodScope = classScope.nextChild();

		assertVariableEquals(methodScope, int.class, "z");
		assertVariableEquals(methodScope, ClassWithCrazyImports.InnerClassC.class, "cc");
		assertVariableEquals(methodScope, InnerClass.class, "tt");
		assertVariableEquals(methodScope, String.class, "m");

		NameScopeWalker methodBodyScope = methodScope.nextChild();
		assertVariableEquals(methodBodyScope, Integer.class, "f");
		assertVariableEquals(methodBodyScope, InnerClassLevel2.class, "x");
		assertVariableEquals(methodBodyScope, SimpleClass.class, "y");
		assertVariableEquals(methodBodyScope, InnerClass2.class, "k");

		assertMethodsEquals(methodBodyScope.getScope(), 2, SimpleClass.class, "method");

		NameScopeWalker method2BodyScope = classScope.nextChild().nextChild();
		NameScopeWalker anonymousClass1 = method2BodyScope.nextChild();
		NameScopeWalker anonymousClass1_1 = anonymousClass1.nextChild().nextChild().nextChild();
		NameScopeWalker anopnymousClass2Method1Body = anonymousClass1_1.nextChild().nextChild();
		assertVariableEquals(anopnymousClass2Method1Body, byte.class, "b");
		
		NameScopeWalker anonymousClass2 = method2BodyScope.nextChild();
		NameScopeWalker anopnymousClass2Method2Body = anonymousClass2.nextChild().nextChild();
		assertVariableEquals(anopnymousClass2Method2Body, InnerClassC.class, "k");
		assertVariableEquals(anonymousClass2, int.class, "counter");


	}

	private void assertVariableEquals(NameScopeWalker scope, Class<?> declaringClass, String name) {
		Variable variable = scope.getScope().resolveVariable(name);
		assertSame(declaringClass, variable.getType().getClazz());
	}

	private void assertTypeEquals(AbstractScope scope, Class<?> type, String name) {
		assertEquals(type, scope.getType(name).getClazz());
	}

	private void assertMethodsEquals(Scope scope, int size, Class<?> declaringClass, String name) {
		Collection<Method> methods = scope.resolveMethods(name);
		assertNotNull(methods);
		assertEquals(size, methods.size());
		for (Method method : methods) {
			assertSame(declaringClass, method.getDeclaringClass());
		}

	}

	private void assertFieldEquals(AbstractScope scope, Class<?> type, Class<?> declaringClass, String name) {
		Field field = ((FieldVariable) scope.resolveVariable(name)).getField();
		assertNotNull(field);
		assertSame(type, field.getType());
		assertSame(declaringClass, field.getDeclaringClass());
	}

}