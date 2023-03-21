package org.stjs.generator.check.expression;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.javascript.annotation.ServerSide;

import com.sun.source.tree.MemberSelectTree;

/**
 * @see {@link IdentifierGlobalScopeNameClashCheck}
 * @author acraciun
 */
public class MemberSelectServerSideCheck implements CheckContributor<MemberSelectTree> {

	@Override
	public Void visit(CheckVisitor visitor, MemberSelectTree tree, GenerationContext<Void> context) {
		Element element = TreeUtils.elementFromUse(tree);

		if (element != null && element.getAnnotation(ServerSide.class) != null) {
			context.addError(tree, "You cannot access fields annotated with @ServerSide in a client code");
		}

		return null;
	}

}
