package org.stjs.generator.scope.path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Test;
import org.stjs.generator.handlers.utils.Option;
import org.stjs.generator.scope.ClassResolver;
import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.generator.scope.path.QualifiedPath.QualifiedMethodPath;


public class QualifiedPathTest {

  @Test
  public void resolvesSimpleCase() {
    QualifiedMethodPath path = QualifiedPath.withMethod("MyClass.myMethod", new ClassResolver() {
      @Override
      public Option<ClassWrapper> resolveClass(String name) {
        if ("MyClass".equals(name)) {
          return Option.some(new ClassWrapper(null));
        }
        throw new AssertionError("not expected call");
      }
    });
    assertEquals("MyClass", path.getClassName(true));
    assertEquals("myMethod", path.getMethodName());
  }
  
  @Test
  public void returnsNullPathIfItDoesNotExist() {
    QualifiedMethodPath path = QualifiedPath.withMethod("MyClass.myMethod", new ClassResolver() {
      @Override
      public Option<ClassWrapper> resolveClass(String name) {
        return Option.none();
      }
    });
    assertNull(path);
  }
  
  @Test
  public void resolvesWithPackage() {
    QualifiedMethodPath path = QualifiedPath.withMethod("MyPackage.MyClass.myMethod", new ClassResolver() {
      @Override
      public Option<ClassWrapper> resolveClass(String name) {
        if ("MyPackage.MyClass".equals(name)) {
          return Option.some(new ClassWrapper(null));
        }
        return Option.none();
      }
    });
    assertEquals("MyPackage.MyClass", path.getClassName(true));
    assertEquals("myMethod", path.getMethodName());
  }
  
  @Test
  public void resolvesWithInnerClass() {
    final AtomicBoolean hasTriedOuterClass = new AtomicBoolean(false);
    QualifiedMethodPath path = QualifiedPath.withMethod("MyPackage.MyClass.MyInnerClass.myMethod", new ClassResolver() {
      @Override
      public Option<ClassWrapper> resolveClass(String name) {
        if ("MyPackage.MyClass".equals(name)) {
          return Option.some(new ClassWrapper(null));
        }
        if ("MyPackage.MyClass.MyInnerClass".equals(name)) {
          hasTriedOuterClass.set(true);
        }
        return Option.none();
      }
    });
    assertTrue(hasTriedOuterClass.get());
    assertEquals("MyPackage.MyClass.MyInnerClass", path.getClassName(true));
    assertEquals("MyInnerClass", path.getInnerClassesName());
    assertEquals("myMethod", path.getMethodName());
  }
}
