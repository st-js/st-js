package org.stjs.generator.javac;

/*>>>
 import checkers.nullness.quals.Nullable;
 */

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.Name;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Utility class for analyzing {@code Element}s.
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(justification = "copied code", value = "BC_UNCONFIRMED_CAST")
@SuppressWarnings("PMD")
public final class ElementUtils {

    // Class cannot be instantiated.
    private ElementUtils() {
        throw new AssertionError("Class ElementUtils cannot be instantiated.");
    }

    /**
     * Returns the innermost type element enclosing the given element
     *
     * @param elem the enclosed element of a class
     * @return the innermost type element
     */
    public static TypeElement enclosingClass(final Element elem) {
        Element result = elem;
        while (result != null && !result.getKind().isClass() && !result.getKind().isInterface()) {
            /* @Nullable */
            Element encl = result.getEnclosingElement();
            result = encl;
        }
        return (TypeElement) result;
    }

    /**
     * Returns the innermost package element enclosing the given element. The same effect as
     * {@link javax.lang.model.util.Elements#getPackageOf(Element)}. Returns the element itself if it is a package.
     *
     * @param elem the enclosed element of a package
     * @return the innermost package element
     */
    public static PackageElement enclosingPackage(final Element elem) {
        Element result = elem;
        while (result != null && result.getKind() != ElementKind.PACKAGE) {
            /* @Nullable */
            Element encl = result.getEnclosingElement();
            result = encl;
        }
        return (PackageElement) result;
    }

    /**
     * Returns the "parent" package element for the given package element. For package "A.B" it gives "A". For package "A" it gives the default
     * package. For the default package it returns null; Note that packages are not enclosed within each other, we have to manually climb the
     * namespaces. Calling "enclosingPackage" on a package element returns the package element itself again.
     *
     * @param elem the package to start from
     * @return the parent package element
     */
    public static PackageElement parentPackage(final Elements e, final PackageElement elem) {
        String fqnstart = elem.getQualifiedName().toString();
        String fqn = fqnstart;
        if (!fqn.isEmpty() && fqn.contains(".")) {
            fqn = fqn.substring(0, fqn.lastIndexOf('.'));
            return e.getPackageElement(fqn);
        }
        return null;
    }

    /**
     * Returns true if the element is a static element: whether it is a static field, static method, or static class
     *
     * @param element
     * @return true if element is static
     */
    public static boolean isStatic(Element element) {
        return element.getModifiers().contains(Modifier.STATIC);
    }

    /**
     * Returns true if the element is a final element: a final field, final method, or final class
     *
     * @param element
     * @return true if the element is final
     */
    public static boolean isFinal(Element element) {
        return element.getModifiers().contains(Modifier.FINAL);
    }

    /**
     * Returns true if the element is a effectively final element.
     * @param element
     * @return true if the element is effectively final
     */
    // public static boolean isEffectivelyFinal(Element element) {
    // Symbol sym = (Symbol) element;
    // if (sym.getEnclosingElement().getKind() == ElementKind.METHOD &&
    // (sym.getEnclosingElement().flags() & ABSTRACT) != 0) {
    // return true;
    // }
    // return (sym.flags() & (FINAL | EFFECTIVELY_FINAL)) != 0;
    // }

    /**
     * Returns the {@code TypeMirror} for usage of Element as a value. It returns the return type of a method element, the class type of a
     * constructor, or simply the type mirror of the element itself.
     *
     * @param element
     * @return the type for the element used as a value
     */
    public static TypeMirror getType(Element element) {
        if (element.getKind() == ElementKind.METHOD) {
            return ((ExecutableElement) element).getReturnType();
        } else if (element.getKind() == ElementKind.CONSTRUCTOR) {
            return enclosingClass(element).asType();
        } else {
            return element.asType();
        }
    }

    public static TypeElement asTypeElement(GenerationContext<?> context, TypeMirror typeMirror) {
        if (typeMirror.getKind() == TypeKind.DECLARED) {
            Name qualifiedName = TypesUtils.getQualifiedName((DeclaredType) typeMirror);
            TypeElement typeElement = context.getElements().getTypeElement(qualifiedName.toString());
            return typeElement;
        } else {
            return null;
        }
    }

    /**
     * Returns the qualified name of the inner most class enclosing the provided {@code Element}
     *
     * @param element an element enclosed by a class, or a {@code TypeElement}
     * @return The qualified {@code Name} of the innermost class enclosing the element
     */
    public static/* @Nullable */Name getQualifiedClassName(Element element) {
        if (element.getKind() == ElementKind.PACKAGE) {
            PackageElement elem = (PackageElement) element;
            return elem.getQualifiedName();
        }

        TypeElement elem = enclosingClass(element);
        if (elem == null) {
            return null;
        }

        return elem.getQualifiedName();
    }

    /**
     * Returns a verbose name that identifies the element.
     */
    public static String getVerboseName(Element elt) {
        if (elt.getKind() == ElementKind.PACKAGE || elt.getKind().isClass()) {
            return getQualifiedClassName(elt).toString();
        } else {
            return getQualifiedClassName(elt) + "." + elt.toString();
        }
    }

    /**
     * Check if the element is an element for 'java.lang.Object'
     *
     * @param element the type element
     * @return true iff the element is java.lang.Object element
     */
    public static boolean isObject(TypeElement element) {
        return element.getQualifiedName().contentEquals("java.lang.Object");
    }

    /**
     * Returns true if the element is a constant time reference
     */
    public static boolean isCompileTimeConstant(Element elt) {
        return elt != null && elt.getKind() == ElementKind.FIELD && ((VariableElement) elt).getConstantValue() != null;
    }

    /**
     * Returns true if the element is declared in ByteCode. Always return false if elt is a package.
     */
    public static boolean isElementFromByteCode(Element elt) {
        if (elt == null) {
            return false;
        }

        if (elt instanceof Symbol.ClassSymbol) {
            Symbol.ClassSymbol clss = (Symbol.ClassSymbol) elt;
            if (null != clss.classfile) {
                // The class file could be a .java file
                return clss.classfile.getName().endsWith(".class");
            } else {
                return false;
            }
        }
        return isElementFromByteCode(elt.getEnclosingElement(), elt);
    }

    /**
     * Returns true if the element is declared in ByteCode. Always return false if elt is a package.
     */
    private static boolean isElementFromByteCode(Element elt, Element orig) {
        if (elt == null) {
            return false;
        }
        if (elt instanceof Symbol.ClassSymbol) {
            Symbol.ClassSymbol clss = (Symbol.ClassSymbol) elt;
            if (null != clss.classfile) {
                // The class file could be a .java file
                return clss.classfile.getName().endsWith(".class") || clss.classfile.getName().endsWith(".class)")
                        || clss.classfile.getName().endsWith(".class)]");
            } else {
                return false;
            }
        }
        return isElementFromByteCode(elt.getEnclosingElement(), elt);
    }

    /**
     * Returns the field of the class
     */
    public static VariableElement findFieldInType(TypeElement type, String name) {
        for (VariableElement field : ElementFilter.fieldsIn(type.getEnclosedElements())) {
            if (field.getSimpleName().toString().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public static Set<VariableElement> findFieldsInType(TypeElement type, Collection<String> names) {
        Set<VariableElement> results = new HashSet<>();
        for (VariableElement field : ElementFilter.fieldsIn(type.getEnclosedElements())) {
            if (names.contains(field.getSimpleName().toString())) {
                results.add(field);
            }
        }
        return results;
    }

    public static boolean isError(Element element) {
        return "com.sun.tools.javac.comp.Resolve$SymbolNotFoundError".equals(element.getClass().getName());
    }

    /**
     * Does the given element need a receiver for accesses? For example, an access to a local variable does not require a receiver.
     *
     * @param element The element to test.
     * @return whether the element requires a receiver for accesses.
     */
    public static boolean hasReceiver(Element element) {
        return element.getKind() != ElementKind.LOCAL_VARIABLE && element.getKind() != ElementKind.PARAMETER
                && element.getKind() != ElementKind.PACKAGE && !ElementUtils.isStatic(element);
    }

    /**
     * Determine all type elements for the classes and interfaces referenced in the extends/implements clauses of the given type element. TODO:
     * can we learn from the implementation of com.sun.tools.javac.model.JavacElements.getAllMembers(TypeElement)?
     */
    public static List<TypeElement> getSuperTypes(TypeElement type) {
        return getSuperTypes(type, true);
    }

    public static List<TypeElement> getSuperTypes(TypeElement type, boolean addInterfaces) {

        List<TypeElement> superelems = new ArrayList<>();
        if (type == null) {
            return superelems;
        }

        // Set up a stack containing type, which is our starting point.
        Deque<TypeElement> stack = new ArrayDeque<>();
        stack.push(type);

        while (!stack.isEmpty()) {
            TypeElement current = stack.pop();

            // For each direct supertype of the current type element, if it
            // hasn't already been visited, push it onto the stack and
            // add it to our superelems set.
            TypeMirror supertypecls = current.getSuperclass();
            if (supertypecls.getKind() != TypeKind.NONE) {
                TypeElement supercls = (TypeElement) ((DeclaredType) supertypecls).asElement();
                if (!superelems.contains(supercls)) {
                    stack.push(supercls);
                    superelems.add(supercls);
                }
            }
            if (addInterfaces) {
                for (TypeMirror supertypeitf : current.getInterfaces()) {
                    TypeElement superitf = (TypeElement) ((DeclaredType) supertypeitf).asElement();
                    if (!superelems.contains(superitf)) {
                        stack.push(superitf);
                        superelems.add(superitf);
                    }
                }
            }
        }

        return Collections.<TypeElement>unmodifiableList(superelems);
    }

    /**
     * Return all fields declared in the given type or any superclass/interface. TODO: should this use
     * javax.lang.model.util.Elements.getAllMembers(TypeElement) instead of our own getSuperTypes?
     */
    public static List<VariableElement> getAllFieldsIn(TypeElement type) {
        List<VariableElement> fields = new ArrayList<VariableElement>();
        fields.addAll(ElementFilter.fieldsIn(type.getEnclosedElements()));
        List<TypeElement> alltypes = getSuperTypes(type);
        for (TypeElement atype : alltypes) {
            fields.addAll(ElementFilter.fieldsIn(atype.getEnclosedElements()));
        }
        return Collections.<VariableElement>unmodifiableList(fields);
    }

    /**
     * Return all methods declared in the given type or any superclass/interface. Note that no constructors will be returned. TODO: should this
     * use javax.lang.model.util.Elements.getAllMembers(TypeElement) instead of our own getSuperTypes?
     */
    public static List<ExecutableElement> getAllMethodsIn(TypeElement type) {
        return getAllMethodsIn(type, true);
    }

    public static List<ExecutableElement> getAllMethodsIn(TypeElement type, boolean addInterfaces) {
        List<ExecutableElement> meths = new ArrayList<ExecutableElement>();
        meths.addAll(ElementFilter.methodsIn(type.getEnclosedElements()));

        List<TypeElement> alltypes = getSuperTypes(type, addInterfaces);
        for (TypeElement atype : alltypes) {
            meths.addAll(ElementFilter.methodsIn(atype.getEnclosedElements()));
        }
        return Collections.<ExecutableElement>unmodifiableList(meths);
    }

    public static boolean sameSignature(GenerationContext context, ExecutableElement executableElement1, ExecutableElement executableElement2) {
        if (!executableElement1.getSimpleName().equals(executableElement2.getSimpleName())) {
            return false;
        }
        if (executableElement1.getParameters().size() != executableElement2.getParameters().size()) {
            return false;
        }

        if (context.getTypes().isSubsignature((ExecutableType)executableElement1.asType(), (ExecutableType)executableElement2.asType())) {
            return true;
        }

        TypeElement enclosingClass1 = enclosingClass(executableElement1);
        TypeElement enclosingClass2 = enclosingClass(executableElement2);

        DeclaredType declaredType1 = context.getTypes().getDeclaredType(enclosingClass1);
        TypeMirror typeMirror1 = context.getTypes().asMemberOf(declaredType1, executableElement1);
        TypeMirror typeMirror2 = context.getTypes().asMemberOf(declaredType1, executableElement2);

        Type.MethodType methodType1 = TypesUtils.asMethodType(typeMirror1);
        Type.MethodType methodType2 = TypesUtils.asMethodType(typeMirror2);

        for (int i = 0; i < methodType1.getParameterTypes().size(); ++i) {

            Type type1 = methodType1.getParameterTypes().get(i);
            Type type2 = methodType2.getParameterTypes().get(i);

            if (!type1.tsym.equals(type2.tsym)) {
                if (!classContainsTypedParameter(enclosingClass1, executableElement1.getParameters().get(i).asType(), context)) {
                    return false;
                }

                if (!classContainsTypedParameter(enclosingClass2, executableElement2.getParameters().get(i).asType(), context)) {
                    return false;
                }

                if (!context.getTypes().isAssignable(type1, type2)) {
                    return false;
                }

            }
        }
        return true;
    }

    private static boolean classContainsTypedParameter(TypeElement classTypeElement, TypeMirror typeMirror, GenerationContext context) {
        Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol) classTypeElement;
        com.sun.tools.javac.util.List<Type> allparams = classSymbol.asType().allparams();

        for (Type allparam : allparams) {
            if (context.getTypes().isAssignable(allparam.getLowerBound(), typeMirror)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param context
     * @param model
     * @return the methods from the parent classes having the same signature as the given method. it's useful when llong for annotations.
     */
    public static List<ExecutableElement> getSameMethodFromParents(GenerationContext context, ExecutableElement model) {
        List<ExecutableElement> allMethods = ElementUtils.getAllMethodsIn(ElementUtils.enclosingClass(model), false);
        List<ExecutableElement> similar = new ArrayList<ExecutableElement>();
        for (ExecutableElement method : allMethods) {
            if (sameSignature(context, model, method)) {
                similar.add(method);
            }
        }
        return similar;
    }

    public static List<ExecutableElement> getSameMethodsFromSupertypes(GenerationContext context, TypeElement clazz, ExecutableElement model) {
        List<ExecutableElement> similar = new ArrayList<ExecutableElement>();

        List<ExecutableElement> allMethodsFromSupetypes = getAllMethodsFromSupertypes(clazz);

        for (ExecutableElement methodFromSupetype : allMethodsFromSupetypes) {
            if (sameSignature(context, model, methodFromSupetype)) {
                similar.add(methodFromSupetype);
            }
        }

        return similar;
    }

    public static List<ExecutableElement> getAllMethodsFromSupertypes(TypeElement clazz) {
        List<ExecutableElement> allMethods = new ArrayList<ExecutableElement>();

        List<TypeElement> superTypes = ElementUtils.getSuperTypes(clazz);
        for (TypeElement superType : superTypes) {
            allMethods.addAll(ElementFilter.methodsIn(superType.getEnclosedElements()));

            // recursive
            allMethods.addAll(getAllMethodsFromSupertypes(superType));
        }

        return allMethods;

    }

    public static boolean isTypeKind(Element elem) {
        return elem.getKind().isClass() || elem.getKind().isInterface();
    }

    public static boolean isConstructor(Element element) {
        if (!(element instanceof Symbol.MethodSymbol)) {
            return false;
        }
        Symbol.MethodSymbol methodElement = (Symbol.MethodSymbol) element;
        return "<init>".equals(methodElement.name.toString()) && !methodElement.getModifiers().contains(Modifier.STATIC);
    }

    public static boolean hasMultipleConstructors(Element element) {
        int constructorCount = 0;
        List<? extends Element> enclosedElements = element.getEnclosedElements();
        for (Element enclosedElement : enclosedElements) {
            if (isConstructor(enclosedElement) && !JavaNodes.isNative(enclosedElement)) {
                constructorCount++;
            }
        }
        return constructorCount > 1;
    }

    public static boolean isClass(Element element) {
        return (element != null) && (element.getKind() == ElementKind.CLASS);
    }

    public static boolean isInnerClass(Element element) {
        return isClass(element) && isClass(element.getEnclosingElement());
    }

    public static int getInnerClassDeepnessLevel(Element element) {
        int deepnessLevel = -1;

        while (isInnerClass(element)) {
            deepnessLevel++;
            element = element.getEnclosingElement();
        }

        return deepnessLevel;
    }

}
