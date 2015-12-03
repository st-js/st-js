package org.stjs.generator.writer.expression;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.NewArrayTree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple support to write the creation (new) of native java array.
 */
public class NewArrayWriter<JS> implements WriterContributor<NewArrayTree, JS> {

    @Override
    @SuppressWarnings("PMD.ConfusingTernary")
    public JS visit(WriterVisitor<JS> visitor, NewArrayTree tree, GenerationContext<JS> context) {
        if (tree.getInitializers() != null) {
            return createArrayFromInitializers(visitor, tree, context);
        } else if (tree.getDimensions() != null) {
            return createArrayFromDimensions(visitor, tree, context);
        }
        throw context.addError(tree, "Java arrays without initializers are not supported. This is a ST-JS bug.");
    }

    private JS createArrayFromDimensions(WriterVisitor<JS> visitor, NewArrayTree tree, GenerationContext<JS> context) {
        // If there is no initializers to our array, let's work through the dimensions to build an array with null objects
        List<? extends ExpressionTree> dimensions = tree.getDimensions();
        if (dimensions.size() > 2) {
            throw context.addError(tree, "Java arrays with more than 2 dimensions are not supported. This is a ST-JS bug.");
        }

        List<JS> dimensionExpressionList = new ArrayList<>();
        for (ExpressionTree dimensionExpressionTree : dimensions) {
            dimensionExpressionList.add(visitor.scan(dimensionExpressionTree, context));
        }

        return context.js().functionCall(
                context.js().name("stjs.createJavaArray"),
                dimensionExpressionList
        );
    }

    private JS createArrayFromInitializers(WriterVisitor<JS> visitor, NewArrayTree tree, GenerationContext<JS> context) {
        List<JS> values = new ArrayList<>();
        for (ExpressionTree arg : tree.getInitializers()) {
            values.add(visitor.scan(arg, context));
        }
        return context.js().array(values);
    }

}
