package org.stjs.generator.scope.simple;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.stjs.generator.ASTNodeData;
import org.stjs.generator.JavascriptKeywords;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.NameScope;
import org.stjs.generator.scope.ParameterScope;
import org.stjs.generator.scope.TypeScope;
import org.stjs.generator.scope.VariableScope;
import org.stjs.generator.scope.classloader.ClassLoaderWrapper;
import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.generator.scope.simple.Scope.ScopeVisitor;
import org.stjs.generator.utils.Option;

import com.google.common.primitives.Primitives;


public class SimpleScopeBuilder extends VoidVisitorAdapter<Scope> {

	private final ClassLoaderWrapper classLoader;

	public SimpleScopeBuilder(ClassLoaderWrapper classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public void visit(final CompilationUnit n, Scope scope) {
		scope.apply(new DefaultScopeVisitor() {
			public void apply(CompilationUnitScope scope) {
				// asterisk declaration have lower priority => process them first (JLS §7.5.2)
				if (n.getImports() != null) {
					for (ImportDeclaration importDecl : n.getImports()) {
						NameExpr name = importDecl.getName();
						checkImport(importDecl, name.toString());
						if (importDecl.isAsterisk()) {
							if (importDecl.isStatic()) {
								QualifiedNameExpr expr = (QualifiedNameExpr) name;
								for (ClassWrapper clazz : identifyQualifiedNameExprClass(expr.getQualifier())) {
									for (Field field : clazz.getDeclaredNonPrivateStaticFields()) {
										scope.addField(field);
									}
									for (Method method : clazz.getDeclaredNonPrivateStaticMethods()) {
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
									for (Field field : clazz.getDeclaredField(fieldOrTypeOrMethodName)) {
										scope.addField(field);
									}
									List<Method> methods = clazz.getDeclaredMethods(fieldOrTypeOrMethodName);
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
			}
		});
		
		super.visit(n, scope);
	}
	
	@Override
	public void visit(final ClassOrInterfaceDeclaration n, Scope scope) {
		if ((n.getExtends() != null) && (n.getExtends().size() > 0) && !n.isInterface()) {
			// TODO : populate scope with parent
		}
		final AtomicReference<Scope> classScope = new AtomicReference<Scope>();
		scope.apply(new DefaultScopeVisitor() {
			
			@Override
			public void apply(CompilationUnitScope compilationUnitScope) {
				// TODO : this is not good enough for inner classes (which need the list of outer classes in their qualified name)
				String qualifiedName = compilationUnitScope.getPackage().getName().toString()+"."+n.getName();
				if (!n.isInterface()) {
					ClassWrapper clazz = loadClassOrInnerClass(qualifiedName).getOrThrow("Cannot load class or interface "+qualifiedName);
					compilationUnitScope.addType(clazz);
					if (n.getTypeParameters() != null) {
						for (TypeParameter p : n.getTypeParameters()) {
							// TODO : Handle Generic Types definitions
						}
					}
					for (ClassWrapper innerClass : clazz.getDeclaredClasses()) {
						compilationUnitScope.addType(innerClass);
					}
					ClassScope scope = new ClassScope(clazz, compilationUnitScope);
					for (Field field : clazz.getDeclaredFields()) {
						scope.addField(field);
					}
					for (Method method : clazz.getDeclaredMethods()) {
						scope.addMethod(method);
					}
					classScope.set(scope);
				}
			}

			@Override
			public void apply(ClassScope classScope) {
				// NOTE : static class must inherit the compilation unit scope, non static class the class scope
			//	throw new RuntimeException("Inner class not implemented yet");
			}
		});
		super.visit(n, classScope.get());
	}
	
	@Override
	public void visit(final MethodDeclaration n, Scope currentScope) {
		handleMethodDeclaration(n.getParameters(), currentScope);
		super.visit(n, currentScope);
	}

	private void handleMethodDeclaration(final List<Parameter> parameters, Scope currentScope) {

		BasicScope scope = new BasicScope((ClassScope) currentScope);
		if (parameters != null) {
			for (Parameter p : parameters) {
				Type type = p.getType();
				if (type instanceof ReferenceType) {
					ReferenceType refType = (ReferenceType) type;
					if (refType.getArrayCount() > 0) {
						throw new RuntimeException("Arrays are not supported");
					}
					type = refType.getType(); // type is a primitive or class
				}
				ClassWrapper clazz;
				if (type instanceof PrimitiveType) {
					PrimitiveType primitiveType = (PrimitiveType) type;
					clazz = PrimitiveTypes.primitiveReflectionType(primitiveType);
				} else if (type instanceof VoidType) {
					clazz = new ClassWrapper(void.class);
				} else if (type instanceof ClassOrInterfaceType) {
					clazz = scope.resolveType(type.toString());
				} else if (type instanceof WildcardType) {
					throw new RuntimeException("Generics not yet implemented");
				} else {
					throw new RuntimeException("Unpexcted type " + type);
				}
				scope.addVariable(new ParameterVariable(clazz, p.getId().getName()));
			}
		}
		// if (n.getTypeParameters() != null) {
		// for (TypeParameter p : n.getTypeParameters()) {
		// // TODO : Generics
		// }
		// }

	}

	@Override
	public void visit(ConstructorDeclaration n, Scope currentScope) {
		handleMethodDeclaration(n.getParameters(), currentScope);
		super.visit(n, currentScope);
	}
	
	@Override
	public void visit(ForeachStmt n, Scope currentScope) {
		super.visit(n, new BasicScope(currentScope));
	}

	@Override
	public void visit(ForStmt n, Scope currentScope) {
		super.visit(n, new BasicScope(currentScope));
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
	        	// TODO : How to load the class???? the class name is automatically generated,
	        	// and we might have more than 1
//	        	scope = new ClassScope(scope);
//	            for (BodyDeclaration member : n.getAnonymousClassBody()) {
//	                member.accept(this, scope);
//	            }
	        }
	}
	
	@Override
	public void visit(final EnumDeclaration n, final Scope currentScope) {
		ClassScope parentClassScope = (ClassScope) currentScope;
		ClassWrapper parentClass = parentClassScope.getClazz();
		ClassWrapper enumClass = classLoader.loadClass(parentClass.getName() + "$" + n.getName()).getOrThrow();
		ClassScope enumClassScope = new ClassScope(enumClass, currentScope);
		for (ClassWrapper innerClass : enumClass.getDeclaredClasses()) {
			/*
			 * TODO : that's maybe not correct. Need to check what's the diff between static and non static inner
			 * classes of enumrations
			 */
			enumClassScope.addType(innerClass);
		}
		for (Field field : enumClass.getDeclaredFields()) {
			enumClassScope.addField(field);
		}
		for (Method method : enumClass.getDeclaredMethods()) {
			enumClassScope.addMethod(method);
		}
		super.visit(n, enumClassScope);
		
	}

	
	private Option<ClassWrapper> identifyQualifiedNameExprClass(NameExpr expr) {
		return loadClassOrInnerClass(expr.toString());
	}
	
	private Option<ClassWrapper> loadClassOrInnerClass(String classLoaderCompatibleName) {
		while (true) {
			Option<ClassWrapper> clazz = classLoader.loadClass(classLoaderCompatibleName);
			if (clazz.isDefined()) {
				return clazz;
			}
			int lastIndexDot = classLoaderCompatibleName.lastIndexOf('.');
			if (lastIndexDot < 0) {
				return Option.none();
			}
			classLoaderCompatibleName = replaceCharAt(classLoaderCompatibleName, lastIndexDot, '$');
		}
	}

	private String replaceCharAt(String s, int pos, char c) {
		StringBuffer buf = new StringBuffer(s);
		buf.setCharAt(pos, c);
		return buf.toString();
	}

	private void checkImport(ImportDeclaration importDecl, String string) {
		// TODO Auto-generated method stub

	}
}