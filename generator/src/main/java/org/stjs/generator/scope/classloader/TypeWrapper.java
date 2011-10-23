package org.stjs.generator.scope.classloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.stjs.generator.utils.Option;

public interface TypeWrapper {
	public Type getType();

	public Option<FieldWrapper> findField(String name);

	public Option<MethodWrapper> findMethod(String name, TypeWrapper... paramTypes);

	/**
	 * @return the last part of the type's name (ex: the Class name)
	 */
	public String getSimpleName();

	/**
	 * @return the full name of a class (package and outer classes included)
	 */
	public String getName();

	/**
	 * @return the last part of the type's name (but outer classes simple name included - if any)
	 */
	public String getExternalName();

	/**
	 * 
	 * @return is this type can be used in an import declaration
	 */
	public boolean isImportable();

	public boolean isInnerType();

	public boolean hasAnnotation(Class<? extends Annotation> class1);

	public boolean isAssignableFrom(TypeWrapper typeWrapper);

	public TypeWrapper getComponentType();
}
