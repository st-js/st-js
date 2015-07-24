package org.stjs.generator.javascript.rhino;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.mozilla.javascript.Node;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.BreakStatement;
import org.mozilla.javascript.ast.CatchClause;
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

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;

/**
 * This class visits a JavaScript AST tree and generate the corresponding source code. It handles also the source maps.
 *
 * @author acraciun
 */
@SuppressWarnings("PMD.ExcessivePublicCount")
public class RhinoJavaScriptWriter implements AstVisitor<Boolean> {
	private static final String LINE_JAVA_DOC = " * ";
	private static final String INDENT = "    ";
	private static final String START_JAVA_DOC = "/**";
	private static final String END_JAVA_DOC = " */";
	private int level;

	private boolean indented;

	private final Writer writer;

	private int currentLine;
	private int currentColumn;
	private final RhinoNodeVisitorSupport visitorSupport = new RhinoNodeVisitorSupport();

	private final SourceMapGenerator sourceMapGenerator;
	private final boolean generateSourceMap;
	private final File inputFile;

	private FilePosition javaPosition;
	private FilePosition javaScriptPosition;

	public RhinoJavaScriptWriter(Writer writer, File inputFile, boolean generateSourceMap) {
		this.writer = writer;
		this.inputFile = inputFile;
		this.generateSourceMap = generateSourceMap;
		this.sourceMapGenerator = generateSourceMap ? SourceMapGeneratorFactory.getInstance(SourceMapFormat.V3) : null;
	}

	protected RhinoJavaScriptWriter indent() {
		level++;
		return this;
	}

	protected RhinoJavaScriptWriter unindent() {
		level--;
		return this;
	}

	protected void makeIndent() {
		for (int i = 0; i < level; i++) {
			try {
				writer.append(INDENT);
			}
			catch (IOException e) {
				throw new STJSRuntimeException("Writing problem:" + e, e);
			}
			currentColumn += INDENT.length();
		}
	}

	protected RhinoJavaScriptWriter print(String arg) {
		if (!indented) {
			makeIndent();
			indented = true;
		}
		try {
			writer.append(arg);
		}
		catch (IOException e) {
			throw new STJSRuntimeException("Writing problem:" + e, e);
		}
		// TODO check for newlines in the string
		currentColumn += arg.length();
		return this;
	}

	protected RhinoJavaScriptWriter printComments(AstNode node) {
		String comment = node.getJsDoc();
		if (comment != null) {
			println(START_JAVA_DOC);
			String[] lines = comment.split("\n");
			for (String line : lines) {
				print(LINE_JAVA_DOC).println(line);
			}
			println(END_JAVA_DOC);
		}
		return this;
	}

	public RhinoJavaScriptWriter println(String arg) {
		print(arg);
		println();
		return this;
	}

	public RhinoJavaScriptWriter println() {
		try {
			writer.append('\n');
		}
		catch (IOException e) {
			throw new STJSRuntimeException("Writing problem:" + e, e);
		}
		indented = false;
		currentLine++;
		currentColumn = 0;
		addMapping();
		return this;
	}

	protected void startPosition(AstNode node) {
		if (generateSourceMap) {
			javaPosition = new FilePosition(RhinoJavaScriptBuilder.getLineNumber(node) - 1, RhinoJavaScriptBuilder.getColumnNumber(node) - 1);
			javaScriptPosition = new FilePosition(currentLine, currentColumn);
		}
	}

	/**
	 * this is only for statements on several lines to be able to catch end of inline function defintions
	 *
	 * @param node
	 */
	protected void endPosition(AstNode node) {
		if (generateSourceMap) {
			boolean hasPosition = javaScriptPosition != null && javaScriptPosition.getLine() != currentLine;
			if (hasPosition) {
				javaPosition = new FilePosition(RhinoJavaScriptBuilder.getEndLineNumber(node) - 1,
						RhinoJavaScriptBuilder.getEndColumnNumber(node) - 1);
				javaScriptPosition = new FilePosition(currentLine, currentColumn);
			}
		}
	}

	protected void addMapping() {
		if (generateSourceMap) {
			FilePosition endJavaScriptPosition = new FilePosition(currentLine, currentColumn);
			if (javaPosition != null && javaPosition.getLine() >= 0 && javaPosition.getColumn() >= 0) {
				sourceMapGenerator.addMapping(inputFile.getName(), null, javaPosition, javaScriptPosition, endJavaScriptPosition);
				javaPosition = null;
			}
		}
	}

	@Override
	public void visitArrayLiteral(ArrayLiteral a, Boolean param) {
		print("[");
		if (a.getElements() != null) {
			printList(a.getElements(), param);
		}
		print("]");
	}

	protected <T extends AstNode> void printList(List<T> items, Boolean param) {
		int max = items.size();
		int count = 0;
		for (AstNode item : items) {
			visitorSupport.accept(item, this, param);
			if (count < max - 1) {
				count++;
				print(", ");
			} else if (item instanceof EmptyExpression) {
				print(",");
			}
		}
	}

	@Override
	public void visitAssignment(Assignment a, Boolean param) {
		printComments(a);
		printBinaryOperator(a.getType(), a.getLeft(), a.getRight(), param);
	}

	protected void printBinaryOperator(int op, AstNode left, AstNode right, Boolean param) {
		visitorSupport.accept(left, this, param);
		print(" ");
		print(AstNode.operatorToString(op));
		print(" ");
		visitorSupport.accept(right, this, param);
	}

	@Override
	public void visitAstRoot(AstRoot r, Boolean param) {
		for (Node child : r) {
			visitorSupport.accept(child, this, param);
		}
		addSourceMapURL();
	}

	@Override
	public void visitBlock(Block block, Boolean param) {
		if (block.getFirstChild() == null) {
			// empty blocks
			print("{}");
			return;
		}
		println("{").indent();
		for (Node child : block) {
			visitorSupport.accept(child, this, param);
		}
		unindent().print("}");
	}

	@Override
	public void visitBreakStatemen(BreakStatement b, Boolean param) {
		startPosition(b);
		print("break");
		if (b.getBreakLabel() != null) {
			print(" ");
			visitorSupport.accept(b.getBreakLabel(), this, param);
		}
		println(";");

	}

	@Override
	public void visitCatchClause(CatchClause c, Boolean param) {
		print("catch (");
		visitorSupport.accept(c.getVarName(), this, param);
		print(") ");
		visitorSupport.accept(c.getBody(), this, param);
	}

	@Override
	public void visitConditionalExpression(ConditionalExpression c, Boolean param) {
		visitorSupport.accept(c.getTestExpression(), this, param);
		print(" ? ");
		visitorSupport.accept(c.getTrueExpression(), this, param);
		print(" : ");
		visitorSupport.accept(c.getFalseExpression(), this, param);
	}

	@Override
	public void visitContinueStatement(ContinueStatement c, Boolean param) {
		startPosition(c);
		print("continue");
		if (c.getLabel() != null) {
			print(" ");
			visitorSupport.accept(c.getLabel(), this, param);
		}
		println(";");

	}

	@Override
	public void visitDoLoop(DoLoop d, Boolean param) {
		startPosition(d);
		print("do ");
		visitorSupport.accept(d.getBody(), this, param);
		print(" while (");
		visitorSupport.accept(d.getCondition(), this, param);
		println(");");

	}

	@Override
	public void visitElementGet(ElementGet eg, Boolean param) {
		visitorSupport.accept(eg.getTarget(), this, param);
		print("[");
		visitorSupport.accept(eg.getElement(), this, param);
		print("]");
	}

	@Override
	public void visitEmptyStatement(EmptyStatement s, Boolean param) {
		startPosition(s);
		println(";");

	}

	@Override
	public void visitEmptyExpression(EmptyExpression s, Boolean param) {
		// do nothing
	}

	@Override
	public void visitExpressionStatement(ExpressionStatement e, Boolean param) {
		printComments(e);
		startPosition(e);
		visitorSupport.accept(e.getExpression(), this, param);
		endPosition(e);
		println(";");

	}

	private void printStatementAsBlock(AstNode stmt, Boolean param) {
		printStatementAsBlock(stmt, param, true);
	}

	private void printStatementAsBlock(AstNode stmt, Boolean param, boolean addNewLineAfterBlock) {
		if (stmt instanceof Block) {
			visitorSupport.accept(stmt, this, param);
			if (addNewLineAfterBlock) {
				println();
			}
		} else {
			println().indent();
			visitorSupport.accept(stmt, this, param);
			unindent();
		}
	}

	@Override
	public void visitForInLoop(ForInLoop f, Boolean param) {
		startPosition(f);
		print("for (");
		visitorSupport.accept(f.getIterator(), this, param);
		print(" in ");
		visitorSupport.accept(f.getIteratedObject(), this, param);
		print(") ");
		printStatementAsBlock(f.getBody(), param);

	}

	@Override
	public void visitForLoop(ForLoop f, Boolean param) {
		startPosition(f);
		print("for (");
		visitorSupport.accept(f.getInitializer(), this, param);
		print("; ");
		visitorSupport.accept(f.getCondition(), this, param);
		print("; ");
		visitorSupport.accept(f.getIncrement(), this, param);
		print(") ");
		printStatementAsBlock(f.getBody(), param);

	}

	@Override
	public void visitFunctionCall(FunctionCall fc, Boolean param) {
		visitorSupport.accept(fc.getTarget(), this, param);
		print("(");
		if (fc.getArguments() != null) {
			printList(fc.getArguments(), param);
		}
		print(")");
	}

	@Override
	public void visitFunctionNode(FunctionNode f, Boolean param) {
		printComments(f);
		print("function");
		if (f.getFunctionName() != null) {
			print(" ");
			visitorSupport.accept(f.getFunctionName(), this, param);
		}
		if (f.getParams() == null) {
			print("() ");
		} else {
			print("(");
			printList(f.getParams(), param);
			print(") ");
		}
		visitorSupport.accept(f.getBody(), this, param);
		// if (functionType == FUNCTION_STATEMENT) {
		// sb.append("\n");
		// }
	}

	@Override
	public void visitIfStatement(IfStatement ifs, Boolean param) {
		startPosition(ifs);
		print("if (");
		visitorSupport.accept(ifs.getCondition(), this, param);
		print(") ");
		printStatementAsBlock(ifs.getThenPart(), param, ifs.getElsePart() == null);
		if (ifs.getElsePart() instanceof IfStatement) {
			print(" else ");
			visitorSupport.accept(ifs.getElsePart(), this, param);
		} else if (ifs.getElsePart() != null) {
			print(" else ");
			printStatementAsBlock(ifs.getElsePart(), param);
		}

	}

	@Override
	public void visitInfixExpression(InfixExpression ie, Boolean param) {
		visitorSupport.accept(ie.getLeft(), this, param);
		print(" ");
		print(AstNode.operatorToString(ie.getType()));
		print(" ");
		visitorSupport.accept(ie.getRight(), this, param);
	}

	@Override
	public void visitKeywordLiteral(KeywordLiteral k, Boolean param) {
		switch (k.getType()) {
		case Token.THIS:
			print("this");
			break;
		case Token.NULL:
			print("null");
			break;
		case Token.TRUE:
			print("true");
			break;
		case Token.FALSE:
			print("false");
			break;
		case Token.DEBUGGER:
			println("debugger;");
			break;
		default:
			break;
		}
	}

	@Override
	public void visitLabel(Label label, Boolean param) {
		print(label.getName());
		println(":");
	}

	@Override
	public void visitLabeledStatement(LabeledStatement labelStatement, Boolean param) {
		for (Label label : labelStatement.getLabels()) {
			visitorSupport.accept(label, this, param); // prints newline
		}
		indent();
		visitorSupport.accept(labelStatement.getStatement(), this, param);
		unindent();
	}

	@Override
	public void visitName(Name name, Boolean param) {
		print(name.getIdentifier());
	}

	@Override
	public void visitNewExpression(NewExpression ne, Boolean param) {
		print("new ");
		visitorSupport.accept(ne.getTarget(), this, param);
		print("(");
		if (ne.getArguments() != null) {
			printList(ne.getArguments(), param);
		}
		print(")");
		if (ne.getInitializer() != null) {
			print(" ");
			visitorSupport.accept(ne.getInitializer(), this, param);
		}
	}

	@Override
	public void visitNumberLitera(NumberLiteral n, Boolean param) {
		print(n.getValue());
	}

	@Override
	public void visitObjectLiteral(ObjectLiteral p, Boolean param) {
		print("{");
		if (p.getElements() != null) {
			printList(p.getElements(), param);
		}
		print("}");
	}

	@Override
	public void visitObjectProperty(ObjectProperty p, Boolean param) {
		visitorSupport.accept(p.getLeft(), this, param);
		if (p.getType() == Token.COLON) {
			print(": ");
		}
		visitorSupport.accept(p.getRight(), this, param);
	}

	@Override
	public void visitParenthesizedExpression(ParenthesizedExpression p, Boolean param) {
		print("(");
		visitorSupport.accept(p.getExpression(), this, param);
		print(")");
	}

	@Override
	public void visitPropertyGet(PropertyGet p, Boolean param) {
		visitorSupport.accept(p.getLeft(), this, param);
		print(".");
		visitorSupport.accept(p.getRight(), this, param);
	}

	@Override
	public void visitReturnStatement(ReturnStatement r, Boolean param) {
		startPosition(r);
		print("return");
		if (r.getReturnValue() != null) {
			print(" ");
			visitorSupport.accept(r.getReturnValue(), this, param);
		}
		println(";");

	}

	@Override
	public void visitStatements(Statements s, Boolean param) {
		printComments(s);
		for (Node stmt : s) {
			visitorSupport.accept(stmt, this, param);
		}
	}

	@Override
	public void visitStringLiteral(StringLiteral expr, Boolean param) {
		print(Character.toString(expr.getQuoteCharacter()));
		print(ScriptRuntime.escapeString(expr.getValue(), expr.getQuoteCharacter()));
		print(Character.toString(expr.getQuoteCharacter()));
	}

	@Override
	public void visitSwitchCase(SwitchCase s, Boolean param) {
		if (s.getExpression() == null) {
			println("default:");
		} else {
			print("case ");
			visitorSupport.accept(s.getExpression(), this, param);
			println(":");
		}
		if (s.getStatements() != null) {
			indent();
			for (AstNode stmt : s.getStatements()) {
				visitorSupport.accept(stmt, this, param);
			}
			unindent();
		}
	}

	@Override
	public void visitSwitchStatement(SwitchStatement s, Boolean param) {
		startPosition(s);
		print("switch (");
		visitorSupport.accept(s.getExpression(), this, param);
		println(") {");
		indent();
		for (SwitchCase sc : s.getCases()) {
			visitorSupport.accept(sc, this, param);
		}
		unindent();
		println("}");

	}

	@Override
	public void visitTryStatement(TryStatement t, Boolean param) {
		startPosition(t);
		print("try ");
		visitorSupport.accept(t.getTryBlock(), this, param);
		for (CatchClause cc : t.getCatchClauses()) {
			visitorSupport.accept(cc, this, param);
		}
		if (t.getFinallyBlock() != null) {
			print(" finally ");
			visitorSupport.accept(t.getFinallyBlock(), this, param);
		}
		println();

	}

	@Override
	public void visitUnaryExpression(UnaryExpression u, Boolean param) {
		int type = u.getType();
		if (!u.isPostfix()) {
			print(AstNode.operatorToString(type));
			if (type == Token.TYPEOF || type == Token.DELPROP || type == Token.VOID) {
				print(" ");
			}
		}
		visitorSupport.accept(u.getOperand(), this, param);
		if (u.isPostfix()) {
			print(AstNode.operatorToString(type));
		}
	}

	@Override
	public void visitVariableDeclaration(VariableDeclaration v, Boolean param) {
		printComments(v);
		if (v.isStatement()) {
			startPosition(v);
		}
		print("var ");
		printList(v.getVariables(), param);
		if (v.isStatement()) {
			println(";");

		}
	}

	@Override
	public void visitVariableInitializer(VariableInitializer v, Boolean param) {
		visitorSupport.accept(v.getTarget(), this, param);
		if (v.getInitializer() != null) {
			print(" = ");
			visitorSupport.accept(v.getInitializer(), this, param);
		}
	}

	@Override
	public void visitWhileLoop(WhileLoop w, Boolean param) {
		startPosition(w);
		print(" while (");
		visitorSupport.accept(w.getCondition(), this, param);
		print(")");
		printStatementAsBlock(w.getBody(), param);

	}

	@Override
	public void visitThrowStatement(ThrowStatement e, Boolean param) {
		startPosition(e);
		print(" throw ");
		visitorSupport.accept(e.getExpression(), this, param);
		println(";");

	}

	public void addSourceMapURL() {
		if (generateSourceMap) {
			addMapping();
			print("//# sourceMappingURL=").print(inputFile.getName().replaceAll("\\.java$", ".map"));
		}
	}

	public SourceMapGenerator getSourceMapGenerator() {
		return sourceMapGenerator;
	}

	@Override
	public void visitCodeFragment(CodeFragment c, Boolean param) {
		if (c.getCode() != null) {
			print(c.getCode());
		}
	}

}
