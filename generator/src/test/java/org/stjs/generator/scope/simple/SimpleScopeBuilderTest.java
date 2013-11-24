package org.stjs.generator.scope.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConfiguration;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.name.DefaultNameProvider;
import org.stjs.generator.scope.AbstractScope;
import org.stjs.generator.scope.CompilationUnitScope;
import org.stjs.generator.scope.Scope;
import org.stjs.generator.scope.ScopeBuilder;
import org.stjs.generator.scope.inner.ClassDeclaringInnerClass.InnerClass;
import org.stjs.generator.scope.simple.ClassWithCrazyImports.InnerClassC;
import org.stjs.generator.scope.simple.SimpleClass.AmbiguousName;
import org.stjs.generator.scope.simple.SimpleClass.AmbiguousName.InnerClassLevel2;
import org.stjs.generator.scope.simple.SimpleClass.InnerClass2;
import org.stjs.generator.type.ClassLoaderWrapper;
import org.stjs.generator.type.FieldWrapper;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.variable.Variable;
import org.stjs.generator.visitor.SetParentVisitor;

public class SimpleScopeBuilderTest {

	@Test
	public void test() throws ParseException, IOException {
		String path = ClassWithCrazyImports.class.getName().replace('.', File.separatorChar);
		path = "src/test/java/" + path + ".java";
		CompilationUnit compilationUnit = JavaParser.parse(new File(path));
		GeneratorConfiguration config = new GeneratorConfigurationBuilder().allowedPackage("org.stjs.generator").build();
		ClassLoaderWrapper classLoader = new ClassLoaderWrapper(Thread.currentThread().getContextClassLoader(), config.getAllowedPackages(),
				config.getAllowedJavaLangClasses());
		GenerationContext context = new GenerationContext(new File(path), config, new DefaultNameProvider(), null);
		// set the parent of each node
		compilationUnit.accept(new SetParentVisitor(), context);
		ScopeBuilder builder = new ScopeBuilder(classLoader, context);
		CompilationUnitScope scope = new CompilationUnitScope(classLoader, context);

		builder.visit(compilationUnit, scope);
		// : assert all imports have been resolved

		assertFieldEquals(scope, Integer.class, SimpleClass.class, "field");
		assertMethodsEquals(scope, 2, SimpleClass.class, "method");
		assertFieldEquals(scope, Integer.class, InnerClassLevel2.class, "innerField");
		assertTypeEquals(scope, SimpleClass.class, "SimpleClass");

		assertTypeEquals(scope, AmbiguousName.class, "AmbiguousName");
		assertFieldEquals(scope, AmbiguousName.class, SimpleClass.class, "AmbiguousName");
		assertMethodsEquals(scope, 1, SimpleClass.class, "AmbiguousName");

		assertTypeEquals(scope, InnerClassLevel2.class, "InnerClassLevel2");
		assertTypeEquals(scope, InnerClass2.class, "InnerClass2");
		// WARN asterisk imports are removed by the eclipse
		// assertEquals("test.generator", getOnlyElement(scope.getTypeImportOnDemandSet()).toString());

		NameScopeWalker walker = new NameScopeWalker(scope);
		NameScopeWalker classScope = walker.nextChild();
		classScope.nextChild();// inner type
		NameScopeWalker methodScope = classScope.nextChild();

		assertVariableEquals(methodScope, int.class, "z");
		assertVariableEquals(methodScope, ClassWithCrazyImports.InnerClassC.class, "cc");
		assertVariableEquals(methodScope, InnerClass.class, "tt");
		assertVariableEquals(methodScope, String.class, "m");

		NameScopeWalker methodBodyScope = methodScope;// .nextChild();
		assertVariableEquals(methodBodyScope, Integer.class, "f");
		assertVariableEquals(methodBodyScope, InnerClassLevel2.class, "x");
		assertVariableEquals(methodBodyScope, SimpleClass.class, "y");
		assertVariableEquals(methodBodyScope, InnerClass2.class, "k");

		assertMethodsEquals(methodBodyScope.getScope(), 2, SimpleClass.class, "method");

		NameScopeWalker method2BodyScope = classScope.nextChild();// .nextChild();
		NameScopeWalker anonymousClass1 = method2BodyScope.nextChild();
		NameScopeWalker anonymousClass1_1 = anonymousClass1.nextChild().nextChild();// .nextChild();
		NameScopeWalker anopnymousClass2Method1Body = anonymousClass1_1.nextChild();// .nextChild();
		assertVariableEquals(anopnymousClass2Method1Body, byte.class, "b");

		NameScopeWalker anonymousClass2 = method2BodyScope.nextChild();
		NameScopeWalker anopnymousClass2Method2Body = anonymousClass2.nextChild();// .nextChild();
		assertVariableEquals(anopnymousClass2Method2Body, InnerClassC.class, "k");
		assertVariableEquals(anonymousClass2, int.class, "counter");

	}

	private void assertVariableEquals(NameScopeWalker scope, Class<?> declaringClass, String name) {
		Variable variable = scope.getScope().resolveVariable(name).getVariable();
		assertSame(declaringClass, variable.getType().getType());
	}

	private void assertTypeEquals(AbstractScope scope, Class<?> type, String name) {
		assertEquals(type, scope.getType(name).getType());
	}

	private void assertMethodsEquals(Scope scope, int size, Class<?> declaringClass, String name) {
		MethodWrapper method = scope.resolveMethod(name).getMethod();
		assertNotNull(method);
		assertSame(declaringClass, method.getOwnerType().getType());

	}

	private void assertFieldEquals(AbstractScope scope, Class<?> type, Class<?> declaringClass, String name) {
		FieldWrapper field = ((FieldWrapper) scope.resolveVariable(name).getVariable());
		assertNotNull(field);
		assertSame(type, field.getType().getType());
		assertSame(declaringClass, field.getOwnerType().getType());
	}

}
