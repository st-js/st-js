package org.stjs.generator.scope.path;

import org.stjs.generator.handlers.utils.Option;
import org.stjs.generator.scope.ClassResolver;
import org.stjs.generator.scope.NameResolverVisitor;
import org.stjs.generator.scope.classloader.ClassWrapper;

public class QualifiedPath {

	private String packag;

	private String clazz;

	public QualifiedPath(String packag, String clazz) {
		this.packag = packag;
		this.clazz = clazz;
	}

	public String getClassSimpleName() {
		return clazz;
	}

	public String getOuterClassQualifiedName() {
		return packag != null ? join(packag, clazz) : clazz;
	}

	public String getClassQualifiedName() {
		return packag != null ? join(packag, clazz) : clazz;
	}

	public static class QualifiedFieldPath extends QualifiedPath {
		private String field;

		public QualifiedFieldPath(String packag, String clazz, String field) {
			super(packag, clazz);
			this.field = field;
		}

		public String getFieldName() {
			return field;
		}
	}

	public static class QualifiedMethodPath extends QualifiedPath {
		private final String method;
		private final String innerClassesName;

		public QualifiedMethodPath(String packag, String clazz, String innerClassesName, String methodName) {
			super(packag, clazz);
			this.innerClassesName = innerClassesName;
			this.method = methodName;
		}

		public String getMethodName() {
			return method;
		}

		@Override
		public String getClassSimpleName() {
			if (innerClassesName != null) {
				return join(super.getClassSimpleName(), innerClassesName);
			}
			return super.getClassSimpleName();
		}

		@Override
		public String getClassQualifiedName() {
			if (innerClassesName != null) {
				return join(super.getClassQualifiedName(), innerClassesName);
			}
			return super.getClassQualifiedName();
		}

		public String getInnerClassesName() {
			return innerClassesName;
		}

	}

	public static String join(String str1, String str2) {
		return str1 + "." + str2;
	}

	public static String afterLastDot(String str) {
		if (str == null) {
			return null;
		}
		int lastIndex = str.lastIndexOf(".");
		if (lastIndex >= 0) {
			return str.substring(lastIndex + 1);
		}
		return str;
	}

	public static String afterFirstDot(String str) {
		if (str == null) {
			return null;
		}
		int firstIndex = str.indexOf(".");
		if (firstIndex >= 0) {
			return str.substring(firstIndex + 1);
		}
		return str;
	}

	public static String beforeLastDot(String str) {
		if (str == null) {
			return null;
		}
		int lastIndex = str.lastIndexOf(".");
		if (lastIndex >= 0) {
			return str.substring(0, lastIndex);
		}
		return null;
	}

	public static String beforeFirstDot(String str) {
		if (str == null) {
			return null;
		}
		int firstIndex = str.indexOf(".");
		if (firstIndex >= 0) {
			return str.substring(0, firstIndex);
		}
		return null;
	}

	public static QualifiedMethodPath withMethod(String path, ClassResolver scope, NameResolverVisitor visitor) {
		/*
		 * The JLS states in section 7.1:
		 * 
		 * "A package may not contain a type declaration and a subpackage of the same name, or a compile-time error
		 * results.
		 * 
		 * This implies that an expression such as :
		 * 
		 * a.b.c() may refer to class b within package a, or innerclass b within class a, but not both.
		 */
		String methodName = afterLastDot(path);
		String classAndPackage = beforeLastDot(path);
		String classAndPackageCandidate = classAndPackage;
		String innerClassesName = null;
		do {
			if (scope.resolveClass(classAndPackageCandidate, visitor).isDefined()) {
				String className = afterLastDot(classAndPackageCandidate);
				String packageName = beforeLastDot(classAndPackageCandidate);
				return new QualifiedMethodPath(packageName, className, innerClassesName, methodName);
			}
			innerClassesName = afterLastDot(classAndPackageCandidate);
			classAndPackageCandidate = beforeLastDot(classAndPackageCandidate);
		} while (classAndPackageCandidate != null);
		return null;
	}

	public static QualifiedFieldPath withField(String path) {
		String fieldName = afterLastDot(path);
		String className = afterLastDot(beforeLastDot(path));
		String packageName = beforeLastDot(beforeLastDot(path));
		return new QualifiedFieldPath(packageName, className, fieldName);
	}

	public static QualifiedPath withClass(Option<ClassWrapper> maybeClass) {
		for (ClassWrapper clazz : maybeClass) {
			return withClass(clazz);
		}
		return null;
	}

	public static QualifiedPath withClass(ClassWrapper clazz) {
		if (clazz != null) {
			return new QualifiedPath(clazz.getPackageName(), clazz.getSimpleName());
		}
		return null;
	}

	public static QualifiedPath withClassName(String name) {
		String className = afterLastDot(name);
		String packageName = beforeLastDot(name);
		return new QualifiedPath(packageName, className);
	}

	public String getClassName(boolean useQualifiedNames) {
		return useQualifiedNames ? getClassQualifiedName() : getClassSimpleName();
	}

}
