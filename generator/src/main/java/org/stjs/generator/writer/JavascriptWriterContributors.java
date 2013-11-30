package org.stjs.generator.writer;

import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.writer.declaration.ClassWriter;
import org.stjs.generator.writer.declaration.MethodWriter;
import org.stjs.generator.writer.expression.ArrayAccessWriter;
import org.stjs.generator.writer.expression.AssignmentWriter;
import org.stjs.generator.writer.expression.BinaryWriter;
import org.stjs.generator.writer.expression.CompoundAssignmentWriter;
import org.stjs.generator.writer.expression.ConditionalWriter;
import org.stjs.generator.writer.expression.IdentifierWriter;
import org.stjs.generator.writer.expression.LiteralWriter;
import org.stjs.generator.writer.expression.MethodInvocationWriter;
import org.stjs.generator.writer.expression.NewArrayWriter;
import org.stjs.generator.writer.expression.NewClassWriter;
import org.stjs.generator.writer.expression.ParenthesizedWriter;
import org.stjs.generator.writer.expression.UnaryWriter;
import org.stjs.generator.writer.statement.AssertWriter;
import org.stjs.generator.writer.statement.BlockWriter;
import org.stjs.generator.writer.statement.BreakWriter;
import org.stjs.generator.writer.statement.CaseWriter;
import org.stjs.generator.writer.statement.CatchWriter;
import org.stjs.generator.writer.statement.ContinueWriter;
import org.stjs.generator.writer.statement.DoWhileLoopWriter;
import org.stjs.generator.writer.statement.EmptyStatementWriter;
import org.stjs.generator.writer.statement.EnhancedForLoopWriter;
import org.stjs.generator.writer.statement.ExpressionStatementWriter;
import org.stjs.generator.writer.statement.ForLoopWriter;
import org.stjs.generator.writer.statement.IfWriter;
import org.stjs.generator.writer.statement.LabeledStatementWriter;
import org.stjs.generator.writer.statement.ReturnWriter;
import org.stjs.generator.writer.statement.SwitchWriter;
import org.stjs.generator.writer.statement.SynchronizedWriter;
import org.stjs.generator.writer.statement.TryWriter;
import org.stjs.generator.writer.statement.VariableWriter;
import org.stjs.generator.writer.statement.WhileLoopWriter;

/**
 * this class adds all the contributors that will generator JavaScript AST nodes out of Java AST nodes
 * 
 * @author acraciun
 * 
 */
public class JavascriptWriterContributors {
	public static void addContributors(TreePathScannerContributors<List<AstNode>, GenerationContext> contributors) {
		contributors.contribute(new CompilationUnitWriter());

		contributors.contribute(new ClassWriter());
		contributors.contribute(new MethodWriter());

		contributors.contribute(new ArrayAccessWriter());
		contributors.contribute(new AssignmentWriter());
		contributors.contribute(new BinaryWriter());
		contributors.contribute(new CompoundAssignmentWriter());
		contributors.contribute(new ConditionalWriter());
		contributors.contribute(new IdentifierWriter());
		contributors.contribute(new LiteralWriter());
		contributors.contribute(new MethodInvocationWriter());
		contributors.contribute(new NewArrayWriter());
		contributors.contribute(new NewClassWriter());
		contributors.contribute(new ParenthesizedWriter());
		contributors.contribute(new UnaryWriter());

		contributors.contribute(new AssertWriter());
		contributors.contribute(new BlockWriter());
		contributors.contribute(new BreakWriter());
		contributors.contribute(new CaseWriter());
		contributors.contribute(new CatchWriter());
		contributors.contribute(new ContinueWriter());
		contributors.contribute(new DoWhileLoopWriter());
		contributors.contribute(new EmptyStatementWriter());
		contributors.contribute(new EnhancedForLoopWriter());
		contributors.contribute(new ExpressionStatementWriter());
		contributors.contribute(new ForLoopWriter());
		contributors.contribute(new IfWriter());
		contributors.contribute(new LabeledStatementWriter());
		contributors.contribute(new ReturnWriter());
		contributors.contribute(new SwitchWriter());
		contributors.contribute(new SynchronizedWriter());
		contributors.contribute(new TryWriter());
		contributors.contribute(new VariableWriter());
		contributors.contribute(new WhileLoopWriter());

	}
}
