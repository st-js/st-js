package org.stjs.generator.writer;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;

/**
 * This class visits the Java AST and calls the corresponding writers to generate JavaScript.
 * 
 * @author acraciun
 */
public class WriterVisitor<R> extends TreePathScannerContributors<R, GenerationContext<R>, WriterVisitor<R>> {
	public WriterVisitor() {
		super();
		// setting another contributor for a node would replace the existing one
		setOnlyOneFinalContributor(true);
	}

	public WriterVisitor(WriterVisitor<R> writerVisitor) {
		super(writerVisitor);
	}
}
