package org.stjs.generator.check;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.declaration.ClassDuplicateMemberNameCheck;
import org.stjs.generator.check.declaration.FieldInitializerCheck;
import org.stjs.generator.check.declaration.MethodVarArgParamCheck;
import org.stjs.generator.check.expression.IdentifierAccessOuterScopeCheck;
import org.stjs.generator.check.statement.VariableFinalInLoopCheck;
import org.stjs.generator.check.statement.VariableWrongNameCheck;
import org.stjs.generator.visitor.TreePathScannerContributors;

public class CheckContributors {
	public static void addContributors(TreePathScannerContributors<Void, GenerationContext> contributors) {
		contributors.contribute(new VariableFinalInLoopCheck());
		contributors.contribute(new VariableWrongNameCheck());
		contributors.contribute(new MethodVarArgParamCheck());
		contributors.contribute(new FieldInitializerCheck());
		contributors.contribute(new ClassDuplicateMemberNameCheck());
		contributors.contribute(new IdentifierAccessOuterScopeCheck());
	}
}
