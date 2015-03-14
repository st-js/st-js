package org.stjs.generator.name;

/**
 *
 * this enum describes the types of dependencies that can arrise between different classes.
 *
 * @author acraciun
 */
public enum DependencyType {
	EXTENDS(""), // for backward compatibility - the files with no prefix are considered EXTENDS <=> required to appear
					// before
	STATIC("s"), OTHER("o");

	private final String prefix;

	private DependencyType(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public static String getTypeName(String typeWithPrefix) {
		if (typeWithPrefix == null || typeWithPrefix.isEmpty()) {
			return typeWithPrefix;
		}
		int pos = typeWithPrefix.indexOf(':');
		return pos < 0 ? typeWithPrefix : typeWithPrefix.substring(pos + 1);
	}

	public static DependencyType getDependencyType(String typeWithPrefix) {
		if (typeWithPrefix == null || typeWithPrefix.isEmpty()) {
			return EXTENDS;
		}
		int pos = typeWithPrefix.indexOf(':');
		if (pos < 0) {
			return EXTENDS;
		}
		if (typeWithPrefix.charAt(0) == STATIC.prefix.charAt(0)) {
			return STATIC;
		}
		return OTHER;
	}

	/**
	 *
	 * @param typeName
	 * @param depType
	 * @return prefix:type or type for EXTENDS
	 */
	public static String getTypeWithPrefix(String typeName, DependencyType depType) {
		if (typeName == null || typeName.isEmpty()) {
			return typeName;
		}
		if (depType == EXTENDS) {
			return typeName;
		}
		return depType.prefix + ":" + typeName;
	}

	public boolean isStricter(DependencyType otherType) {
		return ordinal() < otherType.ordinal();
	}
}
