package org.stjs.generator.javascript.rhino;

import java.io.File;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.mozilla.javascript.Token.CommentType;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.BreakStatement;
import org.mozilla.javascript.ast.CatchClause;
import org.mozilla.javascript.ast.Comment;
import org.mozilla.javascript.ast.ConditionalExpression;
import org.mozilla.javascript.ast.ContinueStatement;
import org.mozilla.javascript.ast.DoLoop;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.EmptyExpression;
import org.mozilla.javascript.ast.EmptyStatement;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.ForInLoop;
import org.mozilla.javascript.ast.ForLoop;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Label;
import org.mozilla.javascript.ast.LabeledStatement;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NewExpression;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.ReturnStatement;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.SwitchCase;
import org.mozilla.javascript.ast.SwitchStatement;
import org.mozilla.javascript.ast.ThrowStatement;
import org.mozilla.javascript.ast.TryStatement;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;
import org.mozilla.javascript.ast.WhileLoop;
import org.stjs.generator.STJSRuntimeException;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.javascript.BinaryOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.javascript.NameValue;
import org.stjs.generator.javascript.UnaryOperator;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.debugging.sourcemap.SourceMapGenerator;

/**
 * this JavaScript builder uses the rhino AST nodes to build the synthax tree.
 * 
 * @author acraciun
 */
@SuppressWarnings("PMD.ExcessivePublicCount")
public class RhinoJavaScriptBuilder implements JavaScriptBuilder<AstNode> {
	private static int propId = AstNode.LAST_PROP + 1;
	private static final int PROP_JAVA_LINE_NO = propId++;
	private static final int PROP_JAVA_COLUMN_NO = propId++;
	private static final int PROP_JAVA_END_LINE_NO = propId++;
	private static final int PROP_JAVA_END_COLUMN_NO = propId++;

	@Override
	public AstNode name(CharSequence name) {
		Name n = new Name();
		n.setIdentifier(name.toString());
		return n;
	}

	@Override
	public AstNode label(CharSequence name) {
		Label n = new Label();
		n.setName(name.toString());
		return n;
	}

	@Override
	public AstNode asExpressionList(Iterable<AstNode> nodes) {
		return binary(BinaryOperator.COMMA, nodes);
	}

	@Override
	public AstNode block(Iterable<AstNode> statements) {
		Block block = new Block();
		for (AstNode stmt : statements) {
			if (stmt != null) {
				block.addStatement(stmt);
			}
		}
		return block;
	}

	@Override
	public AstNode elementGet(AstNode target, AstNode index) {
		ElementGet prop = new ElementGet();
		prop.setTarget(target);
		prop.setElement(index);
		return prop;
	}

	@Override
	public AstNode newExpression(AstNode target, Iterable<AstNode> arguments) {
		NewExpression ne = new NewExpression();
		ne.setTarget(target);
		if (!Iterables.isEmpty(arguments)) {
			ne.setArguments(list(arguments));
		}
		return ne;
	}

	@Override
	public AstNode array(Iterable<AstNode> values) {
		ArrayLiteral array = new ArrayLiteral();
		for (AstNode value : values) {
			array.addElement(value);
		}
		return array;
	}

	@Override
	public AstNode string(String value) {
		StringLiteral expr = new StringLiteral();
		expr.setQuoteCharacter('"');
		expr.setValue(value);
		return expr;
	}

	@Override
	public AstNode paren(AstNode expr) {
		ParenthesizedExpression paren = new ParenthesizedExpression();
		paren.setExpression(expr);
		return paren;
	}

	@Override
	public AstNode position(AstNode node, int javaStartLineNumber, int javaStartColumnNumber, int javaEndLineNumber, int javaEndColumnNumber) {
		node.putIntProp(PROP_JAVA_LINE_NO, javaStartLineNumber);
		node.putIntProp(PROP_JAVA_COLUMN_NO, javaStartColumnNumber);
		node.putIntProp(PROP_JAVA_END_LINE_NO, javaEndLineNumber);
		node.putIntProp(PROP_JAVA_END_COLUMN_NO, javaEndColumnNumber);
		return node;
	}

	public static int getLineNumber(AstNode node) {
		return node.getIntProp(PROP_JAVA_LINE_NO, -1);
	}

	public static int getColumnNumber(AstNode node) {
		return node.getIntProp(PROP_JAVA_COLUMN_NO, -1);
	}

	public static int getEndLineNumber(AstNode node) {
		return node.getIntProp(PROP_JAVA_END_LINE_NO, -1);
	}

	public static int getEndColumnNumber(AstNode node) {
		return node.getIntProp(PROP_JAVA_END_COLUMN_NO, -1);
	}

	@Override
	public AstNode breakStatement(AstNode label) {
		BreakStatement b = new BreakStatement();
		b.setBreakLabel(cast(label, Name.class));
		return b;
	}

	@SuppressWarnings("unchecked")
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(justification = "Checked cast", value = "BC_UNCONFIRMED_CAST")
	private <T extends AstNode> T cast(AstNode node, Class<T> clazz) {
		if (node == null) {
			return null;
		}
		if (!clazz.isAssignableFrom(node.getClass())) {
			throw new STJSRuntimeException("Received wrong JavaScript node type:" + node.getClass().getName() + " instead of " + clazz.getName()
					+ ". This is probably a ST-JS bug. Please report it to our website");
		}
		return (T) node;
	}

	@Override
	public AstNode caseStatement(AstNode expression, Iterable<AstNode> statements) {
		SwitchCase s = new SwitchCase();
		s.setExpression(expression);
		for (AstNode stmt : statements) {
			if (stmt != null) {
				s.addStatement(stmt);
			}
		}
		return s;
	}

	@Override
	public AstNode catchClause(AstNode varName, AstNode body) {
		CatchClause c = new CatchClause();
		c.setVarName(cast(varName, Name.class));
		if (body instanceof Block) {
			c.setBody((Block) body);
		} else {
			Block b = new Block();
			b.addStatement(body);
			c.setBody(b);
		}
		return c;
	}

	@Override
	public AstNode continueStatement(AstNode label) {
		ContinueStatement c = new ContinueStatement();
		c.setLabel(cast(label, Name.class));
		return c;
	}

	@Override
	public AstNode function(String name, Iterable<AstNode> params, AstNode body) {
		FunctionNode func = new FunctionNode();
		if (name != null) {
			func.setFunctionName((Name) name(name));
		}
		func.setParams(list(params));
		if (body == null) {
			func.setBody(new Block());
		} else if (body instanceof Block) {
			func.setBody(body);
		} else {
			func.setBody(addStatement(null, body));
		}
		return func;
	}

	private ObjectProperty objectProperty(CharSequence name, AstNode value) {
		ObjectProperty prop = new ObjectProperty();
		prop.setLeft(name(name));
		prop.setRight(value);
		return prop;
	}

	@Override
	public AstNode object(Iterable<NameValue<AstNode>> props) {
		ObjectLiteral object = new ObjectLiteral();
		for (NameValue<AstNode> prop : props) {
			object.addElement(objectProperty(prop.getName(), prop.getValue()));
		}
		return object;
	}

	@Override
	public AstNode doLoop(AstNode condition, AstNode body) {
		DoLoop loop = new DoLoop();
		loop.setCondition(condition);
		loop.setBody(body);
		return loop;
	}

	@Override
	public AstNode emptyStatement() {
		return new EmptyStatement();
	}

	@Override
	public AstNode emptyExpression() {
		return new EmptyExpression();
	}

	@Override
	public AstNode forInLoop(AstNode iterator, AstNode iterated, AstNode body) {
		ForInLoop loop = new ForInLoop();
		loop.setIteratedObject(iterated);
		loop.setIterator(iterator);
		loop.setBody(body);
		return loop;
	}

	@Override
	public AstNode forLoop(AstNode init, AstNode condition, AstNode update, AstNode body) {
		ForLoop loop = new ForLoop();
		loop.setInitializer(init);
		loop.setCondition(condition);
		loop.setIncrement(update);
		loop.setBody(body);
		return loop;
	}

	@Override
	public AstNode ifStatement(AstNode condition, AstNode thenPart, AstNode elsePart) {
		IfStatement ifs = new IfStatement();
		ifs.setCondition(condition);
		ifs.setThenPart(thenPart);
		ifs.setElsePart(elsePart);
		return ifs;
	}

	@Override
	public AstNode addStatement(AstNode blockOrStatement, AstNode statement) {
		if (blockOrStatement instanceof Block) {
			((Block) blockOrStatement).addStatement(statement);
			return blockOrStatement;
		}
		Block block = new Block();
		if (blockOrStatement != null) {
			block.addStatement(blockOrStatement);
		}
		if (statement != null) {
			block.addStatement(statement);
		}
		return block;
	}

	@Override
	public AstNode addStatementBeginning(AstNode blockOrStatement, AstNode statement) {
		if (blockOrStatement instanceof Block) {
			if (statement != null) {
				blockOrStatement.addChildrenToFront(statement);
				statement.setParent(blockOrStatement);
			}
			return blockOrStatement;
		}
		Block block = new Block();
		if (statement != null) {
			block.addStatement(statement);
		}
		if (blockOrStatement != null) {
			block.addStatement(blockOrStatement);
		}
		return block;
	}

	@Override
	public AstNode labeledStatement(AstNode label, AstNode statement) {
		LabeledStatement s = new LabeledStatement();
		s.addLabel(cast(label, Label.class));
		s.setStatement(statement);
		return s;
	}

	@Override
	public AstNode returnStatement(AstNode returnValue) {
		ReturnStatement r = new ReturnStatement();
		r.setReturnValue(returnValue);
		return r;
	}

	@Override
	public AstNode switchStatement(AstNode expr, Iterable<AstNode> cases) {
		SwitchStatement s = new SwitchStatement();
		s.setExpression(expr);
		for (AstNode c : cases) {
			// the user must make sure it sends the correct types. TODO the code can check and build the correct type if
			// needed
			s.addCase((SwitchCase) c);
		}
		return s;
	}

	@Override
	public AstNode functionCall(AstNode target, Iterable<AstNode> arguments) {
		FunctionCall fc = new FunctionCall();
		fc.setTarget(target);
		if (!Iterables.isEmpty(arguments)) {
			fc.setArguments(list(arguments));
		}
		return fc;
	}

	@Override
	public AstNode property(AstNode target, CharSequence name) {
		if (target == null) {
			return name(name);
		}
		PropertyGet prop = new PropertyGet();
		prop.setTarget(target);
		prop.setProperty((Name) name(name));
		return prop;
	}

	@Override
	public AstNode variableDeclaration(boolean statement, Iterable<NameValue<AstNode>> vars) {
		VariableDeclaration varDecl = new VariableDeclaration();
		varDecl.setIsStatement(statement);
		for (NameValue<AstNode> v : vars) {
			VariableInitializer var = new VariableInitializer();
			var.setTarget(name(v.getName()));
			var.setInitializer(v.getValue());
			varDecl.addVariable(var);
		}
		return varDecl;
	}

	@Override
	public AstNode variableDeclaration(boolean statement, CharSequence name, AstNode init) {
		VariableDeclaration vars = new VariableDeclaration();
		vars.setIsStatement(statement);
		VariableInitializer var = new VariableInitializer();
		var.setTarget(name(name));
		var.setInitializer(init);
		vars.addVariable(var);
		return vars;
	}

	@Override
	public AstNode tryStatement(AstNode tryBlock, Iterable<AstNode> catchClauses, AstNode finallyBlock) {
		TryStatement t = new TryStatement();
		t.setTryBlock(tryBlock);
		for (AstNode c : catchClauses) {
			t.addCatchClause((CatchClause) c);
		}
		t.setFinallyBlock(finallyBlock);
		return t;
	}

	@Override
	public AstNode whileLoop(AstNode condition, AstNode body) {
		WhileLoop w = new WhileLoop();
		w.setCondition(condition);
		w.setBody(body);
		return w;
	}

	@Override
	public AstNode root(Iterable<AstNode> children) {
		AstRoot r = new AstRoot();
		for (AstNode c : children) {
			if (c != null) {
				r.addChild(c);
			}
		}
		return r;
	}

	@Override
	public AstNode code(String code) {
		CodeFragment c = new CodeFragment();
		c.setCode(code);
		return c;
	}

	@Override
	public String toString(AstNode node) {
		return node.toSource();
	}

	@Override
	public AstNode conditionalExpression(AstNode test, AstNode trueExpr, AstNode falseExpr) {
		ConditionalExpression c = new ConditionalExpression();
		c.setTestExpression(test);
		c.setTrueExpression(trueExpr);
		c.setFalseExpression(falseExpr);
		return c;
	}

	@Override
	public AstNode character(String c) {
		StringLiteral s = new StringLiteral();
		s.setQuoteCharacter('\'');
		s.setValue(c);
		return s;
	}

	@Override
	public AstNode number(Number n) {
		NumberLiteral l = new NumberLiteral();
		l.setValue(n.toString());
		return l;
	}

	@Override
	public AstNode statements(Iterable<AstNode> stmts) {
		Statements s = new Statements();
		for (AstNode stmt : stmts) {
			if (stmt != null) {
				s.addStatement(stmt);
			}
		}
		return s;
	}

	@Override
	public AstNode assignment(AssignOperator operator, AstNode left, AstNode right) {
		Assignment a = new Assignment();
		a.setOperator(operator.getJavaScript());
		a.setLeft(left);
		a.setRight(right);
		return a;
	}

	@Override
	public AstNode binary(BinaryOperator operator, Iterable<AstNode> operands) {
		// this is to deal with the COMMA operator who can have less than two operands
		if (Iterables.isEmpty(operands)) {
			return new EmptyExpression();
		}
		if (Iterables.size(operands) == 1) {
			return operands.iterator().next();
		}
		InfixExpression list = new InfixExpression();
		list.setOperator(operator.getJavaScript());
		Iterator<AstNode> it = operands.iterator();
		list.setLeft(it.next());
		list.setRight(it.next());
		while (it.hasNext()) {
			InfixExpression tmpIncrements = new InfixExpression();
			tmpIncrements.setOperator(operator.getJavaScript());
			tmpIncrements.setLeft(list);
			tmpIncrements.setRight(it.next());
			list = tmpIncrements;
		}
		return list;
	}

	@Override
	public AstNode keyword(Keyword token) {
		KeywordLiteral k = new KeywordLiteral();
		k.setType(token.getJavaScript());
		return k;
	}

	@Override
	public AstNode unary(UnaryOperator operator, AstNode operand) {
		UnaryExpression u = new UnaryExpression();
		u.setIsPostfix(operator.isPostfix());
		u.setOperator(operator.getJavaScript());
		u.setOperand(operand);
		return u;
	}

	@Override
	public AstNode expressionStatement(AstNode expr) {
		ExpressionStatement e = new ExpressionStatement();
		e.setExpression(expr);
		return e;
	}

	private static <T extends AstNode> List<T> list(Iterable<T> it) {
		if (it instanceof List) {
			return (List<T>) it;
		}
		return Lists.newArrayList(it);

	}

	@Override
	public SourceMapGenerator writeJavaScript(AstNode javascriptRoot, File inputFile, boolean generateSourceMap, Writer writer) {
		RhinoJavaScriptWriter jsw = new RhinoJavaScriptWriter(writer, inputFile, generateSourceMap);

		jsw.visitAstRoot(cast(javascriptRoot, AstRoot.class), null);

		return jsw.getSourceMapGenerator();
	}

	@Override
	public AstNode comment(AstNode node, String comment) {
		if (node == null) {
			return null;
		}
		if (comment != null) {
			node.setJsDocNode(new Comment(0, comment.length(), CommentType.JSDOC, comment));
		}
		return node;
	}

	@Override
	public AstNode throwStatement(AstNode expr) {
		ThrowStatement s = new ThrowStatement();
		s.setExpression(expr);
		return s;
	}

}
