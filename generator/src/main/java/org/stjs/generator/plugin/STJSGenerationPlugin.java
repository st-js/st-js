package org.stjs.generator.plugin;

import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.writer.WriterVisitor;

public interface STJSGenerationPlugin<JS> {
	void contributeCheckVisitor(CheckVisitor visitor);

	void contributeWriteVisitor(WriterVisitor<JS> visitor);
}
