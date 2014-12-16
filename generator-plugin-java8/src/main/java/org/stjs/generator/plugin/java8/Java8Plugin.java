package org.stjs.generator.plugin.java8;

import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.plugin.STJSGenerationPlugin;
import org.stjs.generator.plugin.java8.check.expression.FieldAccessFromLambdaCheck;
import org.stjs.generator.plugin.java8.check.expression.LambdaAccessFinalInLoopCheck;
import org.stjs.generator.plugin.java8.check.expression.MethodAccessFromLambdaCheck;
import org.stjs.generator.plugin.java8.writer.expression.LambdaExpressionWriter;
import org.stjs.generator.plugin.java8.writer.expression.MemberReferenceWriter;
import org.stjs.generator.writer.WriterVisitor;

public class Java8Plugin<JS> implements STJSGenerationPlugin<JS> {
	@Override
	public boolean loadByDefault() {
		return true;
	}

	@Override
	public void contributeCheckVisitor(CheckVisitor visitor) {
		visitor.contribute(new FieldAccessFromLambdaCheck());
		visitor.contribute(new MethodAccessFromLambdaCheck());
		visitor.contribute(new LambdaAccessFinalInLoopCheck());
	}

	@Override
	public void contributeWriteVisitor(WriterVisitor<JS> visitor) {
		visitor.contribute(new LambdaExpressionWriter<>());
		visitor.contribute(new MemberReferenceWriter<>());
	}

}
