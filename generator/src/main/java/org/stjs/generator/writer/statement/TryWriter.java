package org.stjs.generator.writer.statement;

import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.VariableTree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Try blocks -> as in java
 *
 * @author acraciun
 */
public class TryWriter<JS> implements WriterContributor<TryTree, JS> {

    private static final String EXCEPTION_VARIABLE_NAME = "$exception";

    @Override
    public JS visit(WriterVisitor<JS> visitor, TryTree tree, GenerationContext<JS> context) {
        JS tryBlock = visitor.scan(tree.getBlock(), context);

        List<JS> catchClauses = getCatchClauses(visitor, context, tree.getCatches());
        JS finallyBlock = null;
        if (tree.getFinallyBlock() != null) {
            finallyBlock = visitor.scan(tree.getFinallyBlock(), context);
        }
        return context.withPosition(tree, context.js().tryStatement(tryBlock, catchClauses, finallyBlock));
    }

    private List<JS> getCatchClauses(WriterVisitor<JS> visitor, GenerationContext<JS> context, List<? extends CatchTree> catchTrees) {
        List<JS> catchClauses = new ArrayList<JS>();

        JavaScriptBuilder<JS> js = context.js();

        if (catchTrees.size() == 0) {
            return catchClauses;
        } else if (catchTrees.size() == 1) {
            catchClauses.add(visitor.scan(catchTrees.get(0), context));
        } else {
            // create a single catch clause and use "if" inside the single catch clause under the form:
            //   try {
            //       ...
            //   } catch ($exception) {
            //       if (stjs.isInstanceOf($exception, MyExceptionClassA)) {
            //           ... do whatever you want
            //       } else if (stjs.isInstanceOf($exception, MyExceptionClassB)) {
            //           ... do whatever you want
            //       } else {
            //           throw $exception; // not one of the expected exception type
            //       }

            // Loop in reverse order because we need to add latter exception type checks inside the "else"
            // part of the "if" statement.

            // start with "unhandled" exception case: rethrow the exceptions
            JS $exception = context.js().name(EXCEPTION_VARIABLE_NAME);
            JS lastCatchCauseConvertedToIf = js.block(Collections.singletonList(js.throwStatement($exception)));
            for (int i = catchTrees.size() - 1; i >= 0; i--) {
                CatchTree catchTree = catchTrees.get(i);

                lastCatchCauseConvertedToIf =
                        js.ifStatement(
                                generateCheckInstanceOfExceptionClass(context, catchTree), // condition
                                generateHandleExceptionBlock(visitor, context, catchTree), // then block
                                lastCatchCauseConvertedToIf); // else --> use last generated exception type check
            }

            JS catchClauseConvertedToIfChecks = context.js().catchClause($exception, lastCatchCauseConvertedToIf);
            catchClauses.add(catchClauseConvertedToIfChecks);
        }

        return catchClauses;
    }

    private JS generateCheckInstanceOfExceptionClass(GenerationContext<JS> context, CatchTree catchTree) {
        // Generate the following:
        //     stjs.isInstanceOf($exception, MyExceptionClassA)
        return context.js().functionCall(
                context.js().name("stjs.isInstanceOf"),
                Arrays.<JS>asList(
                        context.js().name(EXCEPTION_VARIABLE_NAME),
                        getExceptionClassTypeName(context, catchTree)));
    }

    private JS getExceptionClassTypeName(GenerationContext<JS> context, CatchTree catchTree) {
        VariableTree parameter = catchTree.getParameter();
        Element element = TreeUtils.elementFromUse((ExpressionTree) parameter.getType());
        String typeName = context.getNames().getTypeName(context, element, DependencyType.OTHER);
        return context.js().name(typeName);
    }

    private JS generateHandleExceptionBlock(WriterVisitor<JS> visitor, GenerationContext<JS> context, CatchTree catchTree) {
        JS existingCatchCauseBlock = visitor.scan(catchTree.getBlock(), context);

        // Output example:
        //    if (instanceof($exception, ExpectedExceptionType) {
        //        var e = $exception;  // <-- Add exception variable declaration at the beginning of the catch block code
        //        ...
        //    }
        context.js().addStatementBeginning(
                existingCatchCauseBlock,
                context.js().variableDeclaration(true, catchTree.getParameter().getName(), context.js().name(EXCEPTION_VARIABLE_NAME)));

        return existingCatchCauseBlock;
    }

}
