package org.stjs.generator.writer;

import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;

/**
 * this class adds all the contributors that will generator JavaScript AST nodes out of Java AST nodes
 * 
 * @author acraciun
 * 
 */
public class JavascriptWriterContributors {
	public static void addContributors(TreePathScannerContributors<List<AstNode>, GenerationContext> contributors) {
		contributors.contribute(new ArrayAccessWriter());
		contributors.contribute(new ClassWriter());
		contributors.contribute(new CompilationUnitWriter());
	}
}
