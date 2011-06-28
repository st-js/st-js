package org.stjs.generator.scope;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class ScopeTest {
	private String declaration = "class A {" + //
			"int x = 0;" + //
			"B instanceB = null;" + //
			"{" + //
			"	instanceB.y = 2;" + //
			"}" + //

			"class B {" + //
			"	int y;" + //

			"	void m() {" + //
			"		x++;" + //
			"	}" + //
			"}" + //
			"}";

	@Test
	public void testScope() throws ParseException {
		try {
			CompilationUnit cu = null;
			// parse the file
			cu = JavaParser.parse(new ByteArrayInputStream(declaration.getBytes()));
			ScopeVisitor scopes = new ScopeVisitor();
			cu.accept(scopes, true);
			System.out.println(scopes.getRootScope());
		} finally {

		}

	}
}
