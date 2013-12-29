package org.stjs.generator.plugin.java8.writer.statements;

public class Statement1 {
	public void method(){
		try{
			int n = Math.abs(20);
		} catch(Exception | Error e){
			throw new RuntimeException(e);
		}
	}
}
