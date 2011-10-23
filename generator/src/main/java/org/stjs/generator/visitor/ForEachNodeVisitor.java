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
package org.stjs.generator.visitor;

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
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class ForEachNodeVisitor<V> extends VoidVisitorAdapter<V> {
	protected void before(Node node, V arg) {
	}

	protected void after(Node node, V arg) {
	}

	@Override
	public void visit(AnnotationDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(AnnotationMemberDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ArrayAccessExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ArrayCreationExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ArrayInitializerExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(AssertStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(AssignExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(BinaryExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(BlockComment n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(BlockStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(BooleanLiteralExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(BreakStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(CastExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(CatchClause n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(CharLiteralExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ClassExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ClassOrInterfaceType n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(CompilationUnit n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ConditionalExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ConstructorDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ContinueStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(DoStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(DoubleLiteralExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(EmptyMemberDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(EmptyStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(EmptyTypeDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(EnclosedExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(EnumConstantDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(EnumDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ExpressionStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(FieldAccessExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(FieldDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ForeachStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ForStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(IfStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ImportDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(InitializerDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(InstanceOfExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(IntegerLiteralExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(IntegerLiteralMinValueExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(JavadocComment n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(LabeledStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(LineComment n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(LongLiteralExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(LongLiteralMinValueExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(MarkerAnnotationExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(MemberValuePair n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(MethodCallExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(MethodDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(NameExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(NormalAnnotationExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(NullLiteralExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ObjectCreationExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(PackageDeclaration n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(Parameter n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(PrimitiveType n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(QualifiedNameExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ReferenceType n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ReturnStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(SingleMemberAnnotationExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(StringLiteralExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(SuperExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(SwitchEntryStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(SwitchStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(SynchronizedStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ThisExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(ThrowStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(TryStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(TypeDeclarationStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(TypeParameter n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(UnaryExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(VariableDeclarationExpr n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(VariableDeclarator n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(VariableDeclaratorId n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(VoidType n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(WhileStmt n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

	@Override
	public void visit(WildcardType n, V arg) {
		before(n, arg);
		super.visit(n, arg);
		after(n, arg);
	}

}
