package org.stjs.generator.handlers;

import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import org.stjs.generator.GenerationContext;

public class LiteralExpressionHandler extends DefaultHandler {
    
  public LiteralExpressionHandler(RuleBasedVisitor ruleVisitor) {
      super(ruleVisitor);
  }
  
  @Override
  public void visit(IntegerLiteralExpr n, GenerationContext context) {
    print(n);
  }
  
  @Override
  public void visit(LongLiteralExpr n, GenerationContext context) {
    print(n);
  }
  
  public void print(StringLiteralExpr n) {
    // java has some more syntax to declare integers :
    // 0x0, 0b0, (java7) 1_000_000
    // TODO : convert it to plain numbers for javascript
    getPrinter().print(n.getValue());
  }
}
