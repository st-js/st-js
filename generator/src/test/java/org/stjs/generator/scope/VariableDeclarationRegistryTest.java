package org.stjs.generator.scope;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import junit.framework.Assert;

import org.junit.Test;
import org.stjs.generator.ASTNodeData;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.QualifiedName.NameTypes;
import org.stjs.generator.scope.path.QualifiedPath;
import org.stjs.generator.utils.Option;


public class VariableDeclarationRegistryTest {

	private NameScope scope = new FullyQualifiedScope(null, null);
	
	@Test
	public void retrievesVariableWithUndefType() throws Exception {
		VariableDeclarationRegistry registry = new VariableDeclarationRegistry(scope);
		registry.addVariable(new PrimitiveType(), "var1");
		QualifiedName<IdentifierName>  qName = registry.resolveIdentifier("var1").getOrThrow();
		assertSame(NameTypes.VARIABLE, qName.getType());
		assertSame(scope, qName.getScope());
		assertTrue(qName.getDefinitionPoint().isEmpty());
	}
	
	@Test
	public void returnsNoneIfNotDef() throws Exception {
		VariableDeclarationRegistry registry = new VariableDeclarationRegistry(scope);
		registry.addVariable(new PrimitiveType(), "var1");
		assertTrue(registry.resolveIdentifier("toto").isEmpty());
	}
	
	@Test
	public void passesTypeDefinitionPointForRefType() throws Exception {
		VariableDeclarationRegistry registry = new VariableDeclarationRegistry(scope);
		ClassOrInterfaceType actualType = new ClassOrInterfaceType();
		ASTNodeData nodeData = new ASTNodeData();
		nodeData.setQualifiedName(new QualifiedName<NameType>(scope, false, NameTypes.CLASS, new JavaTypeName(QualifiedPath.withClassName("toto"))));
		actualType.setData(nodeData);
		ReferenceType refType = new ReferenceType(actualType);
		registry.addVariable(refType, "var1");
		registry.addVariable(actualType, "var2");
		QualifiedName<IdentifierName>  qName = registry.resolveIdentifier("var1").getOrThrow();
		QualifiedName<IdentifierName>  qName2 = registry.resolveIdentifier("var2").getOrThrow();
		assertEquals("toto", qName.getDefinitionPoint().getOrThrow().getSimpleName().getOrThrow());
		assertEquals("toto", qName2.getDefinitionPoint().getOrThrow().getSimpleName().getOrThrow());
	}
}
