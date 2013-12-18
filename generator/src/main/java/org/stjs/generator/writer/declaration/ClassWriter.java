package org.stjs.generator.writer.declaration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javacutils.ElementUtils;
import javacutils.TreeUtils;
import javacutils.TypesUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.javascript.annotation.GlobalScope;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

public class ClassWriter<JS> implements WriterContributor<ClassTree, JS> {

	/**
	 * generate the namespace declaration stjs.ns("namespace") if needed
	 */
	@SuppressWarnings("unused")
	private void addNamespace(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		if (JavaNodes.isInnerType(type)) {
			// this is an inner (anonymous or not) class - no namespace declaration is generated
			return;
		}
		String namespace = JavaNodes.getNamespace(type);
		if (namespace != null) {
			JavaScriptBuilder<JS> js = context.js();
			JS target = js.property(js.name("stjs"), "ns");
			stmts.add(js.expressionStatement(js.functionCall(target, Collections.singleton(js.string(namespace)))));
		}
	}

	/**
	 * @return the node to put in the super class. for intefaces, the super class goes also in the interfaces list
	 */
	@SuppressWarnings("unused")
	private JS getSuperClass(WriterVisitor<JS> visitor, ClassTree clazz, GenerationContext<JS> context) {
		Element type = TreeUtils.elementFromDeclaration(clazz);
		if (clazz.getExtendsClause() == null || type.getKind() == ElementKind.INTERFACE) {
			// no super class found
			return context.js().keyword(Token.NULL);
		}

		Element superType = TreeUtils.elementFromUse((ExpressionTree) clazz.getExtendsClause());
		return context.js().name(context.getNames().getTypeName(context, superType));
	}

	/**
	 * 
	 @return the list of implemented interfaces. for intefaces, the super class goes also in the interfaces list
	 */
	private JS getInterfaces(WriterVisitor<JS> visitor, ClassTree clazz, GenerationContext<JS> context) {
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
		return context.js().function(null, Collections.<JS> emptyList(), null);
	}

	/**
	 * @return the JavaScript node for the class' members
	 */
	private JS getMembers(WriterVisitor<JS> visitor, ClassTree clazz, GenerationContext<JS> context) {
		// the following members must not appear in the initializer function:
		// - constructors (they are printed elsewhere)
		// - abstract methods (they should be omitted)

		List<Tree> nonConstructors = new ArrayList<Tree>();
		for (Tree member : clazz.getMembers()) {
			if (!JavaNodes.isConstructor(member) && !isAbstractInstanceMethod(member) && !(member instanceof BlockTree)) {
				nonConstructors.add(member);
			}
		}

		if (nonConstructors.isEmpty()) {
			return context.js().keyword(Token.NULL);
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
		Element memberElement = TreeUtils.elementFromDeclaration((MethodTree) member);
		if (memberElement.getEnclosingElement().getKind() == ElementKind.INTERFACE) {
			// an interface method is like an instance method
			return true;
		}

		Set<Modifier> modifiers = ((MethodTree) member).getModifiers().getFlags();
		return modifiers.contains(Modifier.ABSTRACT) && !modifiers.contains(Modifier.STATIC);
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
	private void addMainMethodCall(ClassTree clazz, List<AstNode> stmts, GenerationContext context) {
		if (!hasMainMethod(clazz)) {
			return;
		}
		TypeElement type = TreeUtils.elementFromDeclaration(clazz);
		AstNode target = JavaNodes.isGlobal(type) ? null : name(context.getNames().getTypeName(context, type));

		IfStatement ifs = new IfStatement();
		ifs.setCondition(JavaScriptNodes.not(JavaScriptNodes.property(name("stjs"), "mainCallDisabled")));
		ifs.setThenPart(statement(functionCall(target, "main")));
		stmts.add(ifs);
	}

	private AstNode getFieldTypeDesc(TypeMirror type, GenerationContext context) {
		if (JavaNodes.isJavaScriptPrimitive(type)) {
			return NULL();
		}
		AstNode typeName = string(context.getNames().getTypeName(context, type));

		if (type instanceof DeclaredType) {
			DeclaredType declaredType = (DeclaredType) type;

			// enum
			if (declaredType.asElement().getKind() == ElementKind.ENUM) {
				return object("name", string("Enum"), "arguments", array(typeName));
			}
			// parametrized type
			if (!declaredType.getTypeArguments().isEmpty()) {
				ArrayLiteral array = new ArrayLiteral();
				for (TypeMirror arg : declaredType.getTypeArguments()) {
					array.addElement(getFieldTypeDesc(arg, context));
				}
				return object("name", typeName, "arguments", array);
			}

		}

		return typeName;
	}

	@SuppressWarnings("unused")
	private AstNode getTypeDescription(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext context) {
		// if (isGlobal(type)) {
		// printer.print(JavascriptKeywords.NULL);
		// return;
		// }

		TypeElement type = TreeUtils.elementFromDeclaration(tree);
		ObjectLiteral typeDesc = new ObjectLiteral();

		for (Element member : ElementUtils.getAllFieldsIn(type)) {
			TypeMirror memberType = ElementUtils.getType(member);
			if (JavaNodes.isJavaScriptPrimitive(memberType)) {
				continue;
			}
			if (member.getKind() == ElementKind.ENUM_CONSTANT) {
				continue;
			}
			if (memberType instanceof TypeVariable) {
				//what to do with fields of generic parameters !?
				continue;
			}
			typeDesc.addElement(objectProperty(member.getSimpleName().toString(), getFieldTypeDesc(memberType, context)));
		}
		return typeDesc;
	}

	/**
	 * transform a.b.type in constructor.type
	 */
	private String replaceFullNameWithConstructor(String typeName) {
		int pos = typeName.lastIndexOf('.');
		return JavascriptKeywords.CONSTRUCTOR + typeName.substring(pos);
	}

	@SuppressWarnings("unused")
	private boolean generareEnum(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext context, List<AstNode> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		if (type.getKind() != ElementKind.ENUM) {
			return false;
		}

		// add all anum entries
		List<AstNode> enumEntries = new ArrayList<AstNode>();
		for (Element member : ElementUtils.getAllFieldsIn((TypeElement) type)) {
			if (member.getKind() == ElementKind.ENUM_CONSTANT) {
				enumEntries.add(string(member.getSimpleName().toString()));
			}
		}

		FunctionCall enumConstructor = functionCall(name("stjs"), "enumeration", enumEntries);

		String typeName = context.getNames().getTypeName(context, type);
		if (typeName.contains(".")) {
			// inner class or namespace
			boolean innerClass = type.getEnclosingElement().getKind() != ElementKind.PACKAGE;
			String leftSide = innerClass ? replaceFullNameWithConstructor(typeName) : typeName;

			stmts.add(statement(JavaScriptNodes.assignment(null, leftSide, enumConstructor)));
		} else {
			// regular class
			stmts.add(JavaScriptNodes.variableDeclarationStatement(typeName, enumConstructor));
		}

		return true;
	}

	/**
	 * Special generation for classes marked with {@link GlobalScope}. The name of the class must appear nowhere.
	 */
	private boolean generateGlobal(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext context, List<AstNode> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		if (!JavaNodes.isGlobal(type)) {
			return false;
		}

		// print members
		for (Tree member : tree.getMembers()) {
			if (!JavaNodes.isConstructor(member) && !isAbstractInstanceMethod(member) && !(member instanceof BlockTree)) {
				List<AstNode> jsNodes = visitor.scan(member, context);
				stmts.addAll(jsNodes);
			}
		}

		addStaticInitializers(visitor, tree, context, stmts);
		addMainMethodCall(tree, stmts, context);

		return true;
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context) {
		List<AstNode> stmts = new ArrayList<AstNode>();
		if (generateGlobal(visitor, tree, context, stmts)) {
			// special construction for globals
			return stmts;
		}

		addNamespace(visitor, tree, context, stmts);

		Element type = TreeUtils.elementFromDeclaration(tree);
		String typeName = context.getNames().getTypeName(context, type);
		AstNode name = name(typeName);

		if (generareEnum(visitor, tree, context, stmts)) {
			// special construction for enums
			return stmts;
		}

		AstNode superClazz = getSuperClass(visitor, tree, context);
		AstNode interfaces = getInterfaces(visitor, tree, context);
		AstNode members = getMembers(visitor, tree, context);
		AstNode typeDesc = getTypeDescription(visitor, tree, context);
		boolean anonymousClass = tree.getSimpleName().length() == 0;

		if (anonymousClass) {
			// anonymous class
			name = getConstructor(visitor, tree, context);
		} else if (typeName.contains(".")) {
			// inner class or namespace
			boolean innerClass = type.getEnclosingElement().getKind() != ElementKind.PACKAGE;
			String leftSide = innerClass ? replaceFullNameWithConstructor(typeName) : typeName;

			stmts.add(statement(JavaScriptNodes.assignment(null, leftSide, getConstructor(visitor, tree, context))));
		} else {
			// regular class
			stmts.add(JavaScriptNodes.variableDeclarationStatement(typeName, getConstructor(visitor, tree, context)));
		}

		FunctionCall extendsCall = functionCall(name("stjs"), "extend", name, superClazz, interfaces, members, typeDesc);
		if (anonymousClass) {
			stmts.add(extendsCall);
		} else {
			stmts.add(JavaScriptNodes.statement(extendsCall));
		}
		addStaticInitializers(visitor, tree, context, stmts);
		addMainMethodCall(tree, stmts, context);

		return stmts;
	}

}
