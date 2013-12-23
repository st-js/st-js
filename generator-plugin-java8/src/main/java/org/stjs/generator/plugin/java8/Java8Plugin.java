package org.stjs.generator.plugin.java8;

import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.plugin.STJSGenerationPlugin;
import org.stjs.generator.plugin.java8.writer.expression.LambdaExpressionWriter;
import org.stjs.generator.writer.WriterVisitor;

public class Java8Plugin<JS> implements STJSGenerationPlugin<JS> {

	@Override
	public void contributeCheckVisitor(CheckVisitor visitor) {
		// nothing to add
	}

	@Override
	public void contributeWriteVisitor(WriterVisitor<JS> visitor) {
		visitor.contribute(new LambdaExpressionWriter<>());
	}

}
