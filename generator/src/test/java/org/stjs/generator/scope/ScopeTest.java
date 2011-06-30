package org.stjs.generator.scope;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.handlers.XmlVisitor;
import org.stjs.generator.scope.NameType.IdentifierName;

public class ScopeTest {
	private static final int MY_TAB_CONFIG = 4;

	void dumpXML(CompilationUnit cu) throws IOException {
		// create the DOM
		Document dom = DocumentHelper.createDocument();
		Element root = dom.addElement("root");

		new XmlVisitor().visit(cu, root);

		// print
		// Pretty print the document to System.out
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(System.out, format);
		writer.write(dom);
	}

	private NameResolverVisitor getNameResolver(String clazz) throws ParseException, IOException {
		CompilationUnit cu = null;
		// parse the file
		cu = JavaParser.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(clazz));
		ScopeVisitor scopes = new ScopeVisitor();
		NameScope rootScope = new FullyQualifiedScope();
		scopes.visit(cu, rootScope);
		rootScope.dump("");

		NameResolverVisitor resolver = new NameResolverVisitor(rootScope);
		resolver.visit(cu, new NameScopeWalker(rootScope));

		// dumpXML(cu);
		return resolver;
	}

	@Test
	public void testScopeParam() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");

		assertScopeIdent(resolver, "param", 16, column(20, 2), "root.import.type-Declaration1.param-14");
		System.out.println(resolver.getResolvedIdentifiers());
	}

	@Test
	public void testScopeVariable() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScopeIdent(resolver, "var", 16, column(28, 2), "root.import.type-Declaration1.param-14.block-14");
	}

	@Test
	public void testScopeType() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScopeIdent(resolver, "type", 16, column(34, 2), "root.import.type-Declaration1");
	}

	@Test
	public void testScopeInnerOuter() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScopeIdent(resolver, "type", 23, column(28, 4),
				"root.import.type-Declaration1.param-14.block-14.anonymous-18");
		assertScopeIdent(resolver, null, 24, column(28, 4), "-");
		// TODO - how to handle the outer value Declaration1.this.type
		assertScopeIdent(resolver, "out", 25, column(28, 4), "root.import.type-Declaration1");
	}

	@Test
	public void testScopeParent() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScopeIdent(resolver, "parent", 16, column(34, 2), "root.import.type-Declaration1");
	}

	@Test
	public void testScopeImport() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScopeIdent(resolver, "imp", 16, column(34, 2), "root.import");
	}

	private void assertScopeIdent(NameResolverVisitor resolver, String identName, int line, int column, String scopePath) {
		QualifiedName<IdentifierName> qname = resolver.getResolvedIdentifiers().get(new SourcePosition(line, column));
		if (identName == null) {
			assertNull(qname);
			return;
		}
		assertNotNull(qname);
		assertEquals(identName, qname.getName());
		assertNotNull(qname.getScope());
		assertEquals(scopePath, qname.getScope().getPath());
	}

	/**
	 * The AST parser uses a 8-space tab when calculating the column. translate it to a 4-space tab
	 * 
	 * @param columnInEditor
	 * @param tabs
	 * @return
	 */
	private int column(int columnInEditor, int tabs) {
		return columnInEditor + (8 - MY_TAB_CONFIG) * tabs;
	}

}
