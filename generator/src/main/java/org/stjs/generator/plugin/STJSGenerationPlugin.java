package org.stjs.generator.plugin;

import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.writer.WriterVisitor;

/**
 *
 * See {@link org.stjs.javascript.annotation.UsePlugin} for details on how to list your plugin.
 *
 * @author acraciun
 * @param <JS>
 *            - the way JavaScript nodes are represented. For example Rhino AstNode
 * @version $Id: $Id
 */
public interface STJSGenerationPlugin<JS> {
	/**
	 * <p>loadByDefault.</p>
	 *
	 * @return true if this plugin is to be executed with any class, or false if the user should activate it via
	 *         {@link org.stjs.javascript.annotation.UsePlugin}
	 */
	boolean loadByDefault();

	/**
	 * add the check contributors
	 *
	 * @param visitor a {@link org.stjs.generator.check.CheckVisitor} object.
	 */
	void contributeCheckVisitor(CheckVisitor visitor);

	/**
	 * add the writer contributors
	 *
	 * @param visitor a {@link org.stjs.generator.writer.WriterVisitor} object.
	 */
	void contributeWriteVisitor(WriterVisitor<JS> visitor);

}
