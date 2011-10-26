package org.stjs.generator.writer.scopes.p;

import static org.stjs.generator.writer.scopes.SimpleClass.field;
import static org.stjs.generator.writer.scopes.SimpleClass.method;
import static org.stjs.generator.writer.scopes.SimpleClass.AmbiguousName.InnerClassLevel2.innerField;

import org.stjs.generator.scope.ClassDeclaringInnerClass.InnerClass;
import org.stjs.generator.writer.scopes.SimpleClass;
import org.stjs.generator.writer.scopes.SimpleClass.InnerClass2;
import org.stjs.generator.writer.scopes.SimpleClass.AmbiguousName.InnerClassLevel2;


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
