package org.stjs.generator.writer.template;

import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.JavascriptWriterVisitor;

/**
 * This template is called to render the call of the given method.
 * 
 * @author acraciun
 * 
 */
public interface MethodCallTemplate {
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context);
}
