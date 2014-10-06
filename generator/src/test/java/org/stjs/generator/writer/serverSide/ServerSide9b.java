package org.stjs.generator.writer.serverSide;

import org.stjs.javascript.annotation.ServerSide;

public class ServerSide9b {
	@ServerSide
	public String serverSideMethod() {
		return "abc";
	}

	public void method() {
		String v = serverSideMethod();
	}
}
