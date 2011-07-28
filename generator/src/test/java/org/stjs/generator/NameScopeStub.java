package org.stjs.generator;

import org.stjs.generator.scope.NameScope;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;
import org.stjs.generator.scope.QualifiedName;

public class NameScopeStub extends NameScope {

  public NameScopeStub() {
    super(null, null, null);
    // TODO Auto-generated constructor stub
  }

  @Override 
  protected QualifiedName<MethodName> resolveMethod(SourcePosition pos, String name,
      NameScope currentScope) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected QualifiedName<IdentifierName> resolveIdentifier(SourcePosition pos, String name,
      NameScope currentScope) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected QualifiedName<TypeName> resolveType(SourcePosition pos, String name,
      NameScope currentScope) {
    // TODO Auto-generated method stub
    return null;
  }

}
