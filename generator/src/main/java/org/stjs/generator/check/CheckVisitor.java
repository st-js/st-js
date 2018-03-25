package org.stjs.generator.check;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;

/**
 * This visitor will visit the Java AST to check for constructions that cannot be used because they don't have a
 * suitable JavaScript correspondent.
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class CheckVisitor extends TreePathScannerContributors<Void, GenerationContext<Void>, CheckVisitor> {
	/**
	 * <p>Constructor for CheckVisitor.</p>
	 */
	public CheckVisitor() {
		super();
		setContinueScanning(true);
	}

	/**
	 * <p>Constructor for CheckVisitor.</p>
	 *
	 * @param checkVisitor a {@link org.stjs.generator.check.CheckVisitor} object.
	 */
	public CheckVisitor(CheckVisitor checkVisitor) {
		super(checkVisitor);
	}
}
