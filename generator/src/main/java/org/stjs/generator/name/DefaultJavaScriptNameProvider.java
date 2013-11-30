package org.stjs.generator.name;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;

public class DefaultJavaScriptNameProvider implements JavaScriptNameProvider {

	@Override
	public String getTypeName(GenerationContext context, TypeMirror type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVariableName(GenerationContext context, IdentifierTree treeNode, TreePath path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMethodName(GenerationContext context, MethodTree tree, TreePath path) {
		return tree.getName().toString();
	}

	@Override
	public String getMethodName(GenerationContext context, MethodInvocationTree tree, TreePath path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTypeName(GenerationContext context, Element type) {
		// TODO to this correctly
		return type.getSimpleName().toString();
	}

}
