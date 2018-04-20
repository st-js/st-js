package org.stjs.generator.check.expression;

import static org.stjs.generator.writer.templates.DefaultTemplate.isConvertedEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * (c) Swissquote 05.04.18
 *
 * @author sgoetz
 */
public class MethodInvocationJavaObject implements CheckContributor<MethodInvocationTree> {

	private static final List<String> OBJECT_METHODS;

	static {
		OBJECT_METHODS = new ArrayList<>(Arrays.asList(
			"hashCode",
			"equals",
			"clone",
			"toString"
		));
	}

	public boolean implementsMethod(ClassTree tree, GenerationContext<Void> context, String method) {
		for (Tree member : tree.getMembers()) {

			if (member instanceof MethodTree) {
				String methodName = context.getNames().getMethodName(context, (MethodTree) member, context.getCurrentPath());

				if (method.equals(methodName)) {
					return true;
				}
			}
		}

		return false;
	}

	public Symbol.MethodSymbol getMethod(TypeElement methodOwnerElement, String method) {
		if (!(methodOwnerElement instanceof Symbol.ClassSymbol)) {
			return null;
		}

		Scope members = ((Symbol.ClassSymbol) methodOwnerElement).members();
		for (Symbol member : members.getElements()) {
			if (!(member instanceof Symbol.MethodSymbol)) {
				continue;
			}

			String methodName = ((Symbol.MethodSymbol) member).name.toString();
			if (!method.equals(methodName)) {
				continue;
			}

			String owner = ((Symbol.ClassSymbol) ((Symbol.MethodSymbol) member).owner).type.toString();
			if (!owner.startsWith("java.lang.")) {
				return ((Symbol.MethodSymbol) member);
			}

		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visit(CheckVisitor visitor, MethodInvocationTree tree, GenerationContext<Void> context) {
		String name = MethodInvocationWriter.buildMethodName(tree);

		if (GeneratorConstants.SUPER.equals(name)) {
			return null;
		}

		// getClass is automatically forbidden
		if ("getClass".equals(name)) {
			context.addError(tree, "You can't use the 'getClass' method.");
			return null;
		}

		if (!OBJECT_METHODS.contains(name)) {
			return null;
		}

		Element methodElement = InternalUtils.symbol(tree);
		TypeElement methodOwnerElement = (TypeElement) methodElement.getEnclosingElement();

		if ("equals".equals(name) && isConvertedEquals(name)) {
			return null;
		}

		Symbol.MethodSymbol implementedMethod = getMethod(methodOwnerElement, name);

		if (implementedMethod == null) {
			context.addError(tree, "Methods inherited from Object can't be used unless they're implemented. Called '" + name + "()'");
		}

		return null;
	}
}
