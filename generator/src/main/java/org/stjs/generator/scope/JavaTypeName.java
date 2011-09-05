package org.stjs.generator.scope;

import static org.stjs.generator.scope.path.QualifiedPath.join;
import static org.stjs.generator.scope.path.QualifiedPath.withClass;

import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.generator.scope.path.QualifiedPath;
import org.stjs.generator.utils.Option;

/**
 * Java types name, with support for inner classes. Option.None represents anonymous classes, Option.some named classes.
 * For instance SuperClass.InnerClass$1 will be a chain of
 * 
 * None -> Some<InnerClass> -> Some<SuperClass>
 * 
 */
// TODO : we are already using the class loader most of the time to resolve identifiers
// why do generalize that and get rid of this class (use java.lang.Class instead)
// QualifiedPath already knows how to deal with inner classes.
// There is logic duplication now (inner class in compilation unit are not treated the same way as inner classes
// out of compilation unit)
public class JavaTypeName {

	private final Option<QualifiedPath> classPath;
	private final Option<QualifiedPath> enclosingTypePath;

	public JavaTypeName(QualifiedPath classPath, QualifiedPath enclosingTypePath) {
		this.classPath = Option.of(classPath);
		this.enclosingTypePath = Option.of(enclosingTypePath);
	}

	public JavaTypeName(ClassWrapper clazz) {
		this(withClass(clazz), withClass(clazz.getDeclaringClass()));
	}

	public JavaTypeName(QualifiedPath classPath) {
		this.classPath = Option.of(classPath);
		this.enclosingTypePath = Option.none();
	}

	public Option<String> getSimpleName() {
		return classPath.isDefined() ? Option.some(classPath.getOrThrow().getClassSimpleName()) : Option
				.<String> none();
	}

	public boolean isAnonymous() {
		return classPath.isEmpty();
	}

	public Option<String> getFullName(boolean useQualifiedNames) {
		if (isAnonymous()) {
			return Option.none();
		}
		if (enclosingTypePath.isEmpty()) {
			return Option.some(classPath.getOrThrow().getClassName(useQualifiedNames));
		}
		String enclosingQualifiedName = enclosingTypePath.getOrThrow().getClassName(useQualifiedNames);
		return Option.some(join(enclosingQualifiedName, classPath.getOrThrow().getClassName(useQualifiedNames)));
	}

}
