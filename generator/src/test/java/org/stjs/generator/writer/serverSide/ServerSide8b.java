package org.stjs.generator.writer.serverSide;

import org.stjs.javascript.annotation.ServerSide;

public class ServerSide8b {
	@ServerSide
	public String serverSideField;

	public void method() {
		serverSideField = "abc";
	}

}
