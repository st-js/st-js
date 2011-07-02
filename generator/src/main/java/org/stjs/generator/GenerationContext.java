package org.stjs.generator;

import java.util.Map;

import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.QualifiedName;

/**
 * This class can resolve an identifier or a method in the given source context. There is one context create for each
 * generation process.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class GenerationContext {
	private final Map<SourcePosition, QualifiedName<MethodName>> resolvedMethods;
	private final Map<SourcePosition, QualifiedName<IdentifierName>> resolvedIdentifiers;

	private boolean skipHandlers = false;

	public GenerationContext(Map<SourcePosition, QualifiedName<MethodName>> resolvedMethods,
			Map<SourcePosition, QualifiedName<IdentifierName>> resolvedIdentifiers) {
		this.resolvedMethods = resolvedMethods;
		this.resolvedIdentifiers = resolvedIdentifiers;
	}

	public QualifiedName<MethodName> resolveMethod(SourcePosition pos) {
		return resolvedMethods.get(pos);
	}

	public QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos) {
		return resolvedIdentifiers.get(pos);
	}

	public GenerationContext skipHandlers() {
		skipHandlers = true;
		return this;
	}

	public GenerationContext checkHandlers() {
		skipHandlers = false;
		return this;
	}

	public boolean isSkipHandlers() {
		return skipHandlers;
	}

}
