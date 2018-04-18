package org.stjs.generator.name;

import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;

import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;

/**
 * <p>JavaScriptNameProvider interface.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public interface JavaScriptNameProvider {
	/**
	 * <p>getTypeName.</p>
	 *
	 * @param context a {@link org.stjs.generator.GenerationContext} object.
	 * @param type a {@link javax.lang.model.type.TypeMirror} object.
	 * @param dependencyType a {@link org.stjs.generator.name.DependencyType} object.
	 * @return a {@link java.lang.String} object.
	 */
	String getTypeName(GenerationContext<?> context, TypeMirror type, DependencyType dependencyType);

	/**
	 * <p>getTypeName.</p>
	 *
	 * @param context a {@link org.stjs.generator.GenerationContext} object.
	 * @param type a {@link javax.lang.model.element.Element} object.
	 * @param dependencyType a {@link org.stjs.generator.name.DependencyType} object.
	 * @return a {@link java.lang.String} object.
	 */
	String getTypeName(GenerationContext<?> context, Element type, DependencyType dependencyType);

	/**
	 * <p>getMethodName.</p>
	 *
	 * @param context a {@link org.stjs.generator.GenerationContext} object.
	 * @param tree a {@link com.sun.source.tree.MethodTree} object.
	 * @param path a {@link com.sun.source.util.TreePath} object.
	 * @return a {@link java.lang.String} object.
	 */
	String getMethodName(GenerationContext<?> context, MethodTree tree, TreePath path);

	/**
	 * <p>getResolvedTypes.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	Map<String, DependencyType> getResolvedTypes();
}
