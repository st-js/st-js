package org.stjs.generator.name;

import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import com.sun.source.tree.MemberSelectTree;
import org.stjs.generator.GenerationContext;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;

public interface JavaScriptNameProvider {
	String getTypeName(GenerationContext<?> context, TypeMirror type, DependencyType dependencyType);

	String getTypeName(GenerationContext<?> context, Element type, DependencyType dependencyType);

	String getMethodName(GenerationContext<?> context, MethodTree tree);

	String getMethodName(GenerationContext<?> context, MethodInvocationTree tree);

	String getMethodName(GenerationContext<?> context, ExecutableElement method);

	String transformMethodCallToFieldName(GenerationContext<?> context, MethodInvocationTree tree);

	Map<String, DependencyType> getResolvedTypes();

}
