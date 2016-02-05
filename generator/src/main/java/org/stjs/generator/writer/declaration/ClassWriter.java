package org.stjs.generator.writer.declaration;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.JCTree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.AnnotationHelper;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.javascript.NameValue;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.utils.FieldUtils;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.utils.Scopes;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.javascript.annotation.Native;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.annotation.ServerSide;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ClassWriter<JS> extends AbstractMemberWriter<JS> implements WriterContributor<ClassTree, JS> {

	/**
	 * generate the namespace declaration stjs.ns("namespace") if needed
	 */
	private void addNamespace(ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		if (JavaNodes.isInnerType(type)) {
			// this is an inner (anonymous or not) class - no namespace declaration is generated
			return;
		}
		String namespace = context.getCurrentWrapper().getNamespace();
		if (!namespace.isEmpty()) {
			JavaScriptBuilder<JS> js = context.js();
			JS target = js.property(js.name(GeneratorConstants.STJS), "ns");
			stmts.add(js.expressionStatement(js.functionCall(target, Collections.singleton(js.string(namespace)))));
		}
	}

	/**
	 * @return the node to put in the super class. for interfaces, the super class goes also in the interfaces list
	 */
	private JS getSuperClass(ClassTree clazz, GenerationContext<JS> context) {
		if (clazz.getKind() == Tree.Kind.ENUM) {
			TypeElement enumClassElement = TreeUtils.elementFromDeclaration(clazz);

			TypeElement superclassType = ElementUtils.asTypeElement(context, enumClassElement.getSuperclass());

			String enumClassName = context.getNames().getTypeName(
					context,
					superclassType,
					DependencyType.EXTENDS);
			return context.js().name(enumClassName);
		}

		Element type = TreeUtils.elementFromDeclaration(clazz);

		if (clazz.getExtendsClause() == null || type.getKind() == ElementKind.INTERFACE) {
			// no super class found
			return context.js().code("stjs.Java.Object");
		}

		TreeWrapper<Tree, JS> superType = context.getCurrentWrapper().child(clazz.getExtendsClause());
		if (superType.isSyntheticType()) {
			return context.js().keyword(Keyword.NULL);
		}

		DependencyType depType = getDependencyTypeForClassDef(type);
		return context.js().name(superType.getTypeName(depType));
	}

	private DependencyType getDependencyTypeForClassDef(Element type) {
		if (JavaNodes.isInnerType(type)) {
			// this is an inner (anonymous or not) class -> STATIC dep type instead
			return DependencyType.STATIC;
		}
		return DependencyType.EXTENDS;
	}

	/**
	 * @return the list of implemented interfaces. for interfaces, the super class goes also in the interfaces list
	 */
	private JS getInterfaces(ClassTree clazz, GenerationContext<JS> context) {
		Element type = TreeUtils.elementFromDeclaration(clazz);
		DependencyType depType = getDependencyTypeForClassDef(type);

		List<JS> ifaces = new ArrayList<JS>();
		for (Tree iface : clazz.getImplementsClause()) {
			TreeWrapper<Tree, JS> ifaceType = context.getCurrentWrapper().child(iface);
			if (!ifaceType.isSyntheticType()) {
				ifaces.add(context.js().name(ifaceType.getTypeName(depType)));
			}
		}

		if (clazz.getExtendsClause() != null && type.getKind() == ElementKind.INTERFACE) {
			TreeWrapper<Tree, JS> superType = context.getCurrentWrapper().child(clazz.getExtendsClause());
			if (!superType.isSyntheticType()) {
				ifaces.add(0, context.js().name(superType.getTypeName(DependencyType.EXTENDS)));
			}
		}
		return context.js().array(ifaces);
	}

	/**
	 * @return the JavaScript node for the class' constructor
	 */
	private JS getAllocFunction(WriterVisitor<JS> visitor, ClassTree clazz, GenerationContext<JS> context, String constructorFunctionName) {
		TypeElement typeElement = TreeUtils.elementFromDeclaration(clazz);

		List<String> outerClassParameterNames = Scopes.buildOuterClassConstructorParametersNames(typeElement);
		List<JS> outerClassParametersDeclaration = new ArrayList<>();
		List<JS> outerClassVariableAssignment = new ArrayList<>();
		for (String outerClassParameterName : outerClassParameterNames) {
			outerClassParametersDeclaration.add(context.js().name(outerClassParameterName));

			// this._outerClass$0 = outerClass$0;
			JS variableAssigment =
					context.js().expressionStatement(
							context.js().assignment(
									AssignOperator.ASSIGN,
									context.js().property(
											context.js().name(GeneratorConstants.THIS),
											GeneratorConstants.NON_PUBLIC_METHODS_AND_FIELDS_PREFIX + outerClassParameterName),
									context.js().name(outerClassParameterName)));
			outerClassVariableAssignment.add(variableAssigment);
		}

		JS constructorBody = context.js().block(outerClassVariableAssignment);

		addCallToSuperFieldInitializer(context, constructorBody, typeElement);
		for (JS js : getFieldInitializers(visitor, clazz, context)) {
			context.js().addStatement(constructorBody, js);
		}

		return context.js().function(
				constructorFunctionName,
				outerClassParametersDeclaration,
				constructorBody);
	}

	private void addCallToSuperFieldInitializer(GenerationContext<JS> context, JS constructorBody, TypeElement typeElement) {
		TypeMirror superclass = typeElement.getSuperclass();

		if (isCallToSuperTypeConstructorRequired(superclass)) {
			JavaScriptBuilder<JS> js = context.js();
			String superClassName = context.getNames().getTypeName(context, superclass, DependencyType.EXTENDS);

            List<JS> parameters = new ArrayList<>();
            parameters.add(js.name(GeneratorConstants.THIS));

            parameters.addAll(Scopes.buildOuterClassParametersAsNames(context, typeElement, context.getTypes().asElement(superclass)));

            js.addStatement(
                    constructorBody,
                    js.expressionStatement(
                            js.functionCall(
                                    js.property(js.name(superClassName), "call"),
                                    parameters)));
        }
	}

    private boolean isCallToSuperTypeConstructorRequired(TypeMirror superclass) {
        return superclass.getKind() != TypeKind.NONE &&
                !JavaNodes.sameRawType(superclass, Object.class) &&
                !JavaNodes.sameRawType(superclass, Enum.class);
    }

	@SuppressWarnings("unchecked")
	private <T extends Tree> List<T> getFilteredMembers(ClassTree clazz, MemberFilter<T> memberFilter) {
		List<T> selectedMembers = new ArrayList<>();
		for (Tree member : clazz.getMembers()) {
			if (memberFilter.passesFilter(member)) {
				selectedMembers.add((T) member);
			}
		}
		return selectedMembers;
	}

	private void generateEnumValuesProperty(GenerationContext<JS> context, List<JS> stmts, List<JS> enumValues) {
		if (!enumValues.isEmpty()) {
			JS enumValuesProperty = context.js().property(context.js().name(JavascriptKeywords.CONSTRUCTOR), GeneratorConstants.ENUM_VALUES_PROPERTY);
			stmts.add(context.js().expressionStatement(
					context.js().assignment(AssignOperator.ASSIGN, enumValuesProperty, context.js().array(enumValues))));
		}
	}

	private boolean isBodyContainingReturnStatement(MethodTree constructor) {
		final boolean[] returnStatementFound = new boolean[1];

		for (StatementTree statementTree : constructor.getBody().getStatements()) {
			statementTree.accept(
					new TreeScanner<Object, Object>() {
						@Override
						public Object visitReturn(ReturnTree node, Object o) {
							returnStatementFound[0] = true;
							return null;
						}
					},
					null);

			if (returnStatementFound[0]) {
				return true;
			}
		}

		return false;
	}

	private boolean isDefaultConstructorDelegatinOnlyToSuperDefaultConstructor(MethodTree constructor) {
		if (constructor.getParameters().size() != 0) {
			return false;
		}

		BlockTree body = constructor.getBody();
		if (body.getStatements().size() == 1) {
			StatementTree statementTree = body.getStatements().get(0);
			if (statementTree instanceof JCTree.JCExpressionStatement) {
				JCTree.JCExpressionStatement expressionStatement = (JCTree.JCExpressionStatement) statementTree;

				JCTree.JCExpression expression = expressionStatement.getExpression();
				ExecutableElement element = ElementUtils.asExecutableElement(TreeUtils.elementFromUse(expression));

				if (element != null && ElementUtils.isConstructor(element) && element.getParameters().size() == 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Add a "return this;" statement to allow method chaining during new block construction.
	 *
	 * 		var object = new Object()._constructor$String("test");
 	 */
	private void addReturnThisStatementForChaining(GenerationContext<JS> context, JS block) {
		JS returnThis = context.js().returnStatement(context.js().keyword(Keyword.THIS));
		context.js().addStatement(block, returnThis);
	}

	/**
	 *  Add JS corresponding to the contract of a Java Enum (name() and ordinal()):
	 *
	 *  constructor.FIRST._name = "FIRST";
	 *  constructor.FIRST._ordinal = 1;
	 *  constructor._values = [constructor.FIRST];
	 */
	private void generateEnumSignatureForMember(GenerationContext<JS> context, List<JS> stmts, List<JS> enumValues, int index, Tree member) {
		assert member instanceof JCTree.JCVariableDecl;
		String enumEntryName = ((JCTree.JCVariableDecl) member).getName().toString();

		JS enumConstructorProperty = context.js().property(context.js().name(JavascriptKeywords.CONSTRUCTOR), enumEntryName);
		JS enumNameProperty = context.js().property(enumConstructorProperty, GeneratorConstants.ENUM_NAME_PROPERTY);
		JS enumOrdinalProperty = context.js().property(enumConstructorProperty, GeneratorConstants.ENUM_ORDINAL_PROPERTY);

		stmts.add(context.js().expressionStatement(
				context.js().assignment(AssignOperator.ASSIGN, enumNameProperty, context.js().string(enumEntryName))));
		stmts.add(context.js().expressionStatement(
				context.js().assignment(AssignOperator.ASSIGN, enumOrdinalProperty, context.js().number(index))));

		enumValues.add(enumConstructorProperty);
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
		JS thenPart = js.expressionStatement(js.functionCall(js.property(target, "main"), Collections.<JS> emptyList()));
		stmts.add(js.ifStatement(condition, thenPart, null));
	}

	@SuppressWarnings("unchecked")
	private JS getFieldTypeDesc(TypeMirror type, GenerationContext<JS> context) {
		JavaScriptBuilder<JS> js = context.js();
		if (JavaNodes.isJavaScriptPrimitive(type)) {
			return js.keyword(Keyword.NULL);
		}
		JS typeName = js.string(context.getNames().getTypeName(context, type, DependencyType.OTHER));

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
		if (context.getConfiguration().skipTypeDescriptionGeneration()) {
			return context.js().block(Collections.<JS>emptyList());
		}

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
			// maybe we should rather skip the bridge classes here
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
			// special case for array initializer
			List<JS> items = new ArrayList<JS>();
			for (ExpressionTree item : ((NewArrayTree) expr).getInitializers()) {
				items.add(visitor.scan(item, context));
			}
			return context.js().array(items);
		}
		return visitor.scan(expr, context);
	}

	private JS getAnnotationForElement(AnnotationTree ann, WriterVisitor<JS> visitor, GenerationContext<JS> context) {
		// build "annotationType": {arg1, arg2, ...}
		// XXX: should use here type names?
		if (AnnotationHelper.getRetentionType(ann.getAnnotationType()) == RetentionPolicy.SOURCE) {
			return null;
		}
		String annEntryKey = ann.getAnnotationType().toString();
		if (!context.getConfiguration().getAnnotations().contains(annEntryKey)) {
			return null;
		}

		List<NameValue<JS>> annotationArgsDesc = new ArrayList<NameValue<JS>>();
		for (ExpressionTree arg : ann.getArguments()) {
			AssignmentTree assign = (AssignmentTree) arg;
			annotationArgsDesc
					.add(NameValue.of(assign.getVariable().toString(), writeAnnotationValue(visitor, assign.getExpression(), context)));
		}
		return context.js().object(annotationArgsDesc);
	}

	private void addAnnotationsForElement(String name, List<NameValue<JS>> props, WriterVisitor<JS> visitor,
			List<? extends AnnotationTree> annotations, GenerationContext<JS> context) {
		if (annotations.isEmpty()) {
			return;
		}
		List<NameValue<JS>> annotationsDesc = new ArrayList<NameValue<JS>>();
		for (AnnotationTree ann : annotations) {
			JS annotationArgs = getAnnotationForElement(ann, visitor, context);
			if (annotationArgs != null) {
				// XXX: hack here to quote the type name - to change when using the type names
				String annEntryKey = ann.getAnnotationType().toString();
				annotationsDesc.add(NameValue.of("\"" + annEntryKey + "\"", annotationArgs));
			}
		}
		if (!annotationsDesc.isEmpty()) {
			props.add(NameValue.of(name, context.js().object(annotationsDesc)));
		}
	}

	private void addAnnotationsForMethod(MethodTree method, List<NameValue<JS>> props, WriterVisitor<JS> visitor, GenerationContext<JS> context) {
		String name = method.getName().toString();
		if ("<init>".equals(name)) {
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

	/**
	 * Special generation for classes marked with {@link org.stjs.javascript.annotation.GlobalScope}. The name of the
	 * class must appear nowhere.
	 */
	private boolean generateGlobal(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		if (!context.getCurrentWrapper().isGlobal()) {
			return false;
		}

		// print members
		stmts.addAll(
				getMembers(context, visitor, tree, allMembersExceptConstructorsFilter()));

		addStaticInitializers(visitor, tree, context, stmts);
		addMainMethodCall(tree, stmts, context);

		return true;
	}

	private JS addVariableDeclarationForClassAllocator(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts, JS constructorFunction) {
		JavaScriptBuilder<JS> js = context.js();
		Element type = TreeUtils.elementFromDeclaration(tree);
		String typeName = context.getNames().getTypeName(context, type, DependencyType.EXTENDS);
		JS variableName;

		if (typeName.contains(".")) {
			// inner class or namespace
			// generate [ns.]typeName = function() {...}
			variableName = getClassName(tree, context);
			stmts.add(js.expressionStatement(js.assignment(AssignOperator.ASSIGN,variableName, constructorFunction)));
		} else {
			// regular class
			// generate var typeName = function() {...}
			variableName = js.name(typeName);
			stmts.add(js.variableDeclaration(true, typeName, constructorFunction));
		}

		return variableName;
	}

	private String getClassNameAsString(ClassTree tree, GenerationContext<JS> context) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		String typeName = context.getNames().getTypeName(context, type, DependencyType.EXTENDS);

		return typeName;
	}

	public JS getClassName(ClassTree tree, GenerationContext<JS> context) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		String typeName = context.getNames().getTypeName(context, type, DependencyType.EXTENDS);

		if (typeName.contains(".") && type.getEnclosingElement().getKind() != ElementKind.PACKAGE) {
			return context.js().name(replaceFullNameWithConstructor(typeName));
		}

		return context.js().name(typeName);
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

		addNamespace(tree, context, stmts);

		String classNameAsString = getClassNameAsString(tree, context);
		JS className = js.string(classNameAsString);
		JS superClazz = getSuperClass(tree, context);
		JS interfaces = getInterfaces(tree, context);

		JS classBody = getClassBody(context, visitor, tree);

		JS typeDesc = getTypeDescription(visitor, tree, context);

		JS annotationDesc = getAnnotationDescription(visitor, tree, context);
		boolean isAnonymousClass = tree.getSimpleName().length() == 0;

		String constructorFunctionName = "";
		if (isAnonymousClass) {
			String[] classNameParts = classNameAsString.split("\\.");
			constructorFunctionName = classNameParts[classNameParts.length - 1];
		}

		JS allocFunction = getAllocFunction(visitor, tree, context, constructorFunctionName);

		if (!isAnonymousClass) {
			allocFunction = addVariableDeclarationForClassAllocator(visitor, tree, context, stmts, allocFunction);
		}

		@SuppressWarnings("unchecked")
		JS extendsCall = js.functionCall(js.property(js.name(GeneratorConstants.STJS), "extend"),
				Arrays.asList(allocFunction, superClazz, interfaces, classBody, typeDesc, annotationDesc, className));

		if (!isAnonymousClass) {
			extendsCall = js.expressionStatement(js.assignment(AssignOperator.ASSIGN, allocFunction, extendsCall));
		}
		stmts.add(context.withPosition(tree, extendsCall));

		addStaticInitializers(visitor, tree, context, stmts);
		addMainMethodCall(tree, stmts, context);

		return js.statements(stmts);
	}

	private JS getClassBody(GenerationContext<JS> context, WriterVisitor<JS> visitor, ClassTree classTree) {

		List<JS> stmts = new ArrayList<>();

		// initializers like "_constructor(...)"
		stmts.addAll(
				getConstructorsAsInitializerFunctions(context, visitor, classTree));

		// non-static/non-constructor members
		stmts.addAll(
				getMembers(context, visitor, classTree, nonStaticFieldsAndMethodsFilter()));

		// static methods/enum/inner classes
		stmts.addAll(
				getMembers(context, visitor, classTree, staticNonFieldsFilter()));

		// initializer block
		stmts.addAll(
				getMembers(context, visitor, classTree, staticInitializerFilter()));

		// enum entries
		stmts.addAll(
				getEnumEntries(context, visitor, classTree, enumEntryFilter()));

		// static fields members
		stmts.addAll(
				getMembers(context, visitor, classTree, staticFieldsFilter()));

		// create function:
		//     function(constructor, prototype) {...}
		List<JS> params = Arrays.asList(context.js().name(JavascriptKeywords.CONSTRUCTOR), context.js().name(JavascriptKeywords.PROTOTYPE));
		return context.js().function(null, params, context.js().block(stmts));
	}

	private Collection<? extends JS> getEnumEntries(GenerationContext<JS> context, WriterVisitor<JS> visitor, ClassTree classTree, MemberFilter<? extends Tree> memberFilter) {
		List<JS> stmts = new ArrayList<>();

		List<? extends Tree> enumEntries = getFilteredMembers(classTree, memberFilter);

		List<JS> enumValues = new ArrayList<>();
		for (int i = 0; i < enumEntries.size(); i++) {
			Tree member = enumEntries.get(i);
			JS newStatement = visitor.scan(member, context);

 			stmts.add(newStatement);
			generateEnumSignatureForMember(context, stmts, enumValues, i, member);
		}

		generateEnumValuesProperty(context, stmts, enumValues);

		return stmts;
	}

	private MemberFilter<? extends Tree> enumEntryFilter() {
		return new MemberFilter<Tree>() {

			@Override
			public boolean passesFilter(Tree tree) {
				Element symbol = InternalUtils.symbol(tree);

				if (symbol == null) {
					return false;
				}

				return symbol.getKind() == ElementKind.ENUM_CONSTANT && ElementUtils.isStatic(symbol);
			}
		};
	}

	private MemberFilter<? extends Tree> staticFieldsFilter() {
		return new MemberFilter<Tree>() {

			@Override
			public boolean passesFilter(Tree tree) {
				Element symbol = InternalUtils.symbol(tree);

				if (symbol == null) {
					return false;
				}

				return symbol.getKind() == ElementKind.FIELD && ElementUtils.isStatic(symbol);
			}
		};
	}

	private MemberFilter<? extends Tree> staticNonFieldsFilter() {
		return new MemberFilter<Tree>() {

			@Override
			public boolean passesFilter(Tree tree) {
				Element symbol = InternalUtils.symbol(tree);

				if (symbol == null) {
					return false;
				}

				return ((symbol.getKind() == ElementKind.METHOD) && ElementUtils.isStatic(symbol)) ||
						(symbol.getKind() == ElementKind.ENUM) || (symbol.getKind() == ElementKind.CLASS) || (symbol.getKind() == ElementKind.INTERFACE);
			}
		};
	}

	private MemberFilter<? extends Tree> staticInitializerFilter() {
		return new MemberFilter<Tree>() {

			@Override
			public boolean passesFilter(Tree tree) {
				return ((tree instanceof BlockTree) && ((BlockTree) tree).isStatic());
			}
		};
	}

	private MemberFilter<Tree> nonStaticFieldsAndMethodsFilter() {
		return new MemberFilter<Tree>() {
			@Override
			public boolean passesFilter(Tree member) {
				return (!JavaNodes.isConstructor(member) && !(member instanceof BlockTree) && !ElementUtils.isStatic(InternalUtils.symbol(member)));
			}
		};
	}

	private List<JS> getConstructorsAsInitializerFunctions(GenerationContext<JS> context, WriterVisitor<JS> visitor, ClassTree classTree) {
		JavaScriptBuilder<JS> js = context.js();
		List<JS> stmts = new ArrayList<>();

		List<MethodTree> constructors = getFilteredMembers(classTree, constructorFilter());

		for (MethodTree constructorMethodTree : constructors) {
			ExecutableElement constructorElement = TreeUtils.elementFromDeclaration(constructorMethodTree);
			String constructorName = InternalUtils.generateOverloadeConstructorName(context, constructorElement.getParameters());

			JS block = visitor.scan(constructorMethodTree.getBody(), context);

			if (isBodyContainingReturnStatement(constructorMethodTree)) {
				// Wrap current body inside a function because the body may contain an early return
				// which would cause a failure in constructor chaining. The last line of
				// the constructor must be returnng a reference to "this".
				// Example:
				//     _constructor = function() {
				//         // ------------------------------------------
				//         // here is the wrapping inside a function -->
				//         // ------------------------------------------
				//         (function() {
				//             // ...original body here
				//             if (any_boolean_value) {
				//                 return;
				//             }
				//             this.any_flag = true; // this line won't be executed
				//         }).call();
				//         // ------------------------------------------
				//         // <-- here is the wrapping inside a function
				//         // ------------------------------------------
				//
				//         return this; // still returning `this` for constructor chaining
				//    }
				//
				block = js.block(
						Collections.singletonList(
								js.expressionStatement(
										js.functionCall(
												js.paren(
														js.property(
																js.function(null, Collections.<JS>emptyList(), block),
																"call")
												),
												Collections.singletonList(js.name(GeneratorConstants.THIS))
										)
								)
						)
				);
			}

			List<JS> params = MethodWriter.getParams(constructorMethodTree.getParameters(), context);
			JS member = js.property(js.name(JavascriptKeywords.PROTOTYPE), constructorName);
			JS declaration = js.function(null, params, block);
			addReturnThisStatementForChaining(context, block);

			stmts.add(js.expressionStatement(js.assignment(AssignOperator.ASSIGN, member, declaration)));
		}

		return stmts;
	}

	private  List<JS> getMembers(GenerationContext<JS> context, WriterVisitor<JS> visitor, ClassTree classTree, MemberFilter<? extends Tree> memberFilter) {
		List<JS> stmts = new ArrayList<>();

		for (Tree member : getFilteredMembers(classTree, memberFilter)) {
			stmts.add(visitor.scan(member, context));
		}

		return stmts;
	}

	@SuppressWarnings("unchecked")
	private MemberFilter constructorFilter() {
		return new MemberFilter() {
			@Override
			public boolean passesFilter(Tree tree) {
				if (!JavaNodes.isConstructor(tree)) {
					return false;
				}

				assert tree instanceof MethodTree;
				MethodTree methodTree = (MethodTree) tree;
				if (isDefaultConstructorDelegatinOnlyToSuperDefaultConstructor(methodTree)) {
					return false;
				}

				ExecutableElement constructor = TreeUtils.elementFromDeclaration(methodTree);
				return (constructor.getAnnotation(ServerSide.class) == null && constructor.getAnnotation(Native.class) == null);
			}
		};
	}

	private MemberFilter<? extends Tree> allMembersExceptConstructorsFilter() {
		return new MemberFilter<Tree>() {
			@Override
			public boolean passesFilter(Tree member) {
				return (!JavaNodes.isConstructor(member) && !(member instanceof BlockTree));
			}
		};
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

	private List<JS> getFieldInitializers(WriterVisitor<JS> visitor, ClassTree classElement, GenerationContext<JS> context) {
		List<JS> expressions = new ArrayList<>();

		for (Tree tree : classElement.getMembers()) {
			if (tree.getKind() == Tree.Kind.VARIABLE) {
				TreeWrapper<VariableTree, JS> variableTreeWrapper = context.wrap(TreeUtils.elementFromDeclaration((VariableTree) tree));
				if (isFieldInitializerRequired(variableTreeWrapper)) {
					expressions.add(context.js().expressionStatement(
							context.js().assignment(AssignOperator.ASSIGN,
									context.js().property(
											context.js().name(JavascriptKeywords.THIS),
											FieldUtils.getFieldName(variableTreeWrapper.getTree())
									),
									visitor.scan(
											variableTreeWrapper.getTree().getInitializer(),
											context)
							)
					));
				}
			}
		}

		return expressions;
	}

	private boolean isFieldInitializerRequired(TreeWrapper<VariableTree, JS> variableTreeWrapper) {
		if (MemberWriters.shouldSkip(variableTreeWrapper)) {
			return false;
		}

		if (variableTreeWrapper.isStatic()) {
			return false;
		}

		if (variableTreeWrapper.getTree().getInitializer() == null) {
			return false;
		}

		return true;
	}

	private interface MemberFilter<T> {
		boolean passesFilter(Tree tree);
	}
}
