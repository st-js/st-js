package org.stjs.generator.writer.innerTypes;

public class InnerTypes12 {
	
	@SuppressWarnings("unused")
	public static void main(){
		Inner.InnerDeep deep = new Inner.InnerDeep();
	}
	
	private static class Inner {
		private String a = "a";
		private static class InnerDeep {
			private String b = "b";
		}		
	}
}
