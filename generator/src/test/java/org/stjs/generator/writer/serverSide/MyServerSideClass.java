package org.stjs.generator.writer.serverSide;

import org.stjs.javascript.annotation.ServerSide;

public class MyServerSideClass {
	@ServerSide
	public String serverSideField;

	@ServerSide
	public String serverSideMethod() {
		return "";
	}
}
