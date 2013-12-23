package org.stjs.generator.check;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;

/**
 * This visitor will visit the Java AST to check for constructions that cannot be used because they don't have a
 * suitable JavaScript correspondent.
 * 
 * @author acraciun
 */
public class CheckVisitor extends TreePathScannerContributors<Void, GenerationContext<Void>, CheckVisitor> {
	public CheckVisitor() {
		super();
		setContinueScanning(true);
	}
}
