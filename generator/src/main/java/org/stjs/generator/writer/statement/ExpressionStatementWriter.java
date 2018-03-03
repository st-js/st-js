package org.stjs.generator.writer.statement;

import static java.util.Arrays.asList;

import javax.lang.model.type.TypeKind;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.tools.javac.tree.JCTree.JCAssignOp;
import com.sun.tools.javac.tree.JCTree.JCExpression;

public class ExpressionStatementWriter<JS> implements WriterContributor<ExpressionStatementTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, ExpressionStatementTree tree, GenerationContext<JS> context) {
        JavaScriptBuilder<JS> js = context.js();
        ExpressionTree expressionTree = tree.getExpression();
	    if (Kind.PLUS_ASSIGNMENT.equals(expressionTree.getKind()) && expressionTree instanceof JCAssignOp) {
            JCAssignOp asgn = (JCAssignOp) expressionTree;
            JCExpression lhs = asgn.lhs;
            JCExpression rhs = asgn.rhs;
            if (TypeKind.CHAR == rhs.type.getKind()) {
                //handle: 
                //String s = "a";
                //s += 'b';
                JS left = visitor.scan(lhs, context);
                JS right = visitor.scan(rhs, context);
                right = js.functionCall(js.property(js.name("String"), "fromCharCode"), asList(right));
                JS assignment = js.assignment(AssignOperator.PLUS_ASSIGNMENT, left, right);
                return context.withPosition(tree, js.expressionStatement(assignment));
            }
        }
		JS expression = visitor.scan(expressionTree, context);
		if (expression == null) {
			return null;
		}
        JS expressionStatement = js.expressionStatement(expression);
        return context.withPosition(tree, expressionStatement);
	}
}
