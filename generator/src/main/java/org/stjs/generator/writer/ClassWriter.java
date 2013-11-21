package org.stjs.generator.writer;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.ClassTree;

public class ClassWriter implements VisitorContributor<ClassTree, List<AstNode>, GenerationContext> {

	private Name javascriptName(String name) {
		Name n = new Name();
		n.setIdentifier(name);
		return n;
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ClassTree tree, GenerationContext p) {
		VariableDeclaration vars = new VariableDeclaration();
		VariableInitializer var = new VariableInitializer();
		var.setTarget(javascriptName(tree.getSimpleName().toString()));
		vars.addVariable(var);
		return Collections.<AstNode>singletonList(vars);
	}
}
