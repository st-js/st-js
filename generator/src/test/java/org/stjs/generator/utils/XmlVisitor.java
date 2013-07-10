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
package org.stjs.generator.utils;

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
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.lang.reflect.Method;

import org.dom4j.Element;
import org.dom4j.util.UserDataElement;

public class XmlVisitor extends VoidVisitorAdapter<Element> implements VoidVisitor<Element> {

	protected boolean isAttributeAccessor(Method method) {

		String methodName = method.getName();

		return ((Integer.TYPE == method.getReturnType()) || (Boolean.TYPE == method.getReturnType()) || (String.class == method
				.getReturnType()))
				&& (method.getParameterTypes().length == 0)
				&& (Void.TYPE != method.getReturnType())
				&& !methodName.startsWith("jjt")
				&& !methodName.equals("toString")
				&& !methodName.equals("getScope")
				&& !methodName.equals("getClass")
				&& !methodName.equals("getTypeNameNode")
				&& !methodName.equals("getImportedNameNode") && !methodName.equals("hashCode");
	}

	@SuppressWarnings("unchecked")
	private Element openElement(Node n, Element parent) {
		UserDataElement elem = new UserDataElement(n.getClass().getSimpleName());
		parent.elements().add(elem);
		elem.setData(n);
		for (Method method : n.getClass().getDeclaredMethods()) {
			if (isAttributeAccessor(method)) {
				try {
					Object value = method.invoke(n);

					elem.addAttribute(propertyName(method.getName()), value != null ? value.toString() : "");
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return elem;
	}

	private String propertyName(String name) {
		String prop = name;
		if (name.startsWith("get")) {
			prop = name.substring(3);
		} else if (name.startsWith("is")) {
			prop = name.substring(2);
		}
		return Character.toLowerCase(prop.charAt(0)) + prop.substring(1);
	}

	@Override
	public void visit(CompilationUnit n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(PackageDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ImportDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(TypeParameter n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(LineComment n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(BlockComment n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(EnumDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(EmptyTypeDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(EnumConstantDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(AnnotationDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(AnnotationMemberDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(FieldDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(VariableDeclarator n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(VariableDeclaratorId n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ConstructorDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(MethodDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(Parameter n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(EmptyMemberDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(InitializerDeclaration n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(JavadocComment n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ClassOrInterfaceType n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(PrimitiveType n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ReferenceType n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(VoidType n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(WildcardType n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ArrayAccessExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ArrayCreationExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ArrayInitializerExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(AssignExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(BinaryExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(CastExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ClassExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ConditionalExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(EnclosedExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(FieldAccessExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(InstanceOfExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(StringLiteralExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(IntegerLiteralExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(LongLiteralExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(IntegerLiteralMinValueExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(LongLiteralMinValueExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(CharLiteralExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(DoubleLiteralExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(BooleanLiteralExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(NullLiteralExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(MethodCallExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(NameExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ObjectCreationExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(QualifiedNameExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ThisExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(SuperExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(UnaryExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(VariableDeclarationExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(MarkerAnnotationExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(SingleMemberAnnotationExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(NormalAnnotationExpr n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(MemberValuePair n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(TypeDeclarationStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(AssertStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(BlockStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(LabeledStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(EmptyStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ExpressionStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(SwitchStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(SwitchEntryStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(BreakStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ReturnStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(IfStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(WhileStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ContinueStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(DoStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ForeachStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ForStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(ThrowStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(SynchronizedStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(TryStmt n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

	@Override
	public void visit(CatchClause n, Element arg) {
		Element elem = openElement(n, arg);
		super.visit(n, elem);

	}

}
