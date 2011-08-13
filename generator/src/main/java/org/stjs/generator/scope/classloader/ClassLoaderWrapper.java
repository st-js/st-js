package org.stjs.generator.scope.classloader;

import org.stjs.generator.handlers.utils.Option;
import org.stjs.generator.scope.path.QualifiedPath.QualifiedMethodPath;

public class ClassLoaderWrapper {

  private final ClassLoader classLoader;

  public ClassLoaderWrapper(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }
  
  public boolean hasClass(String name) {
    return loadClass(name).isDefined();
  }
  
  public Option<ClassWrapper> loadClass(String name) {
    try {
      return Option.<ClassWrapper>some(new ClassWrapper(classLoader.loadClass(name)));
    } catch (ClassNotFoundException e) {
      return Option.none();
    }
  }

  public Option<ClassWrapper> loadClass(QualifiedMethodPath path) {
    StringBuilder className = new StringBuilder(path.getOuterClassQualifiedName());
    if (path.getInnerClassesName() != null) {
      for (String innerClass : path.getInnerClassesName().split("\\.")) {
        className.append("$");
        className.append(innerClass);
      }
    }
    return loadClass(className.toString());
  }
}
