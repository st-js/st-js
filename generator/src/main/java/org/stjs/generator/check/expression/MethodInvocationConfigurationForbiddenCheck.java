package org.stjs.generator.check.expression;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.code.Symbol;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import javax.lang.model.element.Element;
import java.util.Collection;

/**
 * this check verifies that you don't call a method that is listed as forbidden in the configuration.
 */
public class MethodInvocationConfigurationForbiddenCheck implements CheckContributor<MethodInvocationTree> {

	@Override
	@SuppressWarnings("PMD.CyclomaticComplexity")
	public Void visit(CheckVisitor visitor, MethodInvocationTree tree, GenerationContext<Void> context) {
		Element methodElement = TreeUtils.elementFromUse(tree);
		if (!(methodElement instanceof Symbol.MethodSymbol)) {
			return null;
		}

		Symbol methodOwner = ((Symbol.MethodSymbol) methodElement).owner;
		if (!(methodOwner instanceof Symbol.ClassSymbol)) {
			return null;
		}

		String transpiledMethodName = MethodInvocationWriter.buildMethodName(tree, context);

		String methodFullPath = ((Symbol.ClassSymbol) methodOwner).className() + "." + transpiledMethodName;

		Collection<String> forbiddenMethodInvocations = context.getConfiguration().getForbiddenMethodInvocations();
		if (forbiddenMethodInvocations.contains(methodFullPath)) {
			context.addError(tree, "You cannot access methods that are listed as forbidden. See `forbiddenMethodInvocations` "
					+ "setting list in your Generator Configuration." +
					"Forbidden method: " + methodFullPath);
			return null;
		}

		return null;
	}
}
