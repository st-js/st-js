package org.stjs.generator.scope;

import org.stjs.generator.handlers.utils.Option;

/**
 * Java types name, with support for inner classes.
 * Option.None represents anonymous classes, Option.some named classes.
 * For instance SuperClass.InnerClass$1
 * will be a chain of
 * 
 * None -> Some<InnerClass> -> Some<SuperClass> 
 * 
 */
public class JavaTypeName {

  private final Option<String> simpleName;
  private final Option<JavaTypeName> enclosingTypeName;
  
  public JavaTypeName(String simpleName, JavaTypeName enclosingTypeName) {
   this.simpleName  = Option.of(simpleName);
   this.enclosingTypeName = Option.of(enclosingTypeName);
  }
  
  public JavaTypeName(String simpleName) {
    this.simpleName  = Option.of(simpleName);
    this.enclosingTypeName = Option.none();
   }

  public Option<String> getSimpleName() {
    return simpleName;
  }
  
  public boolean isAnonymous() {
    return getSimpleName().isEmpty();
  }

  public Option<String> getFullyQualifiedString() {
    if (isAnonymous()) {
      return Option.none();
    }
    if (enclosingTypeName.isEmpty()) {
      return simpleName;
    }
    Option<String> enclosingQualifiedName = enclosingTypeName.getOrThrow().getFullyQualifiedString();
    if (enclosingQualifiedName.isEmpty()) {
      return Option.none();
    }
    return Option.some(
        enclosingQualifiedName.getOrThrow() + 
        "." + 
        simpleName.getOrThrow());
  }
  
  
}
