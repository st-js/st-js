package org.stjs.generator;

import japa.parser.ast.Node;
import java.util.HashMap;
import java.util.Map;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.QualifiedName;

public class GenerationContextFactory {

	public static PartialGenerationContext context() {
		return new PartialGenerationContext();
	}

	public static class PartialGenerationContext {

		private final Map<Node, QualifiedName<IdentifierName>> identifiers = new HashMap<Node, QualifiedName<IdentifierName>>();

		public PartialGenerationContext withIdentifier(Node Node, QualifiedName<IdentifierName> identifier) {
			identifiers.put(Node, identifier);
			return this;
		}

		private final Map<Node, QualifiedName<MethodName>> methods = new HashMap<Node, QualifiedName<MethodName>>();

		public PartialGenerationContext withMethod(Node Node, QualifiedName<MethodName> method) {
			methods.put(Node, method);
			return this;
		}

		public GenerationContext build() {
			return new GenerationContext(null) {
				@Override
				public QualifiedName<IdentifierName> resolveIdentifier(Node node) {
					return identifiers.get(node);
				}

				@Override
				public QualifiedName<MethodName> resolveMethod(Node node) {
					return methods.get(node);
				}
			};
		}
	}
}
