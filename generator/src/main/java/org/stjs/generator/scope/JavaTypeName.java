package org.stjs.generator.scope;

import org.stjs.generator.handlers.utils.Option;

public class JavaTypeName {

  private final Option<String> name;
  
  private static final JavaTypeName anonymous = new JavaTypeName(null);

  public JavaTypeName(String name) {
    this.name = Option.of(name);
  }
  
  public static JavaTypeName anonymous() {
    return anonymous;
  }

  public Option<String> getName() {
    return name;
  }
  
  public boolean isAnonymous() {
    return name.isEmpty();
  }
  
  
}
