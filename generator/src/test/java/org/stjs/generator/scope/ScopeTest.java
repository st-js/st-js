package org.stjs.generator.scope;

import static junit.framework.Assert.assertEquals;
import static org.stjs.generator.handlers.utils.Sets.transform;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.stjs.generator.handlers.utils.Function;
import org.stjs.generator.handlers.utils.Lists;
import org.stjs.generator.handlers.utils.Sets;

public class ScopeTest {
	private String declaration =
			"class A {" + //
				"@ScopeIdentifiers()"+//
				"int x = 0;" + //
				
				"@ScopeIdentifiers(x)"+//
				"B instanceB = null;" + //
				"{" + //
				"	instanceB.y = 2;" + //
				"}" + //
				
				"class B {" + //
				
				"	@ScopeIdentifiers({instanceB,x})"+//
				"	int y;" + //
	
				"	void m() {" + //
				"		x++;" + //
				"	}" + //
				"}" + //
			"}";
	
	private String declarationWithWrongScope =
			"class A {" + //
				"@ScopeIdentifiers(y)"+//
				"int x = 0;" + //
			"}";

	@Test(expected=AssertionError.class)
	public void testScopeWithError() throws ParseException {
		assertScope(declarationWithWrongScope);
	}
	
	@Test
	public void testScope() throws ParseException {
		assertScope(declaration);
	}
	
	private void assertScope(String src) throws ParseException {
		CompilationUnit cu = null;
		// parse the file
		cu = JavaParser.parse(new ByteArrayInputStream(src.getBytes()));
		ScopeVisitor scopes = new ScopeVisitor() {
			public void visit(FieldDeclaration n, NameScope scope) {
				Set<String> emptySet = Collections.<String> emptySet();
				AnnotationExpr annotation = Lists.getOnlyElement(n
						.getAnnotations());
				Set<String> identifiersExpected = Sets.newHashSet();
				// TODO : visitor instead of instanceof?
				if (annotation instanceof SingleMemberAnnotationExpr) {
					Expression memberValue = ((SingleMemberAnnotationExpr) annotation)
							.getMemberValue();
					if (memberValue instanceof NameExpr) {
						identifiersExpected.add(((NameExpr) memberValue)
								.getName());
					}
					if (memberValue instanceof ArrayInitializerExpr) {
						for (Expression exp : ((ArrayInitializerExpr) memberValue)
								.getValues()) {
							identifiersExpected.add(exp.toString());
						}

					}
				}
				assertScopeEquals(emptySet, identifiersExpected, scope, n);

			}

		};
		cu.accept(scopes, new ImportScope());
	}
	
	private void assertScopeEquals(Set<String> methodNames, Set<String> identifierNames, NameScope scope, FieldDeclaration expression) {
		Function<QualifiedName<?>, String> unwrapper = new Function<QualifiedName<?>, String>() {
			@Override
			public String apply(QualifiedName<?> input) {
				return input.getName();
			}
		};
		Set<String> methods = transform(scope.getMethods(),unwrapper);
		Set<String> identifiers = transform(scope.getIdentifiers(),unwrapper);
		System.out.println(String.format("Expression \n%s\nexpected %s\ngot %s\n\n", expression, identifierNames, identifiers));
		assertEquals(methodNames, methods);
		assertEquals(identifierNames, identifiers);

		
	}
	
	
}
