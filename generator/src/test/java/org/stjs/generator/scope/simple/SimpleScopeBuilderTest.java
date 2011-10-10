package org.stjs.generator.scope.simple;


public class SimpleScopeBuilderTest {
	/*
	 * @Test public void test() throws ParseException, IOException { String path =
	 * ClassWithCrazyImports.class.getName().replace('.', File.separatorChar); path = "src/test/java/" + path + ".java";
	 * CompilationUnit compilationUnit = JavaParser.parse(new File(path)); SimpleScopeBuilder builder = new
	 * SimpleScopeBuilder(new ClassLoaderWrapper(Thread.currentThread() .getContextClassLoader())); AbstractScope scope
	 * = new CompilationUnitScope(ClassWithCrazyImports.class.getPackage()); builder.visit(compilationUnit, scope); //
	 * TODO : assert all imports have been resolved
	 * 
	 * assertFieldEquals(scope, Integer.class, SimpleClass.class, "field"); assertMethodsEquals(scope, 2,
	 * SimpleClass.class, "method"); assertFieldEquals(scope, Integer.class, InnerClassLevel2.class, "innerField");
	 * assertTypeEquals(scope, SimpleClass.class, "SimpleClass");
	 * 
	 * assertTypeEquals(scope, AmbiguousName.class, "AmbiguousName"); assertFieldEquals(scope, AmbiguousName.class,
	 * SimpleClass.class, "AmbiguousName"); assertMethodsEquals(scope, 1, SimpleClass.class, "AmbiguousName");
	 * 
	 * assertTypeEquals(scope, InnerClassLevel2.class, "InnerClassLevel2"); assertTypeEquals(scope, InnerClass2.class,
	 * "InnerClass2");
	 * 
	 * assertEquals("test.generator", getOnlyElement(scope.getTypeImportOnDemandSet()).toString()); }
	 * 
	 * private void assertTypeEquals(AbstractScope scope, Class<?> type, String name) { assertEquals(type,
	 * scope.getType(name).getClazz()); }
	 * 
	 * private void assertMethodsEquals(AbstractScope scope, int size, Class<?> declaringClass, String name) {
	 * Collection<Method> methods = scope.getMethods(name); assertNotNull(methods); assertEquals(size, methods.size());
	 * for (Method method : methods) { assertSame(declaringClass, method.getDeclaringClass()); }
	 * 
	 * }
	 * 
	 * private void assertFieldEquals(AbstractScope scope, Class<?> type, Class<?> declaringClass, String name) { Field
	 * field = scope.getField(name); assertNotNull(field); assertSame(type, field.getType()); assertSame(declaringClass,
	 * field.getDeclaringClass()); }
	 */
}