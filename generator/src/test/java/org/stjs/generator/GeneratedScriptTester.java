/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator;

import static junit.framework.Assert.assertEquals;
import japa.parser.ast.BlockComment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EmptyMemberDeclaration;
import japa.parser.ast.body.EmptyTypeDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.EmptyStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;
import japa.parser.ast.visitor.VoidVisitor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.stjs.generator.GenerationContextFactory.PartialGenerationContext;
import org.stjs.generator.handlers.RuleBasedVisitor;
import org.stjs.generator.handlers.utils.Option;

public class GeneratedScriptTester {

	public static GeneratedScriptTester handlerTester(Class<? extends VoidVisitor<GenerationContext>> handlerClass,
			boolean trimAllWhiteSpaces) {
		return new GeneratedScriptTester(handlerClass, trimAllWhiteSpaces);
	}

	private final Class<? extends VoidVisitor<GenerationContext>> handlerClass;
	private final boolean trimAllWhiteSpaces;
	private boolean delegateVisitor = true;
	private MaybeDelegateVisitor visitor;

	private GeneratedScriptTester(Class<? extends VoidVisitor<GenerationContext>> handlerClass,
			boolean trimAllWhiteSpaces) {
		this.handlerClass = handlerClass;
		this.trimAllWhiteSpaces = trimAllWhiteSpaces;
	}
	
	public GeneratedScriptTester doNotPropagateInnerNodesVisits() {
		this.delegateVisitor = false;
		return this;
	}

	public GeneratedScriptTester assertGenerateString(String expected, Node node) {
		return assertGenerateString(expected, node, new GenerationContext(new File("test.java")));
	}

	public GeneratedScriptTester assertGenerateString(String expected, Node node, GenerationContext context) {
		try {
			assertEquals(expected, generate(node, context));
			return this;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// TODO : use a mocking framework instead of re-inventing the wheel!
	public GeneratedScriptTester assertVisitorCount(Class<? extends Node> nodeType, int count) {
		AtomicInteger nodeCount = visitor.callCount.get(nodeType);
		assertEquals(count, (int) (nodeCount == null ? 0 : nodeCount.get()));
		return this;
	}

	private String generate(Node node, GenerationContext context)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		visitor = new MaybeDelegateVisitor(delegateVisitor);

		VoidVisitor<GenerationContext> handler = handlerClass.getConstructor(
				RuleBasedVisitor.class).newInstance(visitor);
		node.accept(handler, context);
		String got = visitor.getPrinter().toString();
		if (trimAllWhiteSpaces) {
			got = got.replaceAll("\\s", "");
		}
		return got;
	}

	public void assertGenerateString(String expected, Node node, PartialGenerationContext context) {
		assertGenerateString(expected, node, context.build());
	}
	
	private static class MaybeDelegateVisitor extends RuleBasedVisitor {
		private final boolean delegate;
		// TODO: When we'll use guava, use computing map instead
		// TODO: Use invariant Map
		final Map<Class<? extends Node>, AtomicInteger> callCount = new HashMap<Class<? extends Node>, AtomicInteger>();
		public MaybeDelegateVisitor(boolean delegate) {
			this.delegate = delegate;
		}

		public void visit(CompilationUnit n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
		}

		public void visit(PackageDeclaration n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ImportDeclaration n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(TypeParameter n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(LineComment n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(BlockComment n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ClassOrInterfaceDeclaration n,
				GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(EnumDeclaration n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(EmptyTypeDeclaration n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(EnumConstantDeclaration n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(AnnotationDeclaration n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(AnnotationMemberDeclaration n,
				GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(FieldDeclaration n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(VariableDeclarator n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(VariableDeclaratorId n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ConstructorDeclaration n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(MethodDeclaration n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(Parameter n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(EmptyMemberDeclaration n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(InitializerDeclaration n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(JavadocComment n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ClassOrInterfaceType n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(PrimitiveType n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ReferenceType n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(VoidType n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(WildcardType n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ArrayAccessExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ArrayCreationExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ArrayInitializerExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(AssignExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(BinaryExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(CastExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ClassExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ConditionalExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(EnclosedExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(FieldAccessExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(InstanceOfExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(StringLiteralExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(IntegerLiteralExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(LongLiteralExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(IntegerLiteralMinValueExpr n,
				GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(LongLiteralMinValueExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(CharLiteralExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(DoubleLiteralExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(BooleanLiteralExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(NullLiteralExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(MethodCallExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(NameExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ObjectCreationExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(QualifiedNameExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ThisExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(SuperExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(UnaryExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(VariableDeclarationExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(MarkerAnnotationExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(SingleMemberAnnotationExpr n,
				GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(NormalAnnotationExpr n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(MemberValuePair n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ExplicitConstructorInvocationStmt n,
				GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(TypeDeclarationStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(AssertStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(BlockStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(LabeledStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(EmptyStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ExpressionStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(SwitchStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(SwitchEntryStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(BreakStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ReturnStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(IfStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(WhileStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ContinueStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(DoStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ForeachStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ForStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(ThrowStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(SynchronizedStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(TryStmt n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

		public void visit(CatchClause n, GenerationContext context) {
			AtomicInteger count = callCount.get(n.getClass());
			if (count == null) {
				count = new AtomicInteger(0);
				callCount.put(n.getClass(), count);
			}
			count.incrementAndGet();
			if (delegate) {super.visit(n, context);}
		}

	}
	

}