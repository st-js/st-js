package org.stjs.generator.writer;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.Tree;

/**
 * add the javadoc comment for the processed nodes.
 * 
 * @author acraciun
 * 
 * @param <JS>
 */
public class CommentWriter<JS> implements WriterFilter<Tree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, Tree tree, GenerationContext<JS> context,
			VisitorContributor<Tree, JS, GenerationContext<JS>, WriterVisitor<JS>> chain) {
		JS jsNode = chain.visit(visitor, tree, context);
		if (jsNode == null) {
			return null;
		}

		Element element = InternalUtils.symbol(tree);
		if (element != null) {
			String comment = context.getElements().getDocComment(element);
			if (comment != null) {
				return context.js().comment(jsNode, comment);
			}
		}
		return jsNode;
	}

}
