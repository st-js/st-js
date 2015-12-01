package org.stjs.generator.check.declaration;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.writer.MemberWriters;

import javax.lang.model.element.Modifier;

/**
 * this class checks that you don't have synchornized methods, as this feature is not supported in JavaScript
 *
 * @author acraciun
 */
public class MethodSynchronizedCheck implements CheckContributor<MethodTree> {

    @Override
    public Void visit(CheckVisitor visitor, MethodTree tree, GenerationContext<Void> context) {
        TreeWrapper<Tree, Void> tw = context.getCurrentWrapper();
        if (MemberWriters.shouldSkip(tw)) {
            return null;
        }

        if ((!context.getConfiguration().isSynchronizedAllowed()) && tree.getModifiers().getFlags().contains(Modifier.SYNCHRONIZED)) {
            context.addError(tree, "Synchronized methods are not supported by Javascript");
        }

        return null;
    }
}
