package org.stjs.generator.writer;

import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;

/**
 * This class visits the Java AST and calls the corresponding writers to generate JavaScript.
 * 
 * @author acraciun
 * 
 */
public class WriterVisitor extends TreePathScannerContributors<List<AstNode>, GenerationContext> {

}
