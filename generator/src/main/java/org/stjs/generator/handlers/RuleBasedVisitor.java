package org.stjs.generator.handlers;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.MatchingRule;
import org.stjs.generator.NodeHandlerWithPriority;

public class RuleBasedVisitor extends DumpVisitor<GenerationContext> {
	private final Map<Node, NodeHandlerWithPriority> nodeHandlers = new IdentityHashMap<Node, NodeHandlerWithPriority>();

	private final List<MatchingRule> rules = new ArrayList<MatchingRule>();

	public void addRule(MatchingRule rule) {
		rules.add(rule);
	}

	public void generate(CompilationUnit cu, GenerationContext context) throws IOException {
		// create the DOM
		Document dom = DocumentHelper.createDocument();
		Element root = dom.addElement("root");

		new XmlVisitor().visit(cu, root);

		// print
		// Pretty print the document to System.out
		// OutputFormat format = OutputFormat.createPrettyPrint();
		// XMLWriter writer = new XMLWriter(System.out, format);
		// writer.write(dom);

		// chose the rule for each node
		for (MatchingRule rule : rules) {
			@SuppressWarnings("unchecked")
			List<Element> nodes = dom.selectNodes(rule.getRule());
			for (Element node : nodes) {
				NodeHandlerWithPriority nh = nodeHandlers.get(node.getData());
				if ((nh == null) || (nh.getPriority() < rule.getHandler().getPriority())) {
					// System.out.println("Rule:" + rule.getName() + " node:" + node.getData());
					nodeHandlers.put((Node) node.getData(), rule.getHandler());
				}
			}
		}

		// visit all the nodes now
		visit(cu, context);
	}

	protected VoidVisitor<GenerationContext> getVisitor(Node node, GenerationContext context) {
		if (context.isSkipHandlers()) {// TODO find a better way
			// next call should check
			context.checkHandlers();
			return null;
		}
		NodeHandlerWithPriority nh = nodeHandlers.get(node);
		if (nh != null) {
			return nh.getHandler();
		}
		return null;
	}

	@Override
	public void visit(CompilationUnit n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(PackageDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ImportDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(TypeParameter n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(LineComment n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(BlockComment n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(EnumDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(EmptyTypeDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(EnumConstantDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(AnnotationDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(AnnotationMemberDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(FieldDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(VariableDeclarator n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(VariableDeclaratorId n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ConstructorDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(MethodDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(Parameter n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(EmptyMemberDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(InitializerDeclaration n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(JavadocComment n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ClassOrInterfaceType n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(PrimitiveType n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ReferenceType n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(VoidType n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(WildcardType n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ArrayAccessExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ArrayCreationExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ArrayInitializerExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(AssignExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(BinaryExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(CastExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ClassExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ConditionalExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(EnclosedExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(FieldAccessExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(InstanceOfExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(StringLiteralExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(IntegerLiteralExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(LongLiteralExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(IntegerLiteralMinValueExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(LongLiteralMinValueExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(CharLiteralExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(DoubleLiteralExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(BooleanLiteralExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(NullLiteralExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(MethodCallExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(NameExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ObjectCreationExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(QualifiedNameExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ThisExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(SuperExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(UnaryExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(VariableDeclarationExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(MarkerAnnotationExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(SingleMemberAnnotationExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(NormalAnnotationExpr n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(MemberValuePair n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(TypeDeclarationStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(AssertStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(BlockStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(LabeledStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(EmptyStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ExpressionStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(SwitchStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(SwitchEntryStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(BreakStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ReturnStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(IfStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(WhileStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ContinueStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(DoStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ForeachStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ForStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(ThrowStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(SynchronizedStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(TryStmt n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

	@Override
	public void visit(CatchClause n, GenerationContext context) {
		VoidVisitor<GenerationContext> visitor = getVisitor(n, context);
		if (visitor != null) {
			visitor.visit(n, context);
		} else {
			super.visit(n, context);
		}
	}

}
