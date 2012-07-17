package org.stjs.generator.writer.innerTypes;

public class InnerTypes15 {
	
	@SuppressWarnings("unused")
	public static void main(){
		Inner.InnerDeep deep = new Inner.InnerDeep();
	}
	
	private static class Inner {
		@SuppressWarnings("unused")
		private String a = "a";
		private static class InnerDeep {
			@SuppressWarnings("unused")
			private String b = "b";
		}		
	}
}
