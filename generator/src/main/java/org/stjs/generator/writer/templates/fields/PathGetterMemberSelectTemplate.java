package org.stjs.generator.writer.templates.fields;

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.STJSRuntimeException;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.Tree;

/**
 * <pre>
 * public class FieldTemplates1 {
 * 	public static class Inner {
 * 		&#064;Template(&quot;path&quot;)
 * 		public String field;
 * 	}
 * 
 * 	&#064;Template(&quot;path(method)&quot;)
 * 	public Inner inner;
 * 
 * 	public String method(String param) {
 * 		return &quot;&quot;;
 * 	}
 * 
 * 	public void other() {
 * 		String s = this.inner.field;
 * 
 * 	}
 * }
 * </pre>
 *
 * should generate
 *
 * <pre>
 * 	this.method(\"inner.field\")
 * </pre>
 *
 * @author acraciun
 * @param <JS>
 */
public class PathGetterMemberSelectTemplate<JS> implements WriterContributor<MemberSelectTree, JS> {

	private JS getTarget(WriterVisitor<JS> visitor, ExpressionTree tree, GenerationContext<JS> context) {
		if (tree instanceof MemberSelectTree) {
			MemberSelectTree ms = (MemberSelectTree) tree;
			if (JavaNodes.isSuper(ms.getExpression())) {
				// super.field does not make sense, so convert it to this
				return context.js().keyword(Keyword.THIS);
			}

			TreeWrapper<IdentifierTree, JS> tw = context.getCurrentWrapper();
			Tree target = ms.getExpression();
			JS targetJS = visitor.scan(target, context);
			if (tw.isStatic() && !ElementUtils.isTypeKind(tw.child(target).getElement())) {
				// this is static method called from an instances: e.g. x.staticField
				targetJS = tw.getContext().js().property(targetJS, JavascriptKeywords.CONSTRUCTOR);
			}
			return targetJS;
		}

		return MemberWriters.buildTarget(context.getCurrentWrapper());
	}

	private String ident(Tree tree) {
		if (tree instanceof IdentifierTree) {
			return ((IdentifierTree) tree).getName().toString();
		}
		if (tree instanceof MemberSelectTree) {
			return ((MemberSelectTree) tree).getIdentifier().toString();
		}
		// in fact nothing would be
		throw new STJSRuntimeException("Unexpected node type while building path:" + tree);
	}

	private JS getPath(WriterVisitor<JS> visitor, TreeWrapper<ExpressionTree, JS> tree, GenerationContext<JS> context) {
		String path = "";
		TreeWrapper<ExpressionTree, JS> currentWrapper = tree;
		String[] params = null;
		/**
		 * this is true if the last member in the member select chain is also part of the path. so it's true for an
		 * expression like "path1.path2.path3" but false for "nonpath.path1.path2.path3"
		 */
		boolean targetIsPartOfPath = false;
		while (true) {
			Tree currentTree = currentWrapper.getTree();
			String template = currentWrapper.getFieldTemplate();
			if (!"path".equals(template)) {
				targetIsPartOfPath = false;
				break;
			}
			params = currentWrapper.getFieldTemplateParameters();
			path = ident(currentTree) + (path.length() > 0 ? "." + path : "");

			if (!(currentTree instanceof MemberSelectTree)) {
				targetIsPartOfPath = true;
				break;
			}
			currentWrapper = currentWrapper.child(((MemberSelectTree) currentTree).getExpression());
		}

		JS target = targetIsPartOfPath ? getTarget(visitor, currentWrapper.getTree(), context) : visitor.scan(currentWrapper.getTree(), context);

		if (params == null || params.length == 0) {
			context.addError(currentWrapper.getTree(),
					"The 'path' template needs a parameter designating the method to be called, like this @Template(\"path(methodName)\")");
			return null;
		}

		String methodName = params[0];

		JS pathString = context.js().string(path);
		List<JS> arguments = Collections.singletonList(pathString);

		return context.js().functionCall(context.js().property(target, methodName), arguments);
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MemberSelectTree tree, GenerationContext<JS> context) {
		// this is only for fields.
		TreeWrapper<ExpressionTree, JS> tw = context.getCurrentWrapper();
		Element element = tw.getElement();
		if (element == null || element.getKind() == ElementKind.PACKAGE) {
			// package names are ignored
			return null;
		}

		return getPath(visitor, tw, context);
	}
}
