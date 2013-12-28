package org.stjs.generator.writer.plugins;

import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.plugin.STJSGenerationPlugin;
import org.stjs.generator.writer.WriterVisitor;

public class AddContributorPlugin<JS> implements STJSGenerationPlugin<JS> {

	@Override
	public boolean loadByDefault() {
		return false;
	}

	@Override
	public void contributeCheckVisitor(CheckVisitor visitor) {
	}

	@Override
	public void contributeWriteVisitor(WriterVisitor<JS> visitor) {
		visitor.contribute(new ReplaceBinaryWriter<JS>());
	}

}
