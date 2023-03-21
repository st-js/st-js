package org.stjs.generator.javac;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Elements;

import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.Context;

//import com.sun.source.tree.AnnotatedTypeTree;

/*>>>
 import checkers.nullness.quals.*;
 */

/**
 * Static utility methods used by annotation abstractions in this package. Some methods in this class depend on the use
 * of Sun javac internals; any procedure in the Checker Framework that uses a non-public API should be placed here.
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(
		justification = "copied code", value = "BC_UNCONFIRMED_CAST")
@SuppressWarnings("PMD")
// CHECKSTYLE:OFF
public final class InternalUtils {

	// Class cannot be instantiated.
	private InternalUtils() {
		throw new AssertionError("Class InternalUtils cannot be instantiated.");
	}

	/**
	 * Gets the {@link Element} ("symbol") for the given Tree API node.
	 * 
	 * @param tree
	 *            the {@link Tree} node to get the symbol for
	 * @throws IllegalArgumentException
	 *             if {@code tree} is null or is not a valid javac-internal tree (JCTree)
	 * @return the {@code {@link Symbol} for the given tree, or null if one could not be found
	 */
	public static/* @Nullable */Element symbol(/* @Nullable */Tree tree) {
		if (tree == null) {
			ErrorReporter.errorAbort("InternalUtils.symbol: tree is null");
			return null; // dead code
		}

		if (!(tree instanceof JCTree)) {
			ErrorReporter.errorAbort("InternalUtils.symbol: tree is not a valid Javac tree");
			return null; // dead code
		}

		if (TreeUtils.isExpressionTree(tree)) {
			tree = TreeUtils.skipParens((ExpressionTree) tree);
		}

		switch (tree.getKind()) {
		case VARIABLE:
		case METHOD:
		case CLASS:

		case TYPE_PARAMETER:
			return TreeInfo.symbolFor((JCTree) tree);

			// symbol() only works on MethodSelects, so we need to get it manually
			// for method invocations.
		case METHOD_INVOCATION:
			return TreeInfo.symbol(((JCMethodInvocation) tree).getMethodSelect());

		case ASSIGNMENT:
			return TreeInfo.symbol((JCTree) ((AssignmentTree) tree).getVariable());

		case ARRAY_ACCESS:
			return symbol(((ArrayAccessTree) tree).getExpression());

		case NEW_CLASS:
			return ((JCNewClass) tree).constructor;

		default:
			if (tree.getKind().toString().equals("ENUM") || tree.getKind().toString().equals("INTERFACE")
					|| tree.getKind().toString().equals("ANNOTATION_TYPE")) {
				// java 7 & 8
				// case ENUM:
				// case INTERFACE:
				// case ANNOTATION_TYPE:
				return TreeInfo.symbolFor((JCTree) tree);
			}

			return TreeInfo.symbol((JCTree) tree);
		}
	}

	/**
	 * Determines whether or not the node referred to by the given {@link com.sun.source.util.TreePath} is an anonymous
	 * constructor (the constructor for an anonymous class.
	 * 
	 * @param method
	 *            the {@link com.sun.source.util.TreePath} for a node that may be an anonymous constructor
	 * @return true if the given path points to an anonymous constructor, false if it does not
	 */
	public static boolean isAnonymousConstructor(final MethodTree method) {
		/* @Nullable */Element e = InternalUtils.symbol(method);
		if (e == null || !(e instanceof Symbol)) {
			return false;
		}

		if ((((/* @NonNull */Symbol) e).flags() & Flags.ANONCONSTR) != 0) {
			return true;
		}

		return false;
	}

	/**
	 * indicates whether it should return the constructor that gets invoked in cases of anonymous classes
	 */
	private static final boolean RETURN_INVOKE_CONSTRUCTOR = true;

	/**
	 * Determines the symbol for a constructor given an invocation via {@code new}. If the tree is a declaration of an
	 * anonymous class, then method returns constructor that gets invoked in the extended class, rather than the
	 * anonymous constructor implicitly added by the constructor (JLS 15.9.5.1)
	 * 
	 * @param tree
	 *            the constructor invocation
	 * @return the {@link ExecutableElement} corresponding to the constructor call in {@code tree}
	 */
	public static ExecutableElement constructor(NewClassTree tree) {

		if (!(tree instanceof JCTree.JCNewClass)) {
			ErrorReporter.errorAbort("InternalUtils.constructor: not a javac internal tree");
			return null; // dead code
		}

		JCNewClass newClassTree = (JCNewClass) tree;

		if (RETURN_INVOKE_CONSTRUCTOR && tree.getClassBody() != null) {
			// anonymous constructor bodies should contain exactly one statement
			// in the form:
			// super(arg1, ...)
			// or
			// o.super(arg1, ...)
			//
			// which is a method invocation (!) to the actual constructor

			// the method call is guaranteed to return nonnull
			JCMethodDecl anonConstructor = (JCMethodDecl) TreeInfo.declarationFor(newClassTree.constructor, newClassTree);
			assert anonConstructor != null;
			assert anonConstructor.body.stats.size() == 1;
			JCExpressionStatement stmt = (JCExpressionStatement) anonConstructor.body.stats.head;
			JCTree.JCMethodInvocation superInvok = (JCMethodInvocation) stmt.expr;
			return (ExecutableElement) TreeInfo.symbol(superInvok.meth);
		}

		Element e = newClassTree.constructor;

		assert e instanceof ExecutableElement;

		return (ExecutableElement) e;
	}

	// public final static List<AnnotationMirror> annotationsFromTypeAnnotationTrees(List<? extends AnnotationTree>
	// annos) {
	// List<AnnotationMirror> annotations = new ArrayList<AnnotationMirror>(annos.size());
	// for (AnnotationTree anno : annos) {
	// annotations.add(((JCAnnotation) anno).attribute);
	// }
	// return annotations;
	// }
	//
	// public final static List<? extends AnnotationMirror> annotationsFromTree(AnnotatedTypeTree node) {
	// return annotationsFromTypeAnnotationTrees(((JCAnnotatedType) node).annotations);
	// }
	//
	// public final static List<? extends AnnotationMirror> annotationsFromTree(TypeParameterTree node) {
	// return annotationsFromTypeAnnotationTrees(((JCTypeParameter) node).annotations);
	// }
	//
	// public final static List<? extends AnnotationMirror> annotationsFromArrayCreation(NewArrayTree node, int level) {
	//
	// assert node instanceof JCNewArray;
	// final JCNewArray newArray = ((JCNewArray) node);
	//
	// if (level == -1) {
	// return annotationsFromTypeAnnotationTrees(newArray.annotations);
	// }
	//
	// if (newArray.dimAnnotations.length() > 0 && (level >= 0) && (level < newArray.dimAnnotations.size())) {
	// return annotationsFromTypeAnnotationTrees(newArray.dimAnnotations.get(level));
	// }
	//
	// return Collections.emptyList();
	// }

	public static TypeMirror typeOf(Tree tree) {
		return ((JCTree) tree).type;
	}

	/**
	 * Returns whether a TypeVariable represents a captured type.
	 */
	// public static boolean isCaptured(TypeVariable typeVar) {
	// return ((Type.TypeVar) typeVar).isCaptured();
	// }

	/**
	 * Returns whether a TypeMirror represents a class type.
	 */
	public static boolean isClassType(TypeMirror type) {
		return type instanceof Type.ClassType;
	}

	/**
	 * Returns the least upper bound of two {@link TypeMirror}s.
	 * 
	 * @param processingEnv
	 *            The {@link ProcessingEnvironment} to use.
	 * @param tm1
	 *            A {@link TypeMirror}.
	 * @param tm2
	 *            A {@link TypeMirror}.
	 * @return The least upper bound of {@code tm1} and {@code tm2}.
	 */
	public static TypeMirror leastUpperBound(ProcessingEnvironment processingEnv, TypeMirror tm1, TypeMirror tm2) {
		Type t1 = (Type) tm1;
		Type t2 = (Type) tm2;
		JavacProcessingEnvironment javacEnv = (JavacProcessingEnvironment) processingEnv;
		Types types = Types.instance(javacEnv.getContext());
		if (types.isSameType(t1, t2)) {
			// Special case if the two types are equal.
			return t1;
		}
		// Handle the 'null' type manually (not done by types.lub).
		if (t1.getKind() == TypeKind.NULL) {
			return t2;
		}
		if (t2.getKind() == TypeKind.NULL) {
			return t1;
		}
		// Special case for primitives.
		if (TypesUtils.isPrimitive(t1) || TypesUtils.isPrimitive(t2)) {
			if (types.isAssignable(t1, t2)) {
				return t2;
			} else if (types.isAssignable(t2, t1)) {
				return t1;
			} else {
				return processingEnv.getTypeUtils().getNoType(TypeKind.NONE);
			}
		}
		if (t1.getKind() == TypeKind.WILDCARD) {
			WildcardType wc1 = (WildcardType) t1;
			Type bound = (Type) wc1.getExtendsBound();
			if (bound == null) {
				// Implicit upper bound of java.lang.Object
				Elements elements = processingEnv.getElementUtils();
				return elements.getTypeElement("java.lang.Object").asType();
			}
			t1 = bound;
		}
		if (t2.getKind() == TypeKind.WILDCARD) {
			WildcardType wc2 = (WildcardType) t2;
			Type bound = (Type) wc2.getExtendsBound();
			if (bound == null) {
				// Implicit upper bound of java.lang.Object
				Elements elements = processingEnv.getElementUtils();
				return elements.getTypeElement("java.lang.Object").asType();
			}
			t2 = bound;
		}
		return types.lub(t1, t2);
	}

	/**
	 * Returns the greatest lower bound of two {@link TypeMirror}s.
	 * 
	 * @param processingEnv
	 *            The {@link ProcessingEnvironment} to use.
	 * @param tm1
	 *            A {@link TypeMirror}.
	 * @param tm2
	 *            A {@link TypeMirror}.
	 * @return The greatest lower bound of {@code tm1} and {@code tm2}.
	 */
	public static TypeMirror greatestLowerBound(ProcessingEnvironment processingEnv, TypeMirror tm1, TypeMirror tm2) {
		Type t1 = (Type) tm1;
		Type t2 = (Type) tm2;
		JavacProcessingEnvironment javacEnv = (JavacProcessingEnvironment) processingEnv;
		Types types = Types.instance(javacEnv.getContext());
		if (types.isSameType(t1, t2)) {
			// Special case if the two types are equal.
			return t1;
		}
		// Handle the 'null' type manually.
		if (t1.getKind() == TypeKind.NULL) {
			return t1;
		}
		if (t2.getKind() == TypeKind.NULL) {
			return t2;
		}
		// Special case for primitives.
		if (TypesUtils.isPrimitive(t1) || TypesUtils.isPrimitive(t2)) {
			if (types.isAssignable(t1, t2)) {
				return t1;
			} else if (types.isAssignable(t2, t1)) {
				return t2;
			} else {
				// Javac types.glb returns TypeKind.Error when the GLB does
				// not exist, but we can't create one. Use TypeKind.NONE
				// instead.
				return processingEnv.getTypeUtils().getNoType(TypeKind.NONE);
			}
		}
		if (t1.getKind() == TypeKind.WILDCARD) {
			return t2;
		}
		if (t2.getKind() == TypeKind.WILDCARD) {
			return t1;
		}
		return types.glb(t1, t2);
	}

	/**
	 * Returns the return type of a method, where the "raw" return type of that method is given (i.e., the return type
	 * might still contain unsubstituted type variables), given the receiver of the method call.
	 */
	public static TypeMirror substituteMethodReturnType(TypeMirror methodType, TypeMirror substitutedReceiverType) {
		if (methodType.getKind() != TypeKind.TYPEVAR) {
			return methodType;
		}
		// TODO: find a nicer way to substitute type variables
		String t = methodType.toString();
		Type finalReceiverType = (Type) substitutedReceiverType;
		int i = 0;
		for (TypeSymbol typeParam : finalReceiverType.tsym.getTypeParameters()) {
			if (t.equals(typeParam.toString())) {
				return finalReceiverType.getTypeArguments().get(i);
			}
			i++;
		}
		assert false;
		return null;
	}

	/**
	 * Helper function to extract the javac Context from the javac processing environment.
	 * 
	 * @param env
	 *            the processing environment
	 * @return the javac Context
	 */
	public static Context getJavacContext(ProcessingEnvironment env) {
		return ((JavacProcessingEnvironment) env).getContext();
	}

	/**
	 * @param element
	 * @return Type$1 for inner types
	 */
	public static String getSimpleName(Element element) {
		if (element.getSimpleName().length() != 0) {
			return element.getSimpleName().toString();
		}

		// take the binary name for anonymous classes
		PackageElement pack = ElementUtils.enclosingPackage(element);
		String packageName = pack != null && !pack.isUnnamed() ? pack.getQualifiedName().toString() : "";

		if (element instanceof ClassSymbol) {
			return ((ClassSymbol) element).flatName().toString().substring(packageName.length() + 1);
		}
		return null;
	}

	/**
	 * @param tree
	 * @return true if the node is a vararg
	 */
	public static boolean isVarArg(Tree tree) {
		if (!(tree instanceof VariableTree)) {
			return false;
		}

		/* @Nullable */Element e = InternalUtils.symbol(tree);
		if (e == null || !(e instanceof Symbol)) {
			return false;
		}

		if ((((/* @NonNull */Symbol) e).flags() & Flags.VARARGS) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isSynthetic(Tree tree) {

		/* @Nullable */Element e = InternalUtils.symbol(tree);
		if (e == null || !(e instanceof Symbol)) {
			return false;
		}

		if ((((/* @NonNull */Symbol) e).flags() & Flags.GENERATEDCONSTR) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isSyntheticConstructor(Tree tree) {
		Element e = InternalUtils.symbol(tree);
		if (e == null || e.getKind() != ElementKind.CONSTRUCTOR) {
			return false;
		}
		return isSynthetic(tree);
	}
}
// CHECKSTYLE:ON