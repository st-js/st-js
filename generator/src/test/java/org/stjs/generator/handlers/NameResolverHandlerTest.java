package org.stjs.generator.handlers;

import static org.stjs.generator.GeneratedScriptTester.handlerTester;
import static org.stjs.generator.GenerationContextFactory.context;
import static org.stjs.generator.NodesFactory.fieldAccess;
import static org.stjs.generator.NodesFactory.nameExpr;
import static org.stjs.generator.scope.path.QualifiedPath.withClassName;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import org.junit.Test;
import org.stjs.generator.NodesFactory;
import org.stjs.generator.scope.ImportScope;
import org.stjs.generator.scope.JavaTypeName;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.QualifiedName;
import org.stjs.generator.scope.QualifiedName.NameTypes;
import org.stjs.generator.scope.TypeScope;


public class NameResolverHandlerTest {

  @Test
  public void shouldQualifyStaticMememberAccess() throws Exception {
    TypeScope scope = new TypeScope(null, null, new JavaTypeName(withClassName("MyClass")), null);
    NameExpr node = nameExpr("MY_CONSTANT");
    handlerTester(NameResolverHandler.class, false).
    assertGenerateString("MyClass.MY_CONSTANT",
      node,
      context()
      .withIdentifier(node, new QualifiedName<IdentifierName>(scope, true, NameTypes.CLASS, scope.getDeclaredTypeName())));
  }
  
  @Test
  public void shouldQualifyStaticMethodAccess() throws Exception {
    ImportScope scope = new ImportScope(null, null,null, null);
    MethodCallExpr node = NodesFactory.methodCallExpr("myStaticMethod");
    handlerTester(NameResolverHandler.class, false).
    assertGenerateString("MyClassContainingStaticMethod.myStaticMethod()",
      node,
      context()
      .withMethod(node, new QualifiedName<MethodName>(scope, true, NameTypes.METHOD, new JavaTypeName(withClassName("MyClassContainingStaticMethod")))));
  }
  
  @Test
  public void shouldDelegateToDefaultBehaviorIfQNameIsNotFound() throws Exception {
    MethodCallExpr node = NodesFactory.methodCallExpr("myMethod", fieldAccess("MyClas", "myField"));
    handlerTester(NameResolverHandler.class, false).
    assertGenerateString("MyClas.myField.myMethod()",
      node,
      context());
  }
  
  
}
