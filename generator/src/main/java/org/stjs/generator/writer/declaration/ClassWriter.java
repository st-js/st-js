package org.stjs.generator.writer.declaration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.javascript.NameValue;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

public class ClassWriter<JS> implements WriterContributor<ClassTree, JS> {

	/**
	 * generate the namespace declaration stjs.ns("namespace") if needed
	 */
	private void addNamespace(ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		if (JavaNodes.isInnerType(type)) {
			// this is an inner (anonymous or not) class - no namespace declaration is generated
			return;
		}
		String namespace = JavaNodes.getNamespace(type);
		if (namespace != null) {
			JavaScriptBuilder<JS> js = context.js();
			JS target = js.property(js.name(GeneratorConstants.STJS), "ns");
			stmts.add(js.expressionStatement(js.functionCall(target, Collections.singleton(js.string(namespace)))));
		}
	}

	/**
	 * @return the node to put in the super class. for intefaces, the super class goes also in the interfaces list
	 */
	private JS getSuperClass(ClassTree clazz, GenerationContext<JS> context) {
		Element type = TreeUtils.elementFromDeclaration(clazz);
		if (clazz.getExtendsClause() == null || type.getKind() == ElementKind.INTERFACE) {
			// no super class found
			return context.js().keyword(Keyword.NULL);
		}

		Element superType = TreeUtils.elementFromUse((ExpressionTree) clazz.getExtendsClause());
		return context.js().name(context.getNames().getTypeName(context, superType));
	}

	/**
	 * 
	 @return the list of implemented interfaces. for intefaces, the super class goes also in the interfaces list
	 */
	private JS getInterfaces(ClassTree clazz, GenerationContext<JS> context) {
		List<JS> ifaces = new ArrayList<JS>();
		for (Tree iface : clazz.getImplementsClause()) {
			Element ifaceType = TreeUtils.elementFromUse((ExpressionTree) iface);
			ifaces.add(context.js().name(context.getNames().getTypeName(context, ifaceType)));
		}

		Element type = TreeUtils.elementFromDeclaration(clazz);
		if (clazz.getExtendsClause() != null && type.getKind() == ElementKind.INTERFACE) {
			Element superType = TreeUtils.elementFromUse((ExpressionTree) clazz.getExtendsClause());
			ifaces.add(0, context.js().name(context.getNames().getTypeName(context, superType)));
		}
		return context.js().array(ifaces);
	}

	/**
	 * @return the JavaScript node for the class' constructor
	 */
	private JS getConstructor(WriterVisitor<JS> visitor, ClassTree clazz, GenerationContext<JS> context) {
		for (Tree member : clazz.getMembers()) {
			if (JavaNodes.isConstructor(member)) {
				// TODO skip the "native" constructors
				JS node = visitor.scan(member, context);
				if (node != null) {
					return node;
				}
			}
		}
		// no constructor found : interfaces, return an empty function
		return context.js().function(null, Collections.<JS>emptyList(), null);
	}

	private List<Tree> getAllMembersExceptConstructors(ClassTree clazz) {
		List<Tree> nonConstructors = new ArrayList<Tree>();
		for (Tree member : clazz.getMembers()) {
			if (!JavaNodes.isConstructor(member) && !isAbstractInstanceMethod(member) && !(member instanceof BlockTree)) {
				nonConstructors.add(member);
			}
		}
		return nonConstructors;
	}

	/**
	 * @return the JavaScript node for the class' members
	 */
	private JS getMembers(WriterVisitor<JS> visitor, ClassTree clazz, GenerationContext<JS> context) {
		// the following members must not appear in the initializer function:
		// - constructors (they are printed elsewhere)
		// - abstract methods (they should be omitted)

		List<Tree> nonConstructors = getAllMembersExceptConstructors(clazz);

		if (nonConstructors.isEmpty()) {
			return context.js().keyword(Keyword.NULL);
		}
		@SuppressWarnings("unchecked")
		List<JS> params = Arrays.asList(context.js().name(JavascriptKeywords.CONSTRUCTOR), context.js().name(JavascriptKeywords.PROTOTYPE));

		List<JS> stmts = new ArrayList<JS>();
		for (Tree member : nonConstructors) {
			stmts.add(visitor.scan(member, context));
		}

		return context.js().function(null, params, context.js().block(stmts));
	}

	private boolean isAbstractInstanceMethod(Tree member) {
		if (!(member instanceof MethodTree)) {
			return false;
		}
		MethodTree methodTree = (MethodTree) member;
		return methodTree.getBody() == null;
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
		JS target = JavaNodes.isGlobal(type) ? null : context.js().name(context.getNames().getTypeName(context, type));

		JavaScriptBuilder<JS> js = context.js();
		JS condition = js.unary(UnaryOperator.LOGICAL_COMPLEMENT, js.property(js.name(GeneratorConstants.STJS), "mainCallDisabled"));
		JS thenPart = js.expressionStatement(js.functionCall(js.property(target, "main"), Collections.<JS>emptyList()));
		stmts.add(js.ifStatement(condition, thenPart, null));
	}

	@SuppressWarnings("unchecked")
	private JS getFieldTypeDesc(TypeMirror type, GenerationContext<JS> context) {
		JavaScriptBuilder<JS> js = context.js();
		if (JavaNodes.isJavaScriptPrimitive(type)) {
			return js.keyword(Keyword.NULL);
		}
		JS typeName = js.string(context.getNames().getTypeName(context, type));

		if (type instanceof DeclaredType) {
			DeclaredType declaredType = (DeclaredType) type;

			// enum
			if (declaredType.asElement().getKind() == ElementKind.ENUM) {
				return js.object(Arrays.asList(NameValue.of("name", js.string("Enum")),
						NameValue.of("arguments", js.array(Collections.singleton(typeName)))));
			}
			// parametrized type
			if (!declaredType.getTypeArguments().isEmpty()) {
				List<JS> array = new ArrayList<JS>();
				for (TypeMirror arg : declaredType.getTypeArguments()) {
					array.add(getFieldTypeDesc(arg, context));
				}
				return js.object(Arrays.asList(NameValue.of("name", typeName), NameValue.of("arguments", js.array(array))));
			}

		}

		return typeName;
	}

	@SuppressWarnings("unused")
	private JS getTypeDescription(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context) {
		// if (isGlobal(type)) {
		// printer.print(JavascriptKeywords.NULL);
		// return;
		// }

		TypeElement type = TreeUtils.elementFromDeclaration(tree);

		List<NameValue<JS>> props = new ArrayList<NameValue<JS>>();
		for (Element member : ElementUtils.getAllFieldsIn(type)) {
			TypeMirror memberType = ElementUtils.getType(member);
			if (JavaNodes.isJavaScriptPrimitive(memberType)) {
				continue;
			}
			if (member.getKind() == ElementKind.ENUM_CONSTANT) {
				continue;
			}
			if (memberType instanceof TypeVariable) {
				// what to do with fields of generic parameters !?
				continue;
			}
			props.add(NameValue.of(member.getSimpleName(), getFieldTypeDesc(memberType, context)));
		}
		return context.js().object(props);
	}

	/**
	 * transform a.b.type in constructor.type
	 */
	private String replaceFullNameWithConstructor(String typeName) {
		int pos = typeName.lastIndexOf('.');
		return JavascriptKeywords.CONSTRUCTOR + typeName.substring(pos);
	}

	@SuppressWarnings("unused")
	private boolean generareEnum(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		if (type.getKind() != ElementKind.ENUM) {
			return false;
		}

		JavaScriptBuilder<JS> js = context.js();

		// add all anum entries
		List<JS> enumEntries = new ArrayList<JS>();
		for (Element member : ElementUtils.getAllFieldsIn((TypeElement) type)) {
			if (member.getKind() == ElementKind.ENUM_CONSTANT) {
				enumEntries.add(js.string(member.getSimpleName().toString()));
			}
		}

		JS enumConstructor = js.functionCall(js.property(js.name(GeneratorConstants.STJS), "enumeration"), enumEntries);

		String typeName = context.getNames().getTypeName(context, type);
		if (typeName.contains(".")) {
			// inner class or namespace
			boolean innerClass = type.getEnclosingElement().getKind() != ElementKind.PACKAGE;
			String leftSide = innerClass ? replaceFullNameWithConstructor(typeName) : typeName;

			stmts.add(js.expressionStatement(js.assignment(AssignOperator.ASSIGN, js.name(leftSide), enumConstructor)));
		} else {
			// regular class
			stmts.add(js.variableDeclaration(true, Collections.singleton(NameValue.of(typeName, enumConstructor))));
		}

		return true;
	}

	/**
	 * Special generation for classes marked with {@link org.stjs.javascript.annotation.GlobalScope}. The name of the
	 * class must appear nowhere.
	 */
	private boolean generateGlobal(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		if (!JavaNodes.isGlobal(type)) {
			return false;
		}

		// print members
		List<Tree> nonConstructors = getAllMembersExceptConstructors(tree);
		for (Tree member : nonConstructors) {
			stmts.add(visitor.scan(member, context));
		}

		addStaticInitializers(visitor, tree, context, stmts);
		addMainMethodCall(tree, stmts, context);

		return true;
	}

	private void addConstructorStatement(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		boolean anonymousClass = tree.getSimpleName().length() == 0;
		if (anonymousClass) {
			// anonymous class - nothing to do the constructor will be added directly
			return;
		}

		JavaScriptBuilder<JS> js = context.js();
		Element type = TreeUtils.elementFromDeclaration(tree);
		String typeName = context.getNames().getTypeName(context, type);

		if (typeName.contains(".")) {
			// inner class or namespace
			// generate [ns.]typeName = function() {...}
			boolean innerClass = type.getEnclosingElement().getKind() != ElementKind.PACKAGE;
			String leftSide = innerClass ? replaceFullNameWithConstructor(typeName) : typeName;

			stmts.add(js.expressionStatement(js.assignment(AssignOperator.ASSIGN, js.name(leftSide), getConstructor(visitor, tree, context))));
		} else {
			// regular class
			// generate var typeName = function() {...}
			stmts.add(js.variableDeclaration(true, typeName, getConstructor(visitor, tree, context)));
		}
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context) {
		JavaScriptBuilder<JS> js = context.js();
		List<JS> stmts = new ArrayList<JS>();
		if (generateGlobal(visitor, tree, context, stmts)) {
			// special construction for globals
			return js.statements(stmts);
		}

		addNamespace(tree, context, stmts);

		if (generareEnum(visitor, tree, context, stmts)) {
			// special construction for enums
			return js.statements(stmts);
		}

		Element type = TreeUtils.elementFromDeclaration(tree);
		String typeName = context.getNames().getTypeName(context, type);
		JS name = js.name(typeName);

		JS superClazz = getSuperClass(tree, context);
		JS interfaces = getInterfaces(tree, context);
		JS members = getMembers(visitor, tree, context);
		JS typeDesc = getTypeDescription(visitor, tree, context);
		boolean anonymousClass = tree.getSimpleName().length() == 0;

		if (anonymousClass) {
			// anonymous class
			name = getConstructor(visitor, tree, context);
		}
		addConstructorStatement(visitor, tree, context, stmts);

		@SuppressWarnings("unchecked")
		JS extendsCall = js.functionCall(js.property(js.name(GeneratorConstants.STJS), "extend"),
				Arrays.asList(name, superClazz, interfaces, members, typeDesc));
		if (anonymousClass) {
			stmts.add(extendsCall);
		} else {
			stmts.add(js.expressionStatement(extendsCall));
		}
		addStaticInitializers(visitor, tree, context, stmts);
		addMainMethodCall(tree, stmts, context);

		return js.statements(stmts);
	}
}
