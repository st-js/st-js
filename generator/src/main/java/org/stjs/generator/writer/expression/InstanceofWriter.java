package org.stjs.generator.writer.expression;

import static org.stjs.generator.writer.JavaScriptNodes.functionCall;
import static org.stjs.generator.writer.JavaScriptNodes.name;
import static org.stjs.generator.writer.JavaScriptNodes.property;

import java.util.Collections;
import java.util.List;

import javax.lang.model.type.TypeMirror;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavascriptKeywords;

import com.sun.source.tree.InstanceOfTree;
import com.sun.source.util.TreePath;

public class InstanceofWriter implements VisitorContributor<InstanceOfTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, InstanceOfTree tree,
			GenerationContext context, List<AstNode> prev) {
		// build stjs.isInstanceOf(expr.constructor, type);
		// TODO do I need a check or parenthesis around !?

		TypeMirror type = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getType()));
		AstNode getConstructor = property(visitor.scan(tree.getExpression(), context).get(0), JavascriptKeywords.CONSTRUCTOR);
		return Collections.<AstNode>singletonList(functionCall(name("stjs"), "isInstanceOf", getConstructor, name(context.getNames()
				.getTypeName(context, type))));
	}
}
