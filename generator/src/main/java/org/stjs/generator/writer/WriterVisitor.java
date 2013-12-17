package org.stjs.generator.writer;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;

/**
 * This class visits the Java AST and calls the corresponding writers to generate JavaScript.
 * @author acraciun
 */
public class WriterVisitor<R> extends TreePathScannerContributors<R, GenerationContext<R>, WriterVisitor<R>> {

}
