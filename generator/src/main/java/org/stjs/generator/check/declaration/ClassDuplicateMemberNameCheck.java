package org.stjs.generator.check.declaration;

import java.util.Collection;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.MemberWriters;

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
public class ClassDuplicateMemberNameCheck implements CheckContributor<ClassTree> {

	private void checkCandidate(Element overrideCandidate, ExecutableElement methodElement, TypeElement classElement, Tree member,
			GenerationContext<Void> context) {

		boolean isMethod = overrideCandidate instanceof ExecutableElement;
		boolean isFieldOrInnerType = !isMethod;

		String name = methodElement.getSimpleName().toString();
		if (isFieldOrInnerType) {
			// it's a field or inner type -> this is illegal
			context.addError(member, "There is already a field with the same name as this method in the type or one of its parents: "
					+ ((TypeElement) overrideCandidate.getEnclosingElement()).getQualifiedName() + "." + overrideCandidate.getSimpleName());
		} else if (!context.getElements().overrides(methodElement, (ExecutableElement) overrideCandidate, classElement)) {
			context.addError(member, "Only maximum one method with the name [" + name
					+ "] is allowed to have a body. The other methods must be marked as native."
					+ " The type (or one of its parents) may contain already the method: " + overrideCandidate
					+ " that has a different signature");
		}

	}

	private void checkMethodName(TypeElement classElement, MethodTree method, ExecutableElement methodElement, GenerationContext<Void> context,
			Multimap<String, Element> existingNames) {
		String name = method.getName().toString();

		Collection<Element> sameName = existingNames.get(name);
		if (sameName.isEmpty()) {
			existingNames.put(name, methodElement);
		} else {
			// try to see if it's not in fact a method override
			for (Element overrideCandidate : sameName) {
				checkCandidate(overrideCandidate, methodElement, classElement, method, context);
			}
		}
	}

	private void checkMethod(TypeElement classElement, Tree member, GenerationContext<Void> context, Multimap<String, Element> existingNames) {
		if (member instanceof MethodTree) {
			MethodTree method = (MethodTree) member;
			ExecutableElement methodElement = TreeUtils.elementFromDeclaration(method);
			if (JavaNodes.isNative(methodElement)) {
				// do nothing with the native methods as no code will be generated.
				// the check will be done only for the method that has a body and that is supposed to me the most
				// generic version of the overloaded method
				return;
			}
			TreeWrapper<Tree, Void> tw = context.getCurrentWrapper().child(member);
			if (MemberWriters.shouldSkip(tw)) {
				return;
			}
			if (methodElement.getKind() != ElementKind.METHOD) {
				// skip the constructors
				return;
			}
			checkMethodName(classElement, method, methodElement, context, existingNames);
		}
	}

	private boolean hasOnlyFields(Collection<Element> sameName) {
		for (Element el : sameName) {
			if (!(el instanceof VariableElement)) {
				return false;
			}
		}
		return true;
	}

	private void checkField(Tree member, GenerationContext<Void> context, Multimap<String, Element> existingNames) {
		if (member instanceof VariableTree) {
			TreeWrapper<Tree, Void> tw = context.getCurrentWrapper().child(member);
			if (MemberWriters.shouldSkip(tw)) {
				return;
			}

			String name = ((VariableTree) member).getName().toString();
			Collection<Element> sameName = existingNames.get(name);
			if (sameName.isEmpty()) {
				Element variableElement = TreeUtils.elementFromDeclaration((VariableTree) member);
				existingNames.put(name, variableElement);
			} else {
				if (!hasOnlyFields(sameName)) {
					// accept fields with the same name, but not methods and fields
					context.addError(member, "The type (or one of its parents) contains already a method called [" + name
							+ "]. Javascript cannot distinguish methods/fields with the same name");
				}
			}
		}
	}

	@Override
	public Void visit(CheckVisitor visitor, ClassTree tree, GenerationContext<Void> context) {
		Multimap<String, Element> names = LinkedListMultimap.create();

		// TypeElement classElement = TreeUtils.elementFromDeclaration(tree);
		TypeElement classElement = (TypeElement) context.getTrees().getElement(context.getCurrentPath());
		TypeMirror superType = classElement.getSuperclass();
		if (superType.getKind() != TypeKind.NONE) {
			// add the names from the super class
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
