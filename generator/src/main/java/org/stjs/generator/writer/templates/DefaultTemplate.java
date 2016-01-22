package org.stjs.generator.writer.templates;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.tools.javac.code.Symbol;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.utils.Scopes;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * this is the standard generation template
 *
 * @author acraciun
 */
public class DefaultTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	private boolean isCallToSuperConstructor(MethodInvocationTree tree) {
		if (!TreeUtils.isSuperCall(tree)) {
			return false;
		}

		Element methodElement = TreeUtils.elementFromUse(tree);
		if (JavaNodes.isStatic(methodElement)) {
			// this is a call of type super.staticMethod(args) -> it should be handled as a simple call to staticMethod
			return false;
		}

		return true;
	}

	/**
	 * super(args) -> SuperType.call(this, args)
	 */
	private JS callToSuperConstructor(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		Element methodElement = TreeUtils.elementFromUse(tree);
		TypeElement typeElement = (TypeElement) methodElement.getEnclosingElement();

		String methodName = context.getNames().getMethodName(context, tree);

		// avoid useless call to super() when the super class is Object
		if (GeneratorConstants.SUPER.equals(methodName) && JavaNodes.sameRawType(typeElement.asType(), Object.class)) {
			return null;
		}

		// avoid call to super for synthetic types
		if (GeneratorConstants.SUPER.equals(methodName) && context.getCurrentWrapper().getEnclosingType().isSyntheticType()) {
			return null;
		}

		// transform it into superType.[prototype.method].call(this, args..);
		JS superType = constructSuperType(context, typeElement, methodName);

		List<JS> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);

		addOuterClassReferenceParameters(tree, context, methodElement, arguments);

		arguments.add(0, context.js().keyword(Keyword.THIS));
		return context.js().functionCall(context.js().property(superType, "call"), arguments);
	}

	private void addOuterClassReferenceParameters(MethodInvocationTree tree, GenerationContext<JS> context, Element methodElement, List<JS> arguments) {
		Element enclosingClassForMethod = methodElement.getEnclosingElement();
		if (ElementUtils.isInnerClass(enclosingClassForMethod) && !JavaNodes.isStatic(enclosingClassForMethod)) {
			TypeElement callingClass = TreeUtils.getEnclosingClass(context.getCurrentPath());

			int deepnessLevel = ElementUtils.getInnerClassDeepnessLevel(callingClass);

			String scopeAccessorPrefix = Scopes.buildOuterClassAccessTargetPrefix();
			String scopeAccessorVariable = scopeAccessorPrefix + GeneratorConstants.AUTO_GENERATED_ELEMENT_SEPARATOR + deepnessLevel;

			arguments.add(0, context.js().name(scopeAccessorVariable));
		}
	}

	private JS constructSuperType(GenerationContext<JS> context, TypeElement typeElement, String methodName) {
		String typeName = context.getNames().getTypeName(context, typeElement, DependencyType.STATIC);
//		if (typeName.equals(GeneratorConstants.ENUM_CLASS)) {
//			typeName = GeneratorConstants.TRANSPILED_ENUM_CLASS;
//		}
		return context.js().name(GeneratorConstants.SUPER.equals(methodName) ? typeName : typeName + ".prototype." + methodName);
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		if (isCallToSuperConstructor(tree)) {
			return callToSuperConstructor(visitor, tree, context);
		}

		JS target = MethodInvocationWriter.buildTarget(visitor, context.<MethodInvocationTree> getCurrentWrapper());

		List<JS> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);

		JS constructorName = buildConstructorInvocationForMultipleConstructors(tree, context, target, arguments);
		if (constructorName != null) {
			return constructorName;
		}

		String name = context.getNames().getMethodName(context, tree);
		return context.js().functionCall(context.js().property(target, name), arguments);
	}

	private JS buildConstructorInvocationForMultipleConstructors(MethodInvocationTree tree, GenerationContext<JS> context,
																 JS target, List<JS> arguments) {
		ExecutableElement element = TreeUtils.elementFromUse(tree);
		if (JavaNodes.hasMultipleConstructors(context.getCurrentPath()) && ElementKind.CONSTRUCTOR.equals(element.getKind())) {
			// Invocation of a another constructor, let's get the overloaded constructor name and chain the real static constructor
			String constructorName = InternalUtils.generateOverloadeConstructorName(context, ((Symbol.MethodSymbol) element).getParameters());
			return context.js().functionCall(context.js().property(target, constructorName), arguments);
		}
		return null;
	}

}
