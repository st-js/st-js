package org.stjs.generator.scope.simple;

import static org.stjs.generator.scope.simple.SimpleClass.AmbiguousName;
import static org.stjs.generator.scope.simple.SimpleClass.field;
import static org.stjs.generator.scope.simple.SimpleClass.method;
import static org.stjs.generator.scope.simple.SimpleClass.AmbiguousName.InnerClassLevel2.innerField;

import org.stjs.generator.scope.inner.ClassDeclaringInnerClass.InnerClass;
import org.stjs.generator.scope.simple.SimpleClass.AmbiguousName;
import org.stjs.generator.scope.simple.SimpleClass.AmbiguousName.InnerClassLevel2;
import org.stjs.generator.scope.simple.SimpleClass.InnerClass2;

@SuppressWarnings("unused")
public class ClassWithCrazyImports {

	public static class InnerClassC {
	}

	private void m(int z, InnerClassC cc, InnerClass tt, String m) {

		Integer f = field;
		method();
		InnerClassLevel2 x;
		Integer innerFieldx = innerField;
		SimpleClass y;
		InnerClass2 k;
		AmbiguousName n = AmbiguousName;
		AmbiguousName();
	}

	public static void crazyInnerClasses() {
		new Runnable() {

			@Override
			public void run() {
				new Runnable() {

					@Override
					public void run() {
						byte b;
					}
				}.run();

			}
		}.run();
		new Runnable() {

			int counter;

			@Override
			public void run() {
				InnerClassC k;

			}
		}.run();
	}
}
