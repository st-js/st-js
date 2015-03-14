package org.stjs.generator.check.declaration;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.writer.JavascriptKeywords;

import com.sun.source.tree.ClassTree;

/**
 * this check makes sure the names used to define a namespace are JavaScript correctly defined variables, as they will
 * be used as such
 *
 * @author acraciun
 */
public class ClassNamespaceCheck implements CheckContributor<ClassTree> {

	@Override
	public Void visit(CheckVisitor visitor, ClassTree tree, GenerationContext<Void> context) {
		String namespace = context.getCurrentWrapper().getNamespace();
		if (!namespace.isEmpty()) {
			if (!GeneratorConstants.NAMESPACE_PATTERN.matcher(namespace).matches()) {
				context.addError(tree, "The namespace must be in the form <identifier>[.<identifier>]..");
			}
			String[] identifiers = namespace.split("\\.");
			for (String identifier : identifiers) {
				if (JavascriptKeywords.isReservedWord(identifier)) {
					context.addError(tree, "Identifier \"" + identifier + "\" cannot be used as part of a namespace, "
							+ "because it is a javascript keyword or a javascript reserved word");
				}
			}
		}
		return null;
	}
}
