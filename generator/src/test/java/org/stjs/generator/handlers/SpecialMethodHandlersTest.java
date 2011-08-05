package org.stjs.generator.handlers;

import static org.junit.Assert.assertEquals;
import static org.stjs.generator.NodesFactory.methodCallExpr;
import static org.stjs.generator.scope.path.QualifiedPath.withClass;
import japa.parser.ast.expr.NameExpr;
import org.junit.Test;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.handlers.SpecialMethodHandlers.$InvokeHandler;
import org.stjs.generator.scope.JavaTypeName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.QualifiedName;
import org.stjs.generator.scope.QualifiedName.NameTypes;
import org.stjs.generator.scope.VariableScope;


public class SpecialMethodHandlersTest {

  
  @Test
  public void invokeHandlerTest() throws Exception {
    // myFunction.$invoke(x,y)
    $InvokeHandler handler = new SpecialMethodHandlers.$InvokeHandler();
    RuleBasedVisitor visitor = new RuleBasedVisitor();
    QualifiedName<MethodName> qualifiedName = new QualifiedName<MethodName>(new VariableScope(null, null, null), false, NameTypes.METHOD, new JavaTypeName(withClass(null)));
    handler.handle(new DefaultHandler(visitor) {
      @Override
      public void visit(NameExpr n, GenerationContext context) {
        printer.print(n.getName());
      }
    }, methodCallExpr("$invoke", "x", "y"), qualifiedName, new GenerationContext(null));
    assertEquals("(x, y)",visitor.getPrinter().toString());
  }
}
