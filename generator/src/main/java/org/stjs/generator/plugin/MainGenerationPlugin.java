package org.stjs.generator.plugin;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.check.declaration.ArrayTypeForbiddenCheck;
import org.stjs.generator.check.declaration.ClassDuplicateMemberNameCheck;
import org.stjs.generator.check.declaration.ClassEnumWithoutMembersCheck;
import org.stjs.generator.check.declaration.ClassForbidClassInAnonymousClassCheck;
import org.stjs.generator.check.declaration.ClassForbidClassInInterfaceCheck;
import org.stjs.generator.check.declaration.ClassGlobalForbidInnerCheck;
import org.stjs.generator.check.declaration.ClassGlobalInstanceMembersCheck;
import org.stjs.generator.check.declaration.ClassImplementJavascriptFunctionCheck;
import org.stjs.generator.check.declaration.ClassNamespaceCheck;
import org.stjs.generator.check.declaration.FieldInitializerCheck;
import org.stjs.generator.check.declaration.MethodDeclarationTemplateCheck;
import org.stjs.generator.check.declaration.MethodOverloadCheck;
import org.stjs.generator.check.declaration.MethodSynchronizedCheck;
import org.stjs.generator.check.declaration.MethodVarArgParamCheck;
import org.stjs.generator.check.declaration.MethodWithBodyInInterfaceCheck;
import org.stjs.generator.check.declaration.MethodWrongNameCheck;
import org.stjs.generator.check.expression.IdentifierAccessOuterScopeCheck;
import org.stjs.generator.check.expression.IdentifierAccessServerSideCheck;
import org.stjs.generator.check.expression.IdentifierGlobalScopeNameClashCheck;
import org.stjs.generator.check.expression.InstanceOfInterfaceCheck;
import org.stjs.generator.check.expression.MemberSelectGlobalScopeNameClashCheck;
import org.stjs.generator.check.expression.MemberSelectOuterScopeCheck;
import org.stjs.generator.check.expression.MemberSelectServerSideCheck;
import org.stjs.generator.check.expression.MethodInvocationAnnotationsCheck;
import org.stjs.generator.check.expression.MethodInvocationEnumCheck;
import org.stjs.generator.check.expression.MethodInvocationMapConstructorCheck;
import org.stjs.generator.check.expression.MethodInvocationOuterScopeCheck;
import org.stjs.generator.check.expression.MethodInvocationServerSideCheck;
import org.stjs.generator.check.expression.MethodInvocationSuperSynthCheck;
import org.stjs.generator.check.expression.NewArrayForbiddenCheck;
import org.stjs.generator.check.expression.NewClassInlineFunctionCheck;
import org.stjs.generator.check.expression.NewClassObjectInitCheck;
import org.stjs.generator.check.statement.AssertCheck;
import org.stjs.generator.check.statement.BlockInstanceCheck;
import org.stjs.generator.check.statement.SynchronizedCheck;
import org.stjs.generator.check.statement.VariableFinalInLoopCheck;
import org.stjs.generator.check.statement.VariableWrongNameCheck;
import org.stjs.generator.visitor.DiscriminatorKey;
import org.stjs.generator.writer.CommentWriter;
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
import org.stjs.generator.writer.statement.ThrowWriter;
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
import org.stjs.generator.writer.templates.SuffixTemplate;
import org.stjs.generator.writer.templates.TypeOfTemplate;
import org.stjs.generator.writer.templates.fields.PathGetterMemberSelectTemplate;
import org.stjs.generator.writer.templates.fields.DefaultAssignmentTemplate;
import org.stjs.generator.writer.templates.fields.DefaultCompoundAssignmentTemplate;
import org.stjs.generator.writer.templates.fields.DefaultIdentifierTemplate;
import org.stjs.generator.writer.templates.fields.DefaultMemberSelectTemplate;
import org.stjs.generator.writer.templates.fields.DefaultUnaryTemplate;
import org.stjs.generator.writer.templates.fields.GetterIdentifierTemplate;
import org.stjs.generator.writer.templates.fields.GetterMemberSelectTemplate;
import org.stjs.generator.writer.templates.fields.GlobalGetterIdentifierTemplate;
import org.stjs.generator.writer.templates.fields.GlobalGetterMemberSelectTemplate;
import org.stjs.generator.writer.templates.fields.GlobalSetterAssignmentTemplate;
import org.stjs.generator.writer.templates.fields.GlobalSetterCompoundAssignmentTemplate;
import org.stjs.generator.writer.templates.fields.GlobalSetterUnaryTemplate;
import org.stjs.generator.writer.templates.fields.SetterAssignmentTemplate;
import org.stjs.generator.writer.templates.fields.SetterCompoundAssignmentTemplate;
import org.stjs.generator.writer.templates.fields.SetterUnaryTemplate;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;

/**
 * this is the main generation plugin that adds all the needed checks and writers.
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class MainGenerationPlugin<JS> implements STJSGenerationPlugin<JS> {

	/**
	 * <p>newContext.</p>
	 *
	 * @return a {@link org.stjs.generator.GenerationContext} object.
	 */
	public GenerationContext<JS> newContext() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void contributeCheckVisitor(CheckVisitor visitor) {
		visitor.contribute(new VariableFinalInLoopCheck());
		visitor.contribute(new VariableWrongNameCheck());
		visitor.contribute(new MethodVarArgParamCheck());
		visitor.contribute(new MethodWithBodyInInterfaceCheck());
		visitor.contribute(new FieldInitializerCheck());
		visitor.contribute(new ClassDuplicateMemberNameCheck());
		visitor.contribute(new NewClassInlineFunctionCheck());
		visitor.contribute(new ClassImplementJavascriptFunctionCheck());
		visitor.contribute(new ClassGlobalInstanceMembersCheck());
		visitor.contribute(new ClassNamespaceCheck());
		visitor.contribute(new MethodOverloadCheck());

		visitor.contribute(new NewClassObjectInitCheck());
		visitor.contribute(new ArrayTypeForbiddenCheck());
		visitor.contribute(new ClassEnumWithoutMembersCheck());
		visitor.contribute(new ClassForbidClassInAnonymousClassCheck());
		visitor.contribute(new ClassForbidClassInInterfaceCheck());
		visitor.contribute(new NewArrayForbiddenCheck());

		visitor.contribute(new BlockInstanceCheck());
		visitor.contribute(new MethodInvocationMapConstructorCheck());
		visitor.contribute(new MethodInvocationEnumCheck());
		visitor.contribute(new MethodInvocationAnnotationsCheck());
		visitor.contribute(new SynchronizedCheck());
		visitor.contribute(new MethodSynchronizedCheck());

		visitor.contribute(new AssertCheck());
		visitor.contribute(new IdentifierGlobalScopeNameClashCheck());
		visitor.contribute(new InstanceOfInterfaceCheck());
		visitor.contribute(new MemberSelectGlobalScopeNameClashCheck());

		visitor.contribute(new MethodDeclarationTemplateCheck());

		visitor.contribute(new IdentifierAccessOuterScopeCheck());
		visitor.contribute(new MethodInvocationOuterScopeCheck());
		visitor.contribute(new MemberSelectOuterScopeCheck());

		visitor.contribute(new ClassGlobalForbidInnerCheck());
		visitor.contribute(new MethodInvocationSuperSynthCheck());

		visitor.contribute(new IdentifierAccessServerSideCheck());
		visitor.contribute(new MemberSelectServerSideCheck());
		visitor.contribute(new MethodInvocationServerSideCheck());
		visitor.contribute(new MethodWrongNameCheck());

	}

	/** {@inheritDoc} */
	@Override
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
		visitor.contribute(new ThrowWriter<JS>());

		addMethodCallTemplates(visitor);
		addFieldTemplates(visitor);
		addJavaDocCommentFilter(visitor);
	}

	private void addJavaDocCommentFilter(WriterVisitor<JS> visitor) {
		CommentWriter<JS> cw = new CommentWriter<JS>();
		visitor.addFilter(cw, ClassTree.class);
		visitor.addFilter(cw, MethodTree.class);
		visitor.addFilter(cw, VariableTree.class);
	}

	private DiscriminatorKey template(String name) {
		return DiscriminatorKey.of(MethodInvocationWriter.class.getSimpleName(), name);
	}

	private DiscriminatorKey assignTemplate(String name) {
		return DiscriminatorKey.of(AssignmentWriter.class.getSimpleName(), name);
	}

	private DiscriminatorKey unaryTemplate(String name) {
		return DiscriminatorKey.of(UnaryWriter.class.getSimpleName(), name);
	}

	private DiscriminatorKey compoundAssignTemplate(String name) {
		return DiscriminatorKey.of(CompoundAssignmentWriter.class.getSimpleName(), name);
	}

	private DiscriminatorKey identifierTemplate(String name) {
		return DiscriminatorKey.of(IdentifierWriter.class.getSimpleName(), name);
	}

	private DiscriminatorKey memberSelectTemplate(String name) {
		return DiscriminatorKey.of(MemberSelectWriter.class.getSimpleName(), name);
	}

	/**
	 * <p>addMethodCallTemplates.</p>
	 *
	 * @param visitor a {@link org.stjs.generator.writer.WriterVisitor} object.
	 */
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
		visitor.contribute(template("suffix"), new SuffixTemplate<JS>());
		visitor.contribute(template("properties"), new PropertiesTemplate<JS>());
		visitor.contribute(template("put"), new PutTemplate<JS>());
		visitor.contribute(template("set"), new SetTemplate<JS>());
		visitor.contribute(template("typeOf"), new TypeOfTemplate<JS>());
		visitor.contribute(template("assert"), new AssertTemplate<JS>());
		visitor.contribute(template("none"), new DefaultTemplate<JS>());
	}

	/**
	 * <p>addFieldTemplates.</p>
	 *
	 * @param visitor a {@link org.stjs.generator.writer.WriterVisitor} object.
	 */
	protected void addFieldTemplates(WriterVisitor<JS> visitor) {
		String none = "none";
		String property = "property";
		String gproperty = "gproperty";
		String path = "path";

		visitor.contribute(assignTemplate(none), new DefaultAssignmentTemplate<JS>());
		visitor.contribute(assignTemplate(property), new SetterAssignmentTemplate<JS>());
		visitor.contribute(assignTemplate(gproperty), new GlobalSetterAssignmentTemplate<JS>());

		visitor.contribute(unaryTemplate(none), new DefaultUnaryTemplate<JS>());
		visitor.contribute(unaryTemplate(property), new SetterUnaryTemplate<JS>());
		visitor.contribute(unaryTemplate(gproperty), new GlobalSetterUnaryTemplate<JS>());

		visitor.contribute(compoundAssignTemplate(none), new DefaultCompoundAssignmentTemplate<JS>());
		visitor.contribute(compoundAssignTemplate(property), new SetterCompoundAssignmentTemplate<JS>());
		visitor.contribute(compoundAssignTemplate(gproperty), new GlobalSetterCompoundAssignmentTemplate<JS>());

		visitor.contribute(identifierTemplate(none), new DefaultIdentifierTemplate<JS>());
		visitor.contribute(identifierTemplate(property), new GetterIdentifierTemplate<JS>());
		visitor.contribute(identifierTemplate(gproperty), new GlobalGetterIdentifierTemplate<JS>());

		visitor.contribute(memberSelectTemplate(none), new DefaultMemberSelectTemplate<JS>());
		visitor.contribute(memberSelectTemplate(property), new GetterMemberSelectTemplate<JS>());
		visitor.contribute(memberSelectTemplate(gproperty), new GlobalGetterMemberSelectTemplate<JS>());
		visitor.contribute(memberSelectTemplate(path), new PathGetterMemberSelectTemplate<JS>());
	}

	/** {@inheritDoc} */
	@Override
	public boolean loadByDefault() {
		return true;
	}
}
