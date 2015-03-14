package org.stjs.generator.name;

import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;

public interface JavaScriptNameProvider {
	String getTypeName(GenerationContext<?> context, TypeMirror type, DependencyType dependencyType);

	String getTypeName(GenerationContext<?> context, Element type, DependencyType dependencyType);

	String getVariableName(GenerationContext<?> context, IdentifierTree treeNode, TreePath path);

	String getMethodName(GenerationContext<?> context, MethodTree tree, TreePath path);

	String getMethodName(GenerationContext<?> context, MethodInvocationTree tree, TreePath path);

	Map<String, DependencyType> getResolvedTypes();
}
