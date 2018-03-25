package org.stjs.generator.name;

/**
 *
 * this enum describes the types of dependencies that can arrise between different classes.
 *
 * @author acraciun
 * @version $Id: $Id
 */
public enum DependencyType {
	EXTENDS(""), // for backward compatibility - the files with no prefix are considered EXTENDS <=> required to appear
					// before
	STATIC("s"), OTHER("o");

	private final String prefix;

	private DependencyType(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * <p>Getter for the field <code>prefix</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * <p>getTypeName.</p>
	 *
	 * @param typeWithPrefix a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getTypeName(String typeWithPrefix) {
		if (typeWithPrefix == null || typeWithPrefix.isEmpty()) {
			return typeWithPrefix;
		}
		int pos = typeWithPrefix.indexOf(':');
		return pos < 0 ? typeWithPrefix : typeWithPrefix.substring(pos + 1);
	}

	/**
	 * <p>getDependencyType.</p>
	 *
	 * @param typeWithPrefix a {@link java.lang.String} object.
	 * @return a {@link org.stjs.generator.name.DependencyType} object.
	 */
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
	 * <p>getTypeWithPrefix.</p>
	 *
	 * @param typeName a {@link java.lang.String} object.
	 * @param depType a {@link org.stjs.generator.name.DependencyType} object.
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

	/**
	 * <p>isStricter.</p>
	 *
	 * @param otherType a {@link org.stjs.generator.name.DependencyType} object.
	 * @return a boolean.
	 */
	public boolean isStricter(DependencyType otherType) {
		return ordinal() < otherType.ordinal();
	}
}
