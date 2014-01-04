package org.stjs.generator.writer;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.Tree;

public interface WriterContributor<T extends Tree, R> extends VisitorContributor<T, R, GenerationContext<R>, WriterVisitor<R>> {

}
