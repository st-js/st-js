package org.stjs.generator.writer;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;

public class CompilationUnitWriter<JS> implements WriterContributor<CompilationUnitTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, CompilationUnitTree tree, GenerationContext<JS> context) {
		List<JS> children = new ArrayList<JS>();
		for (Tree type : tree.getTypeDecls()) {
			children.add(visitor.scan(type, context));
		}
		return context.js().root(children);
	}

}
