package org.stjs.generator.plugin;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.check.declaration.ArrayTypeForbiddenCheck;
import org.stjs.generator.check.declaration.ClassDuplicateMemberNameCheck;
import org.stjs.generator.check.declaration.ClassEnumWithoutMembersCheck;
import org.stjs.generator.check.declaration.ClassForbidExtendsSyntheticTypeCheck;
import org.stjs.generator.check.declaration.ClassGlobalInstanceMembersCheck;
import org.stjs.generator.check.declaration.ClassImplementJavascriptFunctionCheck;
import org.stjs.generator.check.declaration.ClassNamespaceCheck;
import org.stjs.generator.check.declaration.FieldInitializerCheck;
import org.stjs.generator.check.declaration.MethodOverloadCheck;
import org.stjs.generator.check.declaration.MethodSynchronizedCheck;
import org.stjs.generator.check.declaration.MethodVarArgParamCheck;
import org.stjs.generator.check.expression.IdentifierAccessOuterScopeCheck;
import org.stjs.generator.check.expression.IdentifierGlobalScopeNameClashCheck;
import org.stjs.generator.check.expression.MemberSelectGlobalScopeNameClashCheck;
import org.stjs.generator.check.expression.MemberSelectOuterScopeCheck;
import org.stjs.generator.check.expression.MethodInvocationMapConstructorCheck;
import org.stjs.generator.check.expression.MethodInvocationOuterScopeCheck;
import org.stjs.generator.check.expression.NewArrayForbiddenCheck;
import org.stjs.generator.check.expression.NewClassInlineFunctionCheck;
import org.stjs.generator.check.expression.NewClassObjectInitCheck;
import org.stjs.generator.check.statement.AssertCheck;
import org.stjs.generator.check.statement.BlockInstanceCheck;
import org.stjs.generator.check.statement.SynchronizedCheck;
import org.stjs.generator.check.statement.VariableFinalInLoopCheck;
import org.stjs.generator.check.statement.VariableWrongNameCheck;
import org.stjs.generator.visitor.DiscriminatorKey;
import org.stjs.generator.writer.CompilationUnitWriter;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.declaration.ClassWriter;
import org.stjs.generator.writer.declaration.MethodWriter;
import org.stjs.generator.writer.expression.ArrayAccessWriter;
import org.stjs.generator.writer.expression.AssignmentWriter;
import org.stjs.generator.writer.expression.BinaryWriter;
import org.stjs.generator.writer.expression.CompoundAssignmentWriter;
import org.stjs.generator.writer.expression.ConditionalWriter;
import org.stjs.generator.writer.expression.IdentifierWriter;
import org.stjs.generator.writer.expression.InstanceofWriter;
import org.stjs.generator.writer.expression.LiteralWriter;
import org.stjs.generator.writer.expression.MemberSelectWriter;
import org.stjs.generator.writer.expression.MethodInvocationWriter;
import org.stjs.generator.writer.expression.NewArrayWriter;
import org.stjs.generator.writer.expression.NewClassWriter;
import org.stjs.generator.writer.expression.ParenthesizedWriter;
import org.stjs.generator.writer.expression.TypeCastWriter;
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
import org.stjs.generator.writer.templates.AdapterTemplate;
import org.stjs.generator.writer.templates.ArrayTemplate;
import org.stjs.generator.writer.templates.AssertTemplate;
import org.stjs.generator.writer.templates.DefaultTemplate;
import org.stjs.generator.writer.templates.DeleteTemplate;
import org.stjs.generator.writer.templates.GetTemplate;
import org.stjs.generator.writer.templates.InvokeTemplate;
import org.stjs.generator.writer.templates.JsTemplate;
import org.stjs.generator.writer.templates.MapTemplate;
import org.stjs.generator.writer.templates.MethodToPropertyTemplate;
import org.stjs.generator.writer.templates.OrTemplate;
import org.stjs.generator.writer.templates.PrefixTemplate;
import org.stjs.generator.writer.templates.PropertiesTemplate;
import org.stjs.generator.writer.templates.PutTemplate;
import org.stjs.generator.writer.templates.SetTemplate;
import org.stjs.generator.writer.templates.TypeOfTemplate;

/**
 * this is the main generation plugin that adds all the needed checks and writers.
 * 
 * @author acraciun
 */
public class MainGenerationPlugin<JS> implements STJSGenerationPlugin<JS> {

	public GenerationContext<JS> newContext() {
		return null;
	}

	public void contributeCheckVisitor(CheckVisitor visitor) {
		visitor.contribute(new VariableFinalInLoopCheck());
		visitor.contribute(new VariableWrongNameCheck());
		visitor.contribute(new MethodVarArgParamCheck());
		visitor.contribute(new FieldInitializerCheck());
		visitor.contribute(new ClassDuplicateMemberNameCheck());
		visitor.contribute(new IdentifierAccessOuterScopeCheck());
		visitor.contribute(new NewClassInlineFunctionCheck());
		visitor.contribute(new ClassImplementJavascriptFunctionCheck());
		visitor.contribute(new ClassGlobalInstanceMembersCheck());
		visitor.contribute(new ClassNamespaceCheck());
		visitor.contribute(new MethodOverloadCheck());
		visitor.contribute(new NewClassObjectInitCheck());
		visitor.contribute(new ClassForbidExtendsSyntheticTypeCheck());
		visitor.contribute(new MethodInvocationOuterScopeCheck());
		visitor.contribute(new MemberSelectOuterScopeCheck());
		visitor.contribute(new ArrayTypeForbiddenCheck());
		visitor.contribute(new ClassEnumWithoutMembersCheck());
		visitor.contribute(new NewArrayForbiddenCheck());
		visitor.contribute(new BlockInstanceCheck());
		visitor.contribute(new MethodInvocationMapConstructorCheck());
		visitor.contribute(new SynchronizedCheck());
		visitor.contribute(new MethodSynchronizedCheck());
		visitor.contribute(new AssertCheck());

		visitor.contribute(new IdentifierGlobalScopeNameClashCheck());
		visitor.contribute(new MemberSelectGlobalScopeNameClashCheck());
	}

	public void contributeWriteVisitor(WriterVisitor<JS> visitor) {
		visitor.contribute(new CompilationUnitWriter<JS>());

		visitor.contribute(new ClassWriter<JS>());
		visitor.contribute(new MethodWriter<JS>());

		visitor.contribute(new ArrayAccessWriter<JS>());
		visitor.contribute(new AssignmentWriter<JS>());
		visitor.contribute(new BinaryWriter<JS>());
		visitor.contribute(new CompoundAssignmentWriter<JS>());
		visitor.contribute(new ConditionalWriter<JS>());
		visitor.contribute(new IdentifierWriter<JS>());
		visitor.contribute(new InstanceofWriter<JS>());
		visitor.contribute(new LiteralWriter<JS>());
		visitor.contribute(new MemberSelectWriter<JS>());
		visitor.contribute(new MethodInvocationWriter<JS>());
		visitor.contribute(new NewArrayWriter<JS>());
		visitor.contribute(new NewClassWriter<JS>());
		visitor.contribute(new ParenthesizedWriter<JS>());
		visitor.contribute(new TypeCastWriter<JS>());
		visitor.contribute(new UnaryWriter<JS>());

		visitor.contribute(new AssertWriter<JS>());
		visitor.contribute(new BlockWriter<JS>());
		visitor.contribute(new BreakWriter<JS>());
		visitor.contribute(new CaseWriter<JS>());
		visitor.contribute(new CatchWriter<JS>());
		visitor.contribute(new ContinueWriter<JS>());
		visitor.contribute(new DoWhileLoopWriter<JS>());
		visitor.contribute(new EmptyStatementWriter<JS>());
		visitor.contribute(new EnhancedForLoopWriter<JS>());
		visitor.contribute(new ExpressionStatementWriter<JS>());
		visitor.contribute(new ForLoopWriter<JS>());
		visitor.contribute(new IfWriter<JS>());
		visitor.contribute(new LabeledStatementWriter<JS>());
		visitor.contribute(new ReturnWriter<JS>());
		visitor.contribute(new SwitchWriter<JS>());
		visitor.contribute(new SynchronizedWriter<JS>());
		visitor.contribute(new TryWriter<JS>());
		visitor.contribute(new VariableWriter<JS>());
		visitor.contribute(new WhileLoopWriter<JS>());

		addMethodCallTemplates(visitor);
	}

	private DiscriminatorKey template(String name) {
		return DiscriminatorKey.of(MethodInvocationWriter.class.getSimpleName(), name);
	}

	protected void addMethodCallTemplates(WriterVisitor<JS> visitor) {
		visitor.contribute(template("adapter"), new AdapterTemplate<JS>());
		visitor.contribute(template("array"), new ArrayTemplate<JS>());
		visitor.contribute(template("delete"), new DeleteTemplate<JS>());
		visitor.contribute(template("get"), new GetTemplate<JS>());
		visitor.contribute(template("invoke"), new InvokeTemplate<JS>());
		visitor.contribute(template("js"), new JsTemplate<JS>());
		visitor.contribute(template("map"), new MapTemplate<JS>());
		visitor.contribute(template("toProperty"), new MethodToPropertyTemplate<JS>());
		visitor.contribute(template("or"), new OrTemplate<JS>());
		visitor.contribute(template("prefix"), new PrefixTemplate<JS>());
		visitor.contribute(template("properties"), new PropertiesTemplate<JS>());
		visitor.contribute(template("put"), new PutTemplate<JS>());
		visitor.contribute(template("set"), new SetTemplate<JS>());
		visitor.contribute(template("typeOf"), new TypeOfTemplate<JS>());
		visitor.contribute(template("assert"), new AssertTemplate<JS>());
		visitor.contribute(template("none"), new DefaultTemplate<JS>());
	}

	@Override
	public boolean loadByDefault() {
		return true;
	}
}
