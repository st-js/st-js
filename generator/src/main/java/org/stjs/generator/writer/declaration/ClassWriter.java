package org.stjs.generator.writer.declaration;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.Tree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptTypes;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.javascript.annotation.STJSBridge;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassWriter<JS> extends JavascriptTypes<JS> implements WriterContributor<ClassTree, JS> {

	private InterfaceWriter interfaceWriter;
	private EnumWriter enumWriter;

	public ClassWriter() {
		interfaceWriter = new InterfaceWriter();
		enumWriter = new EnumWriter();
	}

	/**
	 * @return the node to put in the super class. for interfaces, the super class goes also in the interfaces list
	 */
	private JS getSuperClass(ClassTree clazz, GenerationContext<JS> context) {
		Element type = TreeUtils.elementFromDeclaration(clazz);
		if (clazz.getExtendsClause() == null || type.getKind() == ElementKind.INTERFACE) {
			// no super class found
			return null;
		}

		Tree superClazz = clazz.getExtendsClause();
		TreeWrapper<Tree, JS> superType = context.getCurrentWrapper().child(superClazz);
		if (superType.isSyntheticType()) {
			return null;
		}

		DependencyType depType = getDependencyTypeForClassDef(type);

		JS typeName = context.js().name(superType.getTypeName(depType));

		if (superClazz instanceof ClassTree) {
			List<JS> typeParameters = getTypeParams(((ClassTree) superClazz).getTypeParameters(), context);
			if (typeParameters != null) {
				return context.js().genericType(typeName, typeParameters);
			}
		}

		return typeName;
	}

	private DependencyType getDependencyTypeForClassDef(Element type) {
		if (JavaNodes.isInnerType(type)) {
			// this is an inner (anonymous or not) class -> STATIC dep type instead
			return DependencyType.STATIC;
		}
		return DependencyType.EXTENDS;
	}

	/**
	 * @return the list of implemented interfaces. for intefaces, the super class goes also in the interfaces list
	 */
	private List<JS> getInterfaces(ClassTree clazz, GenerationContext<JS> context) {
		Element type = TreeUtils.elementFromDeclaration(clazz);
		DependencyType depType = getDependencyTypeForClassDef(type);

		List<JS> ifaces = new ArrayList<JS>();
		for (Tree iface : clazz.getImplementsClause()) {
			TreeWrapper<Tree, JS> ifaceType = context.getCurrentWrapper().child(iface);
			if (ifaceType.isSyntheticType()) {
				continue;
			}

			JS typeName = context.js().name(ifaceType.getTypeName(depType));

			if (iface instanceof ParameterizedTypeTree) {
				List<JS> typeParameters = getTypeParams(((ParameterizedTypeTree) iface).getTypeArguments(), context);
				if (typeParameters != null) {
					ifaces.add(context.js().genericType(typeName, typeParameters));
					continue;
				}
			}

			// TODO :: this might never come
			/*if (iface instanceof ClassTree) {
				List<JS> typeParameters = getTypeParams(((ClassTree) iface).getTypeParameters(), context);
				if (typeParameters != null) {
					ifaces.add(context.js().genericType(typeName, typeParameters));
					continue;
				}
			}*/

			ifaces.add(typeName);

		}

		if (clazz.getExtendsClause() != null && type.getKind() == ElementKind.INTERFACE) {
			TreeWrapper<Tree, JS> superType = context.getCurrentWrapper().child(clazz.getExtendsClause());
			if (!superType.isSyntheticType()) {
				JS typeName = context.js().name(superType.getTypeName(DependencyType.EXTENDS));

				if (superType instanceof ClassTree) {
					List<JS> typeParameters = getTypeParams(((ClassTree) superType).getTypeParameters(), context);
					if (typeParameters != null) {
						ifaces.add(0, context.js().genericType(typeName, typeParameters));
					} else {
						ifaces.add(0, typeName);
					}
				} else {
					ifaces.add(0, typeName);
				}
			}
		}
		return ifaces;
	}

	private List<Tree> getAllMembers(ClassTree clazz) {
		List<Tree> nonConstructors = new ArrayList<>();
		for (Tree member : clazz.getMembers()) {
			boolean isNative = false;
			if (member instanceof MethodTree) {
				ExecutableElement methodElement = TreeUtils.elementFromDeclaration((MethodTree) member);
				isNative = JavaNodes.isNative(methodElement);
			}
			if (!(member instanceof BlockTree) && !isNative) {
				nonConstructors.add(member);
			}
		}
		return nonConstructors;
	}

	/**
	 * @return the JavaScript node for the class' members
	 */
	private List<JS> getMembers(WriterVisitor<JS> visitor, ClassTree clazz, GenerationContext<JS> context) {
		// the following members must not appear in the initializer function:
		// - constructors (they are printed elsewhere)
		// - abstract methods (they should be omitted)

		List<Tree> members = getAllMembers(clazz);
		List<JS> stmts = new ArrayList<>();

		if (members.isEmpty()) {
			return stmts;
		}

		for (Tree member : members) {
			JS jsMember = visitor.scan(member, context);
			if (jsMember != null) {
				stmts.add(jsMember);
			}
		}

		return stmts;
	}

	private void addStaticInitializers(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		for (Tree member : tree.getMembers()) {
			if (member instanceof BlockTree) {
				stmts.add(visitor.scan(member, context));
			}
		}
	}

	public static boolean isMainMethod(MethodTree method) {
		if (JavaNodes.isStatic(method) && "main".equals(method.getName().toString()) && method.getParameters().size() == 1) {
			VariableElement var = TreeUtils.elementFromDeclaration(method.getParameters().get(0));
			if (var.asType() instanceof ArrayType) {
				TypeMirror componentType = ((ArrayType) var.asType()).getComponentType();
				return TypesUtils.isString(componentType);
			}
		}
		return false;
	}

	private boolean hasMainMethod(ClassTree clazz) {
		for (Tree member : clazz.getMembers()) {
			if (!(member instanceof MethodTree)) {
				continue;
			}
			MethodTree method = (MethodTree) member;

			if (isMainMethod(method)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * add the call to the main method, if it exists
	 */
	private void addMainMethodCall(ClassTree clazz, List<JS> stmts, GenerationContext<JS> context) {
		if (!hasMainMethod(clazz)) {
			return;
		}
		TypeElement type = TreeUtils.elementFromDeclaration(clazz);
		JS target = context.getCurrentWrapper().isGlobal() ? null : context.js().name(
				context.getNames().getTypeName(context, type, DependencyType.STATIC));

		JavaScriptBuilder<JS> js = context.js();
		JS condition = js.unary(UnaryOperator.LOGICAL_COMPLEMENT, js.property(js.name(GeneratorConstants.STJS), "mainCallDisabled"));
		JS thenPart = js.expressionStatement(js.functionCall(js.property(target, "main"), Collections.<JS>emptyList()));
		stmts.add(js.ifStatement(condition, thenPart, null));
	}

	private List<Tree> getAllClasses(ClassTree clazz) {
		List<Tree> enums = new ArrayList<>();
		for (Tree member : clazz.getMembers()) {
			if (member instanceof ClassTree && member.getKind() == Tree.Kind.CLASS) {
				enums.add(member);

				// Get classes recursively
				enums.addAll(getAllClasses((ClassTree) member));
			}
		}
		return enums;
	}

	/**
	 * Special generation for classes marked with {@link org.stjs.javascript.annotation.GlobalScope}. The name of the
	 * class must appear nowhere.
	 */
	private boolean generateGlobal(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		if (!context.getCurrentWrapper().isGlobal()) {
			return false;
		}

		// print members
		List<Tree> nonConstructors = getAllMembers(tree);
		for (Tree member : nonConstructors) {
			if (JavaNodes.isConstructor(member)) {
				continue;
			}

			stmts.add(visitor.scan(member, context));
		}

		addStaticInitializers(visitor, tree, context, stmts);
		addMainMethodCall(tree, stmts, context);

		return true;
	}

	private boolean isAnonymousClass(ClassTree tree) {
		return tree.getSimpleName().length() == 0;
	}

	public JS getClassName(ClassTree tree, GenerationContext<JS> context) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		String typeName = context.getNames().getTypeName(context, type, DependencyType.EXTENDS);

		if (typeName.contains(".") && type.getEnclosingElement().getKind() != ElementKind.PACKAGE) {
			typeName = typeName.replace('.', '_');
		}

		List<JS> typeParameters = getTypeParams(tree.getTypeParameters(), context);

		JS name = context.js().name(typeName);

		if (typeParameters != null) {
			return context.js().genericType(name, typeParameters);
		}

		return name;
	}

	public void generateClass(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		JavaScriptBuilder<JS> js = context.js();

		JS name = getClassName(tree, context);
		JS superClazz = getSuperClass(tree, context);
		List<JS> interfaces = getInterfaces(tree, context);
		List<JS> members = getMembers(visitor, tree, context);
		//JS typeDesc = getTypeDescription(visitor, tree, context);

		stmts.add(js.classDeclaration(name, members, superClazz, interfaces, context.getCurrentWrapper().isAbstract()));

		addStaticInitializers(visitor, tree, context, stmts);
		addMainMethodCall(tree, stmts, context);
	}

	public void generateReference(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		boolean innerClass = type.getEnclosingElement().getKind() != ElementKind.PACKAGE;

		if (!innerClass) {
			return;
		}

		String typeName = context.getNames().getTypeName(context, type, DependencyType.EXTENDS);

		DeclaredType declaredType = (DeclaredType) type.asType();
		String name = InternalUtils.getSimpleName(declaredType.asElement());

		JavaScriptBuilder<JS> js = context.js();

		stmts.add(
				js.field(name, js.name(typeName), true, null)
		);
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context) {
		if (isBridge(tree)) {
			// class is actually a bridge. This path is usually not reached for top-level classes (because the
			// generator skips top-level classes with @STJSBridge altogether), but can happen for inner classes
			return null;
		}

		JavaScriptBuilder<JS> js = context.js();
		List<JS> stmts = new ArrayList<JS>();
		if (generateGlobal(visitor, tree, context, stmts)) {
			// special construction for globals
			return js.statements(stmts);
		}

		Element type = TreeUtils.elementFromDeclaration(tree);

		// Are we currently generating the main class of a file ?
		boolean outerClass = type.getEnclosingElement().getKind() == ElementKind.PACKAGE;

		// Move member interfaces, enums and classes to the top level
		if (outerClass) {
			// Generate all enums
			enumWriter.generateAll(visitor, tree, context, stmts);

			// Generate all interfaces
			interfaceWriter.generateAll(visitor, tree, context, stmts);

			// Generate all inner classes
			List<Tree> classes = getAllClasses(tree);
			for (Tree member : classes) {
				generateClass(visitor, (ClassTree) member, context, stmts);
			}
		}

		// Render members as references to the top level class
		if (type.getKind() == ElementKind.ENUM) {
			generateReference(visitor, tree, context, stmts);
			return js.statements(stmts);
		}

		// Render interfaces as references to the top level class
		if (type.getKind() == ElementKind.INTERFACE) {
			// TODO :: do we need interface references ?
			//generateReference(visitor, tree, context, stmts);
			return js.statements(stmts);
		}

		// Render members as references to the top level enums
		if (type.getKind() == ElementKind.CLASS && !isAnonymousClass(tree)) {
			generateReference(visitor, tree, context, stmts);
			//return js.statements(stmts);
		}

		// Render top level class
		if (type.getKind() == ElementKind.CLASS && (outerClass || isAnonymousClass(tree))) {
			generateClass(visitor, tree, context, stmts);
		}

		return js.statements(stmts);
	}

	private boolean isBridge(ClassTree tree) {
		TypeElement type = TreeUtils.elementFromDeclaration(tree);
		List<? extends AnnotationMirror> annotations = type.getAnnotationMirrors();
		for (AnnotationMirror a : annotations) {
			if (a.getAnnotationType().toString().equals(STJSBridge.class.getName())) {
				return true;
			}
		}
		return false;
	}
}
