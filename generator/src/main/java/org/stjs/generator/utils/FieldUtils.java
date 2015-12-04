package org.stjs.generator.utils;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;

import javax.lang.model.element.Modifier;

public final class FieldUtils {

    private FieldUtils() {

    }

    public static String getFieldName(VariableTree tree) {
        String fieldName = tree.getName().toString();
        if (!tree.getModifiers().getFlags().contains(Modifier.PUBLIC)) {
            fieldName = GeneratorConstants.NON_PUBLIC_METHODS_AND_FIELDS_PREFIX + fieldName;
        }

        return fieldName;
    }

    public static boolean isInitializerLiteral(ExpressionTree initializer) {
        // allowed x = 1
        if (initializer instanceof LiteralTree) {
            return true;
        }
        // allowed x = -1
        if (initializer instanceof UnaryTree && ((UnaryTree) initializer).getExpression() instanceof LiteralTree) {
            return true;
        }
        return false;
    }

    public static <JS> boolean isFieldDeclaration(GenerationContext<JS> context) {
        if (context.getCurrentPath().getParentPath().getLeaf() instanceof ClassTree) {
            return true;
        }
        return false;
    }
}
