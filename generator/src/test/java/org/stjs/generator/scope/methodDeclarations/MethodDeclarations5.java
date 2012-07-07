package org.stjs.generator.scope.methodDeclarations;

public class MethodDeclarations5 {

	public interface Inner {
		public void method();
	}

	public boolean func(Inner i) {
		return i.equals(null);
	}
}
