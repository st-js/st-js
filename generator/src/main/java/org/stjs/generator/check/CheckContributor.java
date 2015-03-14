package org.stjs.generator.check;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.Tree;

/**
 * this interface is to be implemented by all the check contributors
 * 
 * @author acraciun
 */
public interface CheckContributor<T extends Tree> extends VisitorContributor<T, Void, GenerationContext<Void>, CheckVisitor> {

}
