package org.stjs.generator.scope.methodDeclarations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.stjs.generator.scope.ScopeTestHelper.assertResolvedMethod;
import static org.stjs.generator.scope.ScopeTestHelper.resolveName;

import org.junit.Test;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.scope.methodDeclarations.sub.MethodDeclarations2;

public class MethodDeclarationsTest {

	@Test
	public void resolvesMethods() {
		assertResolvedMethod(ClassUsingStaticMethod.class, "doSth", 1, ClassDefiningStaticMethod.class);
		assertResolvedMethod(ClassUsingStaticMethod.class, "doSth", 2, ClassDefiningStaticMethod.class);
		assertResolvedMethod(ClassUsingStaticMethod.class, "doSth", 3, ClassDefiningStaticMethod.class);
	}

	// TODO the same for methods with Declaration2
	@Test
	public void testMethodsSameName() {
		try {
			resolveName(SameNameMethods.class);
			fail("Expected " + JavascriptFileGenerationException.class);
		} catch (JavascriptFileGenerationException ex) {
			assertEquals(23, ex.getSourcePosition().getLine());
			assertEquals(9, ex.getSourcePosition().getColumn());
		}
	}

	@Test
	public void testInnerClassSamePackage() {
		assertResolvedMethod(MethodDeclarations3.class, "method", 1, MethodDeclarations4.Inner.class);
	}

	@Test
	public void testInnerClassOtherPackage() {
		assertResolvedMethod(MethodDeclarations1.class, "method", 1, MethodDeclarations2.Inner.class);
	}

	@Test
	public void testInheritObjectForInterfaces() {
		assertResolvedMethod(MethodDeclarations5.class, "equals", 1, Object.class);
	}
}
