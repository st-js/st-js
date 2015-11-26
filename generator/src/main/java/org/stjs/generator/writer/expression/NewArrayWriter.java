package org.stjs.generator.writer.expression;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.tools.javac.tree.JCTree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import java.util.ArrayList;
import java.util.HashMap;
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
            return createArrayFromDimensions(tree, context);
        }
        throw context.addError(tree, "Java arrays without initializers are not supported. This is a ST-JS bug.");
    }

    private JS createArrayFromDimensions(NewArrayTree tree, GenerationContext<JS> context) {
        // If their is no initializers to our array, let's work through the dimensions to build an array with null objects
        List<? extends ExpressionTree> dimensions = tree.getDimensions();
        List<JS> values = new ArrayList<>();

        if (dimensions.size() == 1) {
            buildSimpleArray(context, (Integer) ((JCTree.JCLiteral) dimensions.get(0)).getValue(), values);

        } else if (dimensions.size() == 2) {
            buildTwoDimensionalArrays(context, dimensions, values);

        } else {
            throw context.addError(tree, "Java arrays with more than 2 dimensions are not supported. This is a ST-JS bug.");
        }
        return context.js().array(values);
    }

    private JS createArrayFromInitializers(WriterVisitor<JS> visitor, NewArrayTree tree, GenerationContext<JS> context) {
        List<JS> values = new ArrayList<>();
        for (ExpressionTree arg : tree.getInitializers()) {
            values.add(visitor.scan(arg, context));
        }
        return context.js().array(values);
    }

    // anIntArray = new int[2]; --> this.anIntArray = [null, null];
    private void buildSimpleArray(GenerationContext<JS> context, Integer elementsCount, List<JS> values) {
        for (int i = 0; i < elementsCount; i++) {
            values.add(context.js().keyword(Keyword.NULL));
        }
    }

    // anIntArray = new int[2][2]; --> this.anIntArray = [[null, null], [null, null]];
    private void buildTwoDimensionalArrays(GenerationContext<JS> context, List<? extends ExpressionTree> dimensions, List<JS> values) {
        HashMap<Integer, List<JS>> dimensionValues = new HashMap<>();
        int arraySize = (Integer) ((JCTree.JCLiteral) dimensions.get(dimensions.size() - 1)).getValue();
        for (int dimensionIndex = 0, dimensionsSize = dimensions.size(); dimensionIndex < dimensionsSize; dimensionIndex++) {
            dimensionValues.put(dimensionIndex, new ArrayList<JS>());

            for (int i = 0; i < arraySize; i++) {
                dimensionValues.get(dimensionIndex).add(context.js().keyword(Keyword.NULL));
            }
            values.add(context.js().array(dimensionValues.get(dimensionIndex)));
        }
    }
}
