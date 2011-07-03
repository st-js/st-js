package org.stjs.generator.scope;

import static org.stjs.generator.scope.ScopeAssert.assertScope;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.stjs.generator.handlers.XmlVisitor;

public class ScopeTest {

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
		ScopeVisitor scopes = new ScopeVisitor(new File(clazz), Thread.currentThread().getContextClassLoader());
		NameScope rootScope = new FullyQualifiedScope(new File(clazz));
		scopes.visit(cu, rootScope);
		// rootScope.dump("");

		NameResolverVisitor resolver = new NameResolverVisitor(rootScope);
		resolver.visit(cu, new NameScopeWalker(rootScope));

		// dumpXML(cu);
		// System.out.println(resolver.getResolvedIdentifiers());
		return resolver;
	}

	@Test
	public void testScopeParam() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");

		assertScope(resolver).line(17).column(20, 2).assertName("param")
				.assertScopePath("root.import.parent-ParentDeclaration1.type-Declaration1.param-15");
	}

	@Test
	public void testScopeVariable() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScope(resolver).line(17).column(28, 2).assertName("var2")
				.assertScopePath("root.import.parent-ParentDeclaration1.type-Declaration1.param-15.block-15");
	}

	@Test
	public void testScopeType() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScope(resolver).line(17).column(35, 2).assertName("type")
				.assertScopePath("root.import.parent-ParentDeclaration1.type-Declaration1");
	}

	@Test
	public void testScopeInnerOuter() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScope(resolver)
				.line(24)
				.column(28, 4)
				.assertName("type")
				.assertScopePath(
						"root.import.parent-ParentDeclaration1.type-Declaration1.param-15.block-15.anonymous-19");
		assertScope(resolver).line(25).column(28, 4).assertNull();
		// TODO - how to handle the outer value Declaration1.this.type
		assertScope(resolver).line(26).column(28, 4).assertName("out")
				.assertScopePath("root.import.parent-ParentDeclaration1.type-Declaration1");
	}

	@Test
	public void testScopeParent() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		// 27:int exp6 = parentPrivate + parentProtected + parentPackage + parentPublic;
		// parentPrivate resolves to the import not the private field
		assertScope(resolver).line(27).column(28, 4).assertName("parentPrivate").assertScopePath("root.import");
		assertScope(resolver).line(27).column(44, 4).assertName("parentProtected")
				.assertScopePath("root.import.parent-ParentDeclaration1");
		assertScope(resolver).line(27).column(62, 4).assertName("parentPackage")
				.assertScopePath("root.import.parent-ParentDeclaration1");
		assertScope(resolver).line(27).column(78, 4).assertName("parentPublic")
				.assertScopePath("root.import.parent-ParentDeclaration1");
	}

	@Test
	public void testScopeImport() throws ParseException, IOException {
		NameResolverVisitor resolver = getNameResolver("test/Declaration1.java");
		assertScope(resolver).line(17).column(54, 2).assertName("stat").assertScopePath("root.import");
	}

	// TODO the same for methods with Declaration2
}
