package org.stjs.generator.scope;

import org.stjs.generator.handlers.utils.Option;
import org.stjs.generator.scope.classloader.ClassWrapper;

public interface ClassResolver {

	Option<ClassWrapper> resolveClass(String className, NameResolverVisitor visitor);

}
