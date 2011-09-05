package org.stjs.generator.scope;

import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.generator.utils.Option;

public interface ClassResolver {

	Option<ClassWrapper> resolveClass(String className, NameResolverVisitor visitor);

}
