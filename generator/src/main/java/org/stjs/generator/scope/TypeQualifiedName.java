package org.stjs.generator.scope;

import org.stjs.generator.scope.NameType.TypeName;

public class TypeQualifiedName extends QualifiedName<TypeName>{

  private Package definitionPackage;
  
  public TypeQualifiedName(String scopeName, NameScope scope, boolean isStatic, Package definitionPackage) {
    super(scopeName, null, scope, isStatic, false, NameTypes.CLASS);
    this.definitionPackage = definitionPackage;
  }

  public Package getPackage() {
    return definitionPackage;
  }

}
