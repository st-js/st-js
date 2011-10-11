package org.stjs.generator.scope.classloader;

import org.stjs.generator.scope.path.QualifiedPath.QualifiedMethodPath;
import org.stjs.generator.utils.Option;

public class ClassLoaderWrapper {

	private final ClassLoader classLoader;

	public ClassLoaderWrapper(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public boolean hasClass(String name) {
		return loadClass(name).isDefined();
	}

	public Option<ClassWrapper> loadClass(String name) {
		try {
			return Option.<ClassWrapper> some(new ClassWrapper(classLoader.loadClass(name)));
		} catch (ClassNotFoundException e) {
			return Option.none();
		}
	}

	public Option<ClassWrapper> loadClass(QualifiedMethodPath path) {
		if (path == null) {
			return Option.none();
		}

		StringBuilder className = new StringBuilder(path.getOuterClassQualifiedName());
		if (path.getInnerClassesName() != null) {
			for (String innerClass : path.getInnerClassesName().split("\\.")) {
				className.append("$");
				className.append(innerClass);
			}
		}
		return loadClass(className.toString());
	}

	public Option<ClassWrapper> loadClassOrInnerClass(String classLoaderCompatibleName) {
		while (true) {
			Option<ClassWrapper> clazz = loadClass(classLoaderCompatibleName);
			if (clazz.isDefined()) {
				return clazz;
			}
			int lastIndexDot = classLoaderCompatibleName.lastIndexOf('.');
			if (lastIndexDot < 0) {
				return Option.none();
			}
			classLoaderCompatibleName = replaceCharAt(classLoaderCompatibleName, lastIndexDot, '$');
		}
	}
	
	private String replaceCharAt(String s, int pos, char c) {
		StringBuffer buf = new StringBuffer(s);
		buf.setCharAt(pos, c);
		return buf.toString();
	}
}