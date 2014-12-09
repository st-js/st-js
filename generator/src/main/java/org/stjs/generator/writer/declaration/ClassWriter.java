package org.stjs.generator.writer.declaration;

import java.lang.annotation.RetentionPolicy;
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
import org.stjs.generator.javac.AnnotationHelper;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.javascript.NameValue;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.javascript.annotation.ServerSide;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

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
		if (namespace == null) {
			namespace = context.getConfiguration().getNamespace();
		}
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

		TreeWrapper<Tree, JS> superType = context.getCurrentWrapper().child(clazz.getExtendsClause());
		if (superType.isSyntheticType()) {
			return context.js().keyword(Keyword.NULL);
		}

		return context.js().name(superType.getTypeName());
	}

	/**
	 *
	 @return the list of implemented interfaces. for intefaces, the super class goes also in the interfaces list
	 */
	private JS getInterfaces(ClassTree clazz, GenerationContext<JS> context) {
		List<JS> ifaces = new ArrayList<JS>();
		for (Tree iface : clazz.getImplementsClause()) {
			TreeWrapper<Tree, JS> ifaceType = context.getCurrentWrapper().child(iface);
			if (!ifaceType.isSyntheticType()) {
				ifaces.add(context.js().name(ifaceType.getTypeName()));
			}
		}

		Element type = TreeUtils.elementFromDeclaration(clazz);
		if (clazz.getExtendsClause() != null && type.getKind() == ElementKind.INTERFACE) {
			TreeWrapper<Tree, JS> superType = context.getCurrentWrapper().child(clazz.getExtendsClause());
			if (!superType.isSyntheticType()) {
				ifaces.add(0, context.js().name(superType.getTypeName()));
			}
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
		JS target = context.getCurrentWrapper().isGlobal() ? null : context.js().name(context.getNames().getTypeName(context, type));

		JavaScriptBuilder<JS> js = context.js();
		JS condition = js.unary(UnaryOperator.LOGICAL_COMPLEMENT, js.property(js.name(GeneratorConstants.STJS), "mainCallDisabled"));
		JS thenPart = js.expressionStatement(js.functionCall(js.property(target, "main"), Collections.<JS> emptyList()));
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
			if (!skipTypeDescForField(member)) {
				props.add(NameValue.of(member.getSimpleName(), getFieldTypeDesc(memberType, context)));
			}
		}
		return context.js().object(props);
	}

	private boolean skipTypeDescForField(Element member) {
		if (((TypeElement) member.getEnclosingElement()).getQualifiedName().toString().startsWith("java.lang.")) {
			//maybe we should rather skip the bridge classes here
			return true;
		}
		if (member.getAnnotation(ServerSide.class) != null) {
			return true;
		}
		return false;
	}

	/**
	 * transform a.b.type in constructor.type
	 */
	private String replaceFullNameWithConstructor(String typeName) {
		int pos = typeName.lastIndexOf('.');
		return JavascriptKeywords.CONSTRUCTOR + typeName.substring(pos);
	}

	private JS writeAnnotationValue(WriterVisitor<JS> visitor, ExpressionTree expr, GenerationContext<JS> context) {
		if (expr instanceof NewArrayTree) {
			//special case for array initializer
			List<JS> items = new ArrayList<JS>();
			for (ExpressionTree item : ((NewArrayTree) expr).getInitializers()) {
				items.add(visitor.scan(item, context));
			}
			return context.js().array(items);
		}
		return visitor.scan(expr, context);
	}

	private void addAnnotationsForElement(String name, List<NameValue<JS>> props, WriterVisitor<JS> visitor,
			List<? extends AnnotationTree> annotations, GenerationContext<JS> context) {
		if (annotations.isEmpty()) {
			return;
		}
		List<NameValue<JS>> annotationsDesc = new ArrayList<NameValue<JS>>();
		for (AnnotationTree ann : annotations) {
			//build "annotationType": {arg1, arg2, ...}
			//XXX: should use here type names?
			if (AnnotationHelper.getRetentionType(ann.getAnnotationType()) == RetentionPolicy.SOURCE) {
				continue;
			}

			String annEntryKey = ann.getAnnotationType().toString();
			if (context.getConfiguration().getSkippedAnnotations().contains(annEntryKey)) {
				continue;
			}
			List<NameValue<JS>> annotationArgsDesc = new ArrayList<NameValue<JS>>();
			for (ExpressionTree arg : ann.getArguments()) {
				AssignmentTree assign = (AssignmentTree) arg;
				annotationArgsDesc.add(NameValue.of(assign.getVariable().toString(),
						writeAnnotationValue(visitor, assign.getExpression(), context)));
			}
			JS annotationArgs = context.js().object(annotationArgsDesc);

			//XXX: hack here to quote the type name - to change when using the type names
			annotationsDesc.add(NameValue.of("\"" + annEntryKey + "\"", annotationArgs));
		}
		if (!annotationsDesc.isEmpty()) {
			props.add(NameValue.of(name, context.js().object(annotationsDesc)));
		}
	}

	private void addAnnotationsForMethod(MethodTree method, List<NameValue<JS>> props, WriterVisitor<JS> visitor, GenerationContext<JS> context) {
		String name = method.getName().toString();
		if (name.equals("<init>")) {
			name = "_init_";
		}

		addAnnotationsForElement(name, props, visitor, method.getModifiers().getAnnotations(), context);
		for (int i = 0; i < method.getParameters().size(); ++i) {
			addAnnotationsForElement(method.getName().toString() + "$" + i, props, visitor, method.getParameters().get(i).getModifiers()
					.getAnnotations(), context);
		}
	}

	/**
	 * build the annotation description element
	 *
	 * <pre>
	 * $annotations : {
	 * _: {....}
	 * field1: {...}
	 * method1: {...}
	 * method1$0:  {...}
	 * method1$1:  {...}...
	 * }
	 * </pre>
	 *
	 * for each annotation list you have:
	 *
	 * <pre>
	 * {
	 * "annotationType1": [expr1, expr2, expr3],
	 * "annotationType2": []
	 * }
	 * </pre>
	 */
	private JS getAnnotationDescription(WriterVisitor<JS> visitor, ClassTree classTree, GenerationContext<JS> context) {
		List<NameValue<JS>> props = new ArrayList<NameValue<JS>>();
		addAnnotationsForElement("_", props, visitor, classTree.getModifiers().getAnnotations(), context);

		for (Tree member : classTree.getMembers()) {
			if (MemberWriters.shouldSkip(context.getCurrentWrapper().child(member))) {
				continue;
			}
			if (member instanceof VariableTree) {
				VariableTree field = (VariableTree) member;
				addAnnotationsForElement(field.getName().toString(), props, visitor, field.getModifiers().getAnnotations(), context);
			} else if (member instanceof MethodTree) {
				addAnnotationsForMethod((MethodTree) member, props, visitor, context);
			}
		}

		return context.js().object(props);
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
	 * Special generation for classes marked with {@link org.stjs.javascript.annotation.GlobalScope}. The name of the class must appear nowhere.
	 */
	private boolean generateGlobal(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		if (!context.getCurrentWrapper().isGlobal()) {
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
		JS annotationDesc = getAnnotationDescription(visitor, tree, context);
		boolean anonymousClass = tree.getSimpleName().length() == 0;

		if (anonymousClass) {
			// anonymous class
			name = getConstructor(visitor, tree, context);
		}
		addConstructorStatement(visitor, tree, context, stmts);

		@SuppressWarnings("unchecked")
		JS extendsCall =
				js.functionCall(js.property(js.name(GeneratorConstants.STJS), "extend"),
						Arrays.asList(name, superClazz, interfaces, members, typeDesc, annotationDesc));
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
