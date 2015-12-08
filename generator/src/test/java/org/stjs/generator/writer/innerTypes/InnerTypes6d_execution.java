package org.stjs.generator.writer.innerTypes;

public class InnerTypes6d_execution {

	public static String main(String[] args) {
		InnerTypes6d_execution myClass = new InnerTypes6d_execution();
		return myClass.process();
	}

	public String process() {
		return new InnerClass().process();
	}

	public String getName() {
		return "MyClass";
	}

	private class InnerClass {
		public String getName() {
			return "MyClass$InnerClass";
		}

		public String process() {
			return getName() + "," + this.getName() + "," + InnerTypes6d_execution.this.getName();
		}
	}
}
