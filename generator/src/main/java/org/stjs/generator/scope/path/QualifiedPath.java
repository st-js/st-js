package org.stjs.generator.scope.path;



public class QualifiedPath {

  private String packag;
  
  private String clazz;
  
  public QualifiedPath(String packag, String clazz) {
    this.packag = packag;
    this.clazz = clazz;
  }
  
  public String getClassSimpleName() {
    return clazz;
  }
  
  public String getClassQualifiedName() {
    return packag != null ? join(packag, clazz) : clazz;
  }

  
  public static class QualifiedFieldPath extends QualifiedPath {
    private String field;

    public QualifiedFieldPath(String packag, String clazz, String field) {
      super(packag, clazz);
      this.field = field;
    }

    public String getFieldName() {
      return field;
    }
  }
  
  public static class QualifiedMethodPath extends QualifiedPath {
    private String method;

    public QualifiedMethodPath(String packag, String clazz, String method) {
      super(packag, clazz);
      this.method = method;
    }
    
    public String getMethodName() {
      return method;
    }

    public String getFullName() {
     return join(getClassQualifiedName(),method);
    }
  }
  
  public static String join(String str1, String str2) {
    return str1+"."+str2;
  }
  
  public static String afterLastDot(String str) {
    if (str == null) {
      return null;
    }
    int lastIndex = str.lastIndexOf(".");
    if (lastIndex>=0) {
      return  str.substring(lastIndex+1);
    }
    return str;
  }

  public static String beforeLastDot(String str) {
    if (str == null) {
      return null;
    }
    int lastIndex = str.lastIndexOf(".");
    if (lastIndex>=0) {
      return  str.substring(0, lastIndex);
    }
    return null;
  }
  
  public static String beforeFirstDot(String str) {
    if (str == null) {
      return null;
    }
    int firstIndex = str.indexOf(".");
    if (firstIndex>=0) {
      return  str.substring(0, firstIndex);
    }
    return null;
  }
  
  public static QualifiedMethodPath withMethod(String path) {
    String methodName = afterLastDot(path);
    String className = afterLastDot(beforeLastDot(path));
    String packageName = beforeLastDot(beforeLastDot(path));
    return new QualifiedMethodPath(packageName, className, methodName);
  }

  public static QualifiedFieldPath withField(String path) {
    String fieldName = afterLastDot(path);
    String className = afterLastDot(beforeLastDot(path));
    String packageName = beforeLastDot(beforeLastDot(path));
    return new QualifiedFieldPath(packageName, className, fieldName);
  }

  public static QualifiedPath withClass(Class<?> clazz) {
    if (clazz == null) {
      return null;
    }
    return new QualifiedPath(clazz.getPackage().getName(), clazz.getSimpleName());
  }

  public static QualifiedPath withClassName(String name) {
    String className = afterLastDot(name);
    String packageName = beforeLastDot(name);
    return new QualifiedPath(packageName, className);
  }

  public String getClassName(boolean useQualifiedNames) {
    return useQualifiedNames ? getClassQualifiedName() : getClassSimpleName();
  }


  
}
