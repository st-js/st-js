package test.generator.scopes.p;

import static test.generator.scopes.SimpleClass.field;
import static test.generator.scopes.SimpleClass.method;
import static test.generator.scopes.SimpleClass.AmbiguousName.InnerClassLevel2.innerField;
import test.generator.scopes.SimpleClass;
import static test.generator.scopes.SimpleClass.AmbiguousName; // single import for field, method and type!
import test.generator.scopes.SimpleClass.AmbiguousName.InnerClassLevel2;
import test.generator.scopes.SimpleClass.AmbiguousName.InnerClassLevel2; // duplicate import
import static test.generator.scopes.SimpleClass.InnerClass2;// static import of inner class, huh?
import test.generator.*;
import test.innerclasses.ClassDeclaringInnerClass.InnerClass;

public class ClassWithCrazyImports {
	
	public static class InnerClassC {}
	
	
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
