package org.stjs.generator.scope.simple;

import static org.stjs.generator.ASTNodeData.parent;
import static org.stjs.generator.ASTNodeData.resolvedMethod;
import static org.stjs.generator.ASTNodeData.resolvedType;
import static org.stjs.generator.ASTNodeData.resolvedVariable;
import static org.stjs.generator.ASTNodeData.scope;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BinaryExpr.Operator;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;

import java.lang.reflect.GenericArrayType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.stjs.generator.ASTNodeData;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.handlers.ForEachNodeVisitor;
import org.stjs.generator.scope.classloader.ClassLoaderWrapper;
import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.generator.scope.classloader.FieldWrapper;
import org.stjs.generator.scope.classloader.MethodWrapper;
import org.stjs.generator.scope.classloader.TypeWrapper;
import org.stjs.generator.scope.classloader.TypeWrappers;
import org.stjs.generator.utils.ClassUtils;
import org.stjs.generator.utils.Operators;
import org.stjs.generator.utils.Option;
import org.stjs.generator.utils.PreConditions;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;

public class SimpleScopeBuilder extends ForEachNodeVisitor<Scope> {
	private final ClassLoaderWrapper classLoader;
	private final GenerationContext context;

	private final Map<ClassScope, AtomicInteger> anonymousClassCount = new MapMaker()
			.makeComputingMap(new Function<ClassScope, AtomicInteger>() {

				@Override
				public AtomicInteger apply(ClassScope input) {
					return new AtomicInteger(0);
				}
			});

	public SimpleScopeBuilder(ClassLoaderWrapper classLoader, GenerationContext context) {
		this.classLoader = classLoader;
		this.context = context;
	}

	@Override
	protected void before(Node node, Scope arg) {
		ASTNodeData data = (ASTNodeData) node.getData();
		if (data.getScope() == null) {
			data.setScope(arg);
		}
		/*
		 * if (data.getExpressionType() == null && node instanceof Expression) { throw new
		 * IllegalStateException("The node:" + node + " is an expression but no type was set"); }
		 */
	}

	@Override
	public void visit(final PackageDeclaration n, Scope scope) {
		((CompilationUnitScope) scope).setPackageName(n.getName().toString());
		super.visit(n, scope);
	}

	@Override
	public void visit(final CompilationUnit n, Scope inputScope) {
		PreConditions.checkState(inputScope instanceof CompilationUnitScope,
				"A compilationUnitScope was expected. Got %s", inputScope);

		CompilationUnitScope scope = (CompilationUnitScope) inputScope;
		// asterisk declaration have lower priority => process them first (JLS §7.5.2)
		if (n.getImports() != null) {
			for (ImportDeclaration importDecl : n.getImports()) {
				NameExpr name = importDecl.getName();
				checkImport(importDecl, name.toString());
				if (importDecl.isAsterisk()) {
					if (importDecl.isStatic()) {
						QualifiedNameExpr expr = (QualifiedNameExpr) name;
						for (ClassWrapper clazz : identifyQualifiedNameExprClass(expr.getQualifier())) {
							for (FieldWrapper field : clazz.getDeclaredNonPrivateStaticFields()) {
								scope.addField(field);
							}
							for (MethodWrapper method : clazz.getDeclaredNonPrivateStaticMethods()) {
								scope.addMethod(method);
							}
							for (ClassWrapper type : clazz.getDeclaredNonPrivateStaticClasses()) {
								scope.addType(type);
							}
						}
					} else {
						scope.addTypeImportOnDemand(name);
					}
				}
			}
			for (ImportDeclaration importDecl : n.getImports()) {
				NameExpr name = importDecl.getName();
				checkImport(importDecl, name.toString());
				if (!importDecl.isAsterisk()) {
					if (importDecl.isStatic()) {
						QualifiedNameExpr expr = (QualifiedNameExpr) name;
						for (ClassWrapper clazz : identifyQualifiedNameExprClass(expr.getQualifier())) {
							String fieldOrTypeOrMethodName = name.getName();
							for (FieldWrapper field : clazz.findField(fieldOrTypeOrMethodName)) {
								scope.addField(field);
							}
							List<MethodWrapper> methods = clazz.findMethods(fieldOrTypeOrMethodName);
							if (!methods.isEmpty()) {
								scope.addMethods(fieldOrTypeOrMethodName, methods);
							}
							for (ClassWrapper innerClass : clazz.getDeclaredClass(fieldOrTypeOrMethodName)) {
								scope.addType(innerClass);
								break;
							}
							// TODO : do wee need to continue here?
						}
					}
					for (ClassWrapper clazz : identifyQualifiedNameExprClass(name)) {
						scope.addType(clazz);

					}
				}
			}
		}

		super.visit(n, scope);
	}

	private ClassScope addClassToScope(CompilationUnitScope compilationUnitScope, ClassWrapper clazz) {
		compilationUnitScope.addType(clazz);

		for (ClassWrapper innerClass : clazz.getDeclaredClasses()) {
			compilationUnitScope.addType(innerClass);
		}

		ClassScope scope = new ClassScope(clazz, compilationUnitScope, context);

		return scope;
	}

	@Override
	public void visit(final ClassOrInterfaceDeclaration n, Scope scope) {
		Scope classScope = scope.apply(new DefaultScopeVisitor<Scope>() {
			@Override
			public Scope apply(CompilationUnitScope compilationUnitScope) {
				// TODO : this is not good enough for inner classes (which need the list of outer classes in their
				// qualified name)

				String qualifiedName = compilationUnitScope.getPackageName().toString() + "." + n.getName();
				ClassWrapper clazz = classLoader.loadClassOrInnerClass(qualifiedName).getOrThrow(
						"Cannot load class or interface " + qualifiedName);
				return addClassToScope(compilationUnitScope, clazz);
			}

			@Override
			public Scope apply(ClassScope classScope) {
				// NOTE : static class must inherit the compilation unit scope, non static class the class scope
				// throw new RuntimeException("Inner class not implemented yet");
				return null;
			}
		});
		super.visit(n, classScope != null ? classScope : scope);
	}

	@Override
	public void visit(final MethodDeclaration n, Scope currentScope) {
		// the scope is where the method is defined, i.e. the classScope
		((ASTNodeData) n.getData()).setScope(currentScope);
		BasicScope scope = handleMethodDeclaration(n.getParameters(), currentScope);
		super.visit(n, new BasicScope(scope, context));
	}

	private BasicScope handleMethodDeclaration(final List<Parameter> parameters, Scope currentScope) {

		BasicScope scope = new BasicScope(currentScope, context);
		if (parameters != null) {
			for (Parameter p : parameters) {
				TypeWrapper clazz = resolveType(scope, p.getType());
				scope.addVariable(new ParameterVariable(clazz, p.getId().getName()));
			}
		}
		// if (n.getTypeParameters() != null) {
		// for (TypeParameter p : n.getTypeParameters()) {
		// // TODO : Generics
		// }
		// }
		return scope;
	}

	private TypeWrapper resolveType(Scope scope, Type type) {
		// TODO : shouldn't that go directly in the scope classes?
		int arrayCount = 0;
		if (type instanceof ReferenceType) {
			ReferenceType refType = (ReferenceType) type;
			arrayCount = refType.getArrayCount();
			type = refType.getType(); // type is a primitive or class
		}
		TypeWrapper resolvedType;
		if (type instanceof PrimitiveType) {
			PrimitiveType primitiveType = (PrimitiveType) type;
			resolvedType = PrimitiveTypes.primitiveReflectionType(primitiveType);
		} else if (type instanceof VoidType) {
			resolvedType = new ClassWrapper(void.class);
		} else if (type instanceof ClassOrInterfaceType) {
			resolvedType = scope.resolveType(type.toString()).getType();
		} else if (type instanceof WildcardType) {
			throw new RuntimeException("Generics not yet implemented");
		} else {
			throw new RuntimeException("Unexpected type " + type);
		}
		if (arrayCount == 0) {
			return resolvedType;
		}
		if (resolvedType instanceof ClassWrapper) {
			return ClassUtils.arrayOf((ClassWrapper) resolvedType, arrayCount);
		}
		throw new RuntimeException("Array of generics type is not yet implemented");
	}

	@Override
	public void visit(VariableDeclarationExpr n, Scope scope) {
		// TODO add resolvedType
		PreConditions
				.checkState(scope instanceof BasicScope, "The variable [%s] is not defined inside a BasicScope", n);

		BasicScope basicScope = (BasicScope) scope;
		if (n.getVars() != null) {
			TypeWrapper clazz = resolveType(basicScope, n.getType());
			/*
			 * TODO : this is not as simple. the order of the variables declarations matters! In this example class XXX
			 * { private String x; void m() { String y = x; String x = "hello"; String k = x; } }
			 * 
			 * y = x refers to the field but k = x refers to the local var. if you replace String x = "hello", by String
			 * x = x; this causes a compilation error, because it is equivalent to; String x; x=x; This means that
			 * 'String x;' create a new scope in which ' = x' is evaluated.
			 */
			for (VariableDeclarator var : n.getVars()) {
				basicScope.addVariable(new LocalVariable(clazz, var.getId().getName()));
			}
		}

		super.visit(n, scope);
	}

	@Override
	public void visit(CatchClause n, Scope currentScope) {
		// TODO : this is broken in Java7 because a catch block might declare more than one exception
		// would need a new javap
		BasicScope scope = new BasicScope(currentScope, context);
		scope.addVariable(new ParameterVariable(resolveType(scope, n.getExcept().getType()), n.getExcept().getId()
				.getName()));
		super.visit(n, scope);
	}

	@Override
	public void visit(ConstructorDeclaration n, Scope currentScope) {
		// the scope is where the constructor is defined, i.e. the classScope
		((ASTNodeData) n.getData()).setScope(currentScope);
		BasicScope scope = handleMethodDeclaration(n.getParameters(), currentScope);
		super.visit(n, new BasicScope(scope, context));
	}

	@Override
	public void visit(ForeachStmt n, Scope currentScope) {
		super.visit(n, new BasicScope(currentScope, context));
	}

	@Override
	public void visit(ForStmt n, Scope currentScope) {
		super.visit(n, new BasicScope(currentScope, context));
	}

	@Override
	public void visit(InitializerDeclaration n, Scope currentScope) {
		super.visit(n, new BasicScope(currentScope, context));
	}

	@Override
	public void visit(final EnumDeclaration n, final Scope currentScope) {
		ClassWrapper enumClass = currentScope.apply(new DefaultScopeVisitor<ClassWrapper>() {
			@Override
			public ClassWrapper apply(CompilationUnitScope scope) {
				return classLoader.loadClass(scope.getPackageName() + "." + n.getName()).getOrThrow();
			}

			@Override
			public ClassWrapper apply(ClassScope scope) {
				ClassWrapper parentClass = scope.getClazz();
				return classLoader.loadClass(parentClass.getName() + "$" + n.getName()).getOrThrow();
			}
		});
		ClassScope enumClassScope = new ClassScope(enumClass, currentScope, context);

		super.visit(n, enumClassScope);

	}

	@Override
	public void visit(ObjectCreationExpr n, Scope scope) {
		if (n.getScope() != null) {
			n.getScope().accept(this, scope);
		}
		if (n.getTypeArgs() != null) {
			for (Type t : n.getTypeArgs()) {
				t.accept(this, scope);
			}
		}
		n.getType().accept(this, scope);
		if (n.getArgs() != null) {
			for (Expression e : n.getArgs()) {
				e.accept(this, scope);
			}
		}
		if (n.getAnonymousClassBody() != null) {
			ClassScope classScope = scope.closest(ClassScope.class);
			int anonymousClassNumber = anonymousClassCount.get(classScope).incrementAndGet();
			ClassWrapper anonymousClass = classLoader.loadClass(
					classScope.getClazz().getName() + "$" + anonymousClassNumber).getOrThrow();
			ClassScope anonymousClassScope = new ClassScope(anonymousClass, scope, context);

			for (BodyDeclaration member : n.getAnonymousClassBody()) {
				member.accept(this, anonymousClassScope);
			}
		}
		// TODO add potential generic params
		resolvedType(n, resolveType(scope, n.getType()));
		scope(n, scope);
	}

	private Option<ClassWrapper> identifyQualifiedNameExprClass(NameExpr expr) {
		return classLoader.loadClassOrInnerClass(expr.toString());
	}

	private void checkImport(ImportDeclaration importDecl, String string) {
		// TODO Auto-generated method stub

	}

	/**
	 * type references -> set the resolved type
	 */
	@Override
	public void visit(ClassOrInterfaceType n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, resolveType(arg, n));
	}

	@Override
	public void visit(PrimitiveType n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, resolveType(arg, n));
	}

	@Override
	public void visit(ReferenceType n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, resolveType(arg, n));
	}

	@Override
	public void visit(VoidType n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, resolveType(arg, n));
	}

	@Override
	public void visit(WildcardType n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, resolveType(arg, n));
	}

	/*** expressions -> set the type ***/

	@Override
	public void visit(ArrayAccessExpr n, Scope arg) {
		super.visit(n, arg);
		TypeWrapper arrayType = resolvedType(n.getName());
		if (arrayType.getType() instanceof GenericArrayType) {
			resolvedType(n, TypeWrappers.wrap(((GenericArrayType) arrayType.getType()).getGenericComponentType()));
		} else if (arrayType.getType() instanceof Class<?>) {
			resolvedType(n, TypeWrappers.wrap(((Class<?>) arrayType.getType()).getComponentType()));
		} else {
			throw new IllegalStateException("Unknown reflect type for node:" + n);
		}
	}

	@Override
	public void visit(ArrayCreationExpr n, Scope arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	@Override
	public void visit(ArrayInitializerExpr n, Scope arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}

	@Override
	public void visit(AssignExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, resolvedType(n.getTarget()));
	}

	@Override
	public void visit(BinaryExpr n, Scope arg) {
		super.visit(n, arg);
		if (Operators.isLogical(n.getOperator())) {
			resolvedType(n, TypeWrappers.wrap(boolean.class));
		} else if (n.getOperator() == Operator.plus
				&& (String.class.equals(resolvedType(n.getLeft())) || String.class.equals(resolvedType(n.getRight())))) {
			// if at least one is string, the result would be string
			resolvedType(n, TypeWrappers.wrap(String.class));
		} else {
			// Number is not very exact, but it's enough for our purposes
			resolvedType(n, TypeWrappers.wrap(Number.class));
		}
	}

	@Override
	public void visit(CastExpr n, Scope arg) {
		super.visit(n, arg);
		TypeWrapper type = resolveType(arg, n.getType());
		resolvedType(n, type);
	}

	@Override
	public void visit(ClassExpr n, Scope arg) {
		super.visit(n, arg);
		TypeWrapper type = resolveType(arg, n.getType());
		resolvedType(n, type);
	}

	@Override
	public void visit(ConditionalExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, resolvedType((n.getThenExpr() instanceof NullLiteralExpr) ? n.getElseExpr() : n.getThenExpr()));
	}

	@Override
	public void visit(EnclosedExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, resolvedType(n.getInner()));
	}

	@Override
	public void visit(InstanceOfExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, TypeWrappers.wrap(boolean.class));

	}

	@Override
	public void visit(StringLiteralExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, TypeWrappers.wrap(String.class));
	}

	@Override
	public void visit(IntegerLiteralExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, TypeWrappers.wrap(int.class));
	}

	@Override
	public void visit(LongLiteralExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, TypeWrappers.wrap(long.class));
	}

	@Override
	public void visit(IntegerLiteralMinValueExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, TypeWrappers.wrap(int.class));

	}

	@Override
	public void visit(LongLiteralMinValueExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, TypeWrappers.wrap(long.class));

	}

	@Override
	public void visit(CharLiteralExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, TypeWrappers.wrap(char.class));
	}

	@Override
	public void visit(DoubleLiteralExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, TypeWrappers.wrap(n.getValue().endsWith("f") || n.getValue().endsWith("F") ? float.class
				: double.class));
	}

	@Override
	public void visit(BooleanLiteralExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, TypeWrappers.wrap(boolean.class));
	}

	@Override
	public void visit(NullLiteralExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, null);
	}

	@Override
	public void visit(MethodCallExpr n, Scope arg) {
		super.visit(n, arg);
		MethodWrapper method;
		TypeWrapper[] argumentTypes = null;
		if (n.getArgs() != null) {
			argumentTypes = new TypeWrapper[n.getArgs().size()];
			for (int i = 0; i < n.getArgs().size(); ++i) {
				argumentTypes[i] = resolvedType(n.getArgs().get(i));
			}
		} else {
			argumentTypes = new TypeWrapper[0];
		}
		if (n.getScope() == null) {
			MethodsWithScope ms = arg.resolveMethod(n.getName(), argumentTypes);
			PreConditions.checkState(ms != null, "The method %s should've been resolved", n.getName());
			method = ms.getMethod();
		} else {
			TypeWrapper scopeType = resolvedType(n.getScope());
			PreConditions.checkState(scopeType != null, "The method %s 's scope should've been resolved", n.getName());
			method = scopeType.findMethod(n.getName(), argumentTypes).getOrThrow();
		}

		resolvedType(n, method.getReturnType());
		resolvedMethod(n, method);

	}

	@Override
	public void visit(FieldAccessExpr n, Scope arg) {
		super.visit(n, arg);
		TypeWrapper scopeType = resolvedType(n.getScope());
		if (scopeType == null) {
			// if the scope's type is null, it's because is part of a qualified class name x.y.z -> so try to
			// resolve it
			TypeWithScope exprType = arg.resolveType(n.toString());
			if (exprType != null) {
				resolvedType(n, exprType.getType());
			} else {
				resolvedType(n, null);
			}
		} else {
			// if not -> resolve the field in the type
			FieldWrapper field = scopeType.findField(n.getField()).getOrThrow();
			resolvedType(n, field.getType());
		}

	}

	@Override
	public void visit(NameExpr n, Scope arg) {
		super.visit(n, arg);
		Node parent = parent(n);
		if (parent instanceof QualifiedNameExpr || parent instanceof ImportDeclaration
				|| parent instanceof PackageDeclaration) {
			// don't bother as is only part of a package or import declaration
			resolvedType(n, null);
		} else if (parent instanceof SwitchEntryStmt) {
			// this is an enum label -> the type is the enum from the selector
			resolvedType(n, resolvedType(((SwitchStmt) parent(parent)).getSelector()));
		} else {

			// here n can be:
			// 1) start of a fully qualified class x.y.z
			// 2) the name of a class
			// 3) a field
			// 4) a variable or a parameter
			// 5) an enum constant
			VariableWithScope var = arg.resolveVariable(n.getName());
			if (var != null) {
				resolvedVariable(n, var.getVariable());
				resolvedType(n, var.getVariable().getType());
			} else {
				TypeWithScope type = arg.resolveType(n.getName());
				if (type != null) {
					resolvedType(n, type.getType());
				}
			}
		}

	}

	@Override
	public void visit(QualifiedNameExpr n, Scope arg) {
		// don't bother as is only part of a package or import declaration
		resolvedType(n, null);
		super.visit(n, arg);
	}

	@Override
	public void visit(ThisExpr n, Scope arg) {
		super.visit(n, arg);
		if (n.getClassExpr() != null) {
			resolvedType(n, resolvedType(n.getClassExpr()));
		} else {
			ClassScope classScope = arg.closest(ClassScope.class);
			resolvedType(n, classScope.getClazz());
		}
	}

	@Override
	public void visit(SuperExpr n, Scope arg) {
		super.visit(n, arg);
		ClassWrapper thisClazz;
		if (n.getClassExpr() != null) {
			thisClazz = (ClassWrapper) resolvedType(n.getClassExpr());
		} else {
			ClassScope classScope = arg.closest(ClassScope.class);
			thisClazz = classScope.getClazz();
		}
		resolvedType(n, thisClazz.getSuperclass().getOrElse(TypeWrappers.wrap(Object.class)));
	}

	@Override
	public void visit(UnaryExpr n, Scope arg) {
		super.visit(n, arg);
		resolvedType(n, resolvedType(n.getExpr()));
	}

}