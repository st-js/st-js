package org.stjs.generator.scope.inner;

import static org.stjs.generator.scope.ScopeTestHelper.assertResolvedAnonymousClass;
import static org.stjs.generator.scope.ScopeTestHelper.assertResolvedMethod;
import japa.parser.ParseException;

import java.io.IOException;

import org.junit.Test;
import org.stjs.javascript.functions.Callback0;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Callback2;

public class InnerClassScopeTest {

	@Test
	public void resolvesInnerClassesInOtherFiles() throws ParseException, IOException {
		assertResolvedMethod(ClassUsingInnerClass.class, "doSth", 1, ClassDeclaringInnerClass.InnerClass.class);
		assertResolvedMethod(ClassUsingInnerClass.class, "doSth", 2, ClassDeclaringInnerClass.InnerClass.class);
		assertResolvedMethod(ClassUsingInnerClass.class, "doSth", 3, ClassDeclaringInnerClass.InnerClass.class);
	}

	@Test
	public void resolvesNonStaticInner() throws ParseException, IOException {
		assertResolvedMethod(ClassDeclaringInnerClass2.class, "doSth", 1, ClassDeclaringInnerClass2.InnerClass.class);
	}

	@Test
	public void fieldVsInnerClass() throws ParseException, IOException {
		assertResolvedMethod(FieldsVsInnerClass.class, "print", 1, FieldsVsInnerClass.class);
		assertResolvedMethod(FieldsVsInnerClass.class, "print", 2, FieldsVsInnerClass.MyInnerClass2.class);
	}

	@Test
	public void testAnonClass1() {
		assertResolvedAnonymousClass(AnonymousInnerClass.class, 1, MyClass.class);
		assertResolvedAnonymousClass(AnonymousInnerClass.class, 2, MyClass.class);
		assertResolvedAnonymousClass(AnonymousInnerClass.class, 3, MyClass.class);
		assertResolvedAnonymousClass(AnonymousInnerClass.class, 4, MyClass2.class);
		assertResolvedAnonymousClass(AnonymousInnerClass.class, 5, MyClass.class);
		assertResolvedAnonymousClass(AnonymousInnerClass.class, 6, MyClass2.class);
	}

	@Test
	public void testAnonClass2() {
		assertResolvedAnonymousClass(AnonymousInnerClass2.class, 1, Callback0.class);
		assertResolvedAnonymousClass(AnonymousInnerClass2.class, 2, Callback1.class);
		assertResolvedAnonymousClass(AnonymousInnerClass2.class, 3, Callback2.class);
	}

	@Test
	public void testAnonClass3() {
		assertResolvedAnonymousClass(AnonymousInnerClass2Reversed.class, 1, Callback2.class);
		assertResolvedAnonymousClass(AnonymousInnerClass2Reversed.class, 2, Callback0.class);
		assertResolvedAnonymousClass(AnonymousInnerClass2Reversed.class, 3, Callback1.class);
	}

	@Test
	public void testAnonClassWithAbstractMethod() {
		assertResolvedAnonymousClass(AnonymousInnerClass3.class, 1, Callback2.class);
	}

	@Test
	public void testAnonClassAndInnerClass() {
		assertResolvedAnonymousClass(AnonymousInnerClass4.class, 1, Callback2.class);
	}
}
