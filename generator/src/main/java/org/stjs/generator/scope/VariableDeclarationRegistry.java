package org.stjs.generator.scope;

import static java.util.Collections.unmodifiableSet;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.stjs.generator.ASTNodeData;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.TypeName;
import org.stjs.generator.scope.QualifiedName.NameTypes;
import org.stjs.generator.utils.Option;

class VariableDeclarationRegistry {

	private final Map<String, Type> variables = new HashMap<String, Type>();
	private final NameScope scope;

	public VariableDeclarationRegistry(NameScope scope) {
		this.scope = scope;
	}

	public void addVariable(Type type, String var) {
		variables.put(var, type);
	}
		
	Option<QualifiedName<IdentifierName>> resolveIdentifier(String name) {
		if (variables.containsKey(name)) {
			Type type = getDefinitionType(variables.get(name));
			ASTNodeData nodeData = (ASTNodeData)type.getData();
			if (nodeData != null) {
				QualifiedName<TypeName> resolvedType = (QualifiedName<TypeName>) nodeData.getQualifiedName();
				if (resolvedType != null) {
					return Option.some(new QualifiedName<IdentifierName>(scope, false, NameTypes.VARIABLE, resolvedType.getDefinitionPoint().getOrNull()));
				}
			}
			// probably (type instanceof PrimitiveType)
			return Option.some(new QualifiedName<IdentifierName>(scope, false, NameTypes.VARIABLE));
		}
		return Option.none();
	}
	
	private Type getDefinitionType(Type type) {
		if (type instanceof ReferenceType) {
			return getDefinitionType(((ReferenceType)type).getType());
		}
		return type;
	}

	public boolean containsKey(String name) {
		return variables.containsKey(name);
	}

	public Set<String> getVariables() {
		return unmodifiableSet(variables.keySet());
	}
}