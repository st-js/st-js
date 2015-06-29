package org.stjs.generator.writer.serverSide;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.JavascriptFileGenerationException;

public class ServerSideTest extends AbstractStjsTest {
	@Test
	public void testFieldNotGenerated() {
		assertCodeDoesNotContain(ServerSide1.class, "serverSideField");
	}

	@Test
	public void testMethodNotGenerated() {
		assertCodeDoesNotContain(ServerSide2.class, "serverSideMethod");
	}

	@Test
	public void testConstructorNotGenerated() {
		assertCodeDoesNotContain(ServerSide3.class, "serverSideParam");
	}

	@Test
	public void tesSkipFieldCheck() {
		generate(ServerSide4.class);
	}

	@Test
	public void tesSkipMethodCheck() {
		generate(ServerSide5.class);
	}

	@Test
	public void tesSkipMethodOverloadCheck1() {
		generate(ServerSide6a.class);
	}

	@Test
	public void tesSkipMethodOverloadCheck2() {
		generate(ServerSide6b.class);
	}

	@Test
	public void tesSkipConstructorOverloadCheck() {
		generate(ServerSide7.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void tesDetectServerSideFieldAccess() {
		generate(ServerSide8.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void tesDetectOwnServerSideFieldAccess() {
		generate(ServerSide8b.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void tesDetectServerSideMethodAccess() {
		generate(ServerSide9.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void tesDetectOwnServerSideMethodAccess() {
		generate(ServerSide9b.class);
	}

	@Test(expected = JavascriptFileGenerationException.class)
	public void tesDetectServerSideConstructorAccess() {
		generate(ServerSide10.class);
	}
}
