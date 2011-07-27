package org.stjs.generator;

import java.util.HashMap;
import java.util.Map;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.QualifiedName;

public class GenerationContextFactory {

  public static PartialGenerationContext context() {
    return new PartialGenerationContext();
  }
  public static class PartialGenerationContext {
    
    private Map<SourcePosition, QualifiedName<IdentifierName>> identifiers = new HashMap<SourcePosition, QualifiedName<IdentifierName>>();
    
    public PartialGenerationContext withIdentifier(QualifiedName<IdentifierName> identifier) {
      identifiers.put(new SourcePosition(0,0), identifier);
      return this;
    }
  
    public GenerationContext build() {
      return new GenerationContext(null, null, identifiers);
    }
  }
}
