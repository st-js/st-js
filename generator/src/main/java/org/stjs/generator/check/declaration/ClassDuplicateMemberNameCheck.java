package org.stjs.generator.check.declaration;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;

import java.lang.reflect.Modifier;
import java.util.Collection;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.ast.ASTNodeData;
import org.stjs.generator.ast.SourcePosition;
import org.stjs.generator.type.ClassWrapper;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.utils.Option;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

/**
 * checks the a field name or method exists only once in the class and its hierchy
 * @author acraciun
 */
public class ClassDuplicateMemberNameCheck implements VisitorContributor<ClassTree, Void, GenerationContext> {
	private static void checkExistentFieldOrMethod(String name, BodyDeclaration member, ClassWrapper superClass, GenerationContext context) {
		if (superClass.findField(name).isDefined() || !superClass.findMethods(name).isEmpty()) {
			throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(member),
					"One of the parent types contains already a method or a field called [" + name
							+ "] with a different signature. Javascript cannot distinguish methods/fields with the same name");
		}
	}

	private static void postCheckMethod(BodyDeclaration member, ClassWrapper superClass, GenerationContext context) {
		if (!(member instanceof MethodDeclaration)) {
			return;
		}
		MethodDeclaration methodDeclaration = (MethodDeclaration) member;
		MethodWrapper resolvedMethod = ASTNodeData.resolvedMethod(methodDeclaration);
		if (Modifier.isStatic(resolvedMethod.getModifiers())) {
			return;
		}
		String name = methodDeclaration.getName();
		Option<MethodWrapper> overrideMethod = superClass.findMethod(name, resolvedMethod.getParameterTypes());
		if (overrideMethod.isDefined()) {
			if (Modifier.isPrivate(overrideMethod.getOrThrow().getModifiers())) {
				throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(member),
						"One of the parent types contains already a private method called [" + name
								+ "]. Javascript cannot distinguish methods/fields with the same name");
			}
			return;
		}

		checkExistentFieldOrMethod(name, member, superClass, context);
	}

	private void checkMethod(TypeElement classElement, Tree member, GenerationContext context, Multimap<String, Element> existingNames) {
		if (member instanceof MethodTree) {
			MethodTree method = (MethodTree) member;
			ExecutableElement methodElement = TreeUtils.elementFromDeclaration(method);
			if (JavaNodes.isNative(methodElement)) {
				// do nothing with the native methods as no code will be generated.
				// the check will be done only for the method that has a body and that is supposed to me the most
				// generic version of the overloaded method
				return;
			}
			if (methodElement.getKind() != ElementKind.METHOD) {
				//skip the constructors
				return;
			}
			String name = method.getName().toString();

			Collection<Element> sameName = existingNames.get(name);
			if (sameName.isEmpty()) {
				existingNames.put(name, methodElement);
			} else {
				//try to see if it's  not in fact a method override
				for (Element overrideCandidate : sameName) {
					boolean error = false;
					if (!(overrideCandidate instanceof ExecutableElement)) {
						//it's a field or inner type -> this is illegal
						error = true;
					} else if (!context.getElements().overrides(methodElement, (ExecutableElement) overrideCandidate, classElement)) {
						error = true;
					}
					if (error) {
						context.addError(
								member,
								"Only maximum one method with the name ["
										+ name
										+ "] is allowed to have a body. The other methods must be marked as native. The type (or one of its parents) may contain already a method or a field called ["
										+ name + "] with a different signature. ");
						break;
					}
				}
			}
		}
	}

	private void checkField(Tree member, GenerationContext context, Multimap<String, Element> existingNames) {
		if (member instanceof VariableTree) {
			String name = ((VariableTree) member).getName().toString();
			Element variableElement = TreeUtils.elementFromDeclaration((VariableTree) member);
			if (existingNames.containsKey(name)) {
				context.addError(member, "The type (or one of its parents) contains already a method or a field called [" + name
						+ "] with a different signature. Javascript cannot distinguish methods/fields with the same name");
			} else {
				existingNames.put(name, variableElement);
			}
		}
	}

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, ClassTree tree, GenerationContext context, Void prev) {
		Multimap<String, Element> names = LinkedListMultimap.create();

		TypeElement classElement = TreeUtils.elementFromDeclaration(tree);
		TypeMirror superType = classElement.getSuperclass();
		if (superType.getKind() != TypeKind.NONE) {
			//add the names from the super class
			TypeElement superclassElement = (TypeElement) ((DeclaredType) superType).asElement();
			for (Element memberElement : context.getElements().getAllMembers(superclassElement)) {
				if (!JavaNodes.isNative(memberElement)) {
					names.put(memberElement.getSimpleName().toString(), memberElement);
				}
			}
		}
		// check first the methods
		for (Tree member : tree.getMembers()) {
			checkMethod(classElement, member, context, names);
		}
		// check the fields
		for (Tree member : tree.getMembers()) {
			checkField(member, context, names);
		}
		return null;
	}
}
