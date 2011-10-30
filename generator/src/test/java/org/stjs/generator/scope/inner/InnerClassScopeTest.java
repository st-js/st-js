package org.stjs.generator.scope.inner;

import static org.stjs.generator.scope.ScopeTestHelper.assertResolvedMethod;
import japa.parser.ParseException;

import java.io.IOException;

import org.junit.Test;

public class InnerClassScopeTest {

	@Test
	public void resolvesInnerClassesInOtherFiles() throws ParseException, IOException {
		assertResolvedMethod(ClassUsingInnerClass.class, "doSth", 1, ClassDeclaringInnerClass.InnerClass.class);
		assertResolvedMethod(ClassUsingInnerClass.class, "doSth", 2, ClassDeclaringInnerClass.InnerClass.class);
		assertResolvedMethod(ClassUsingInnerClass.class, "doSth", 3, ClassDeclaringInnerClass.InnerClass.class);
	}

	@Test
	public void fieldVsInnerClass() throws ParseException, IOException {
		assertResolvedMethod(FieldsVsInnerClass.class, "print", 1, FieldsVsInnerClass.class);
		assertResolvedMethod(FieldsVsInnerClass.class, "print", 2, FieldsVsInnerClass.MyInnerClass2.class);
	}
}
