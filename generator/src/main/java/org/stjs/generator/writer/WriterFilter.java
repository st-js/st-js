package org.stjs.generator.writer;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.VisitorFilterContributor;

import com.sun.source.tree.Tree;

public interface WriterFilter<T extends Tree, R> extends VisitorFilterContributor<T, R, GenerationContext<R>, WriterVisitor<R>> {

}
