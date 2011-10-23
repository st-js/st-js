package org.stjs.generator.scope.classloader;

/**
 * 
 * This is a wrapper around a method, but with the correct type for a generic type for example
 * 
 * @author acraciun
 * 
 */
public class MethodWrapper {
	private final String name;
	private final TypeWrapper returnType;
	private final TypeWrapper[] parameterTypes;
	private final int modifiers;
	private final TypeWrapper ownerType;
	private final boolean declared;

	public MethodWrapper(String name, TypeWrapper returnType, TypeWrapper[] parameterTypes, int modifiers,
			TypeWrapper ownerType, boolean declared) {
		this.name = name;
		this.returnType = returnType;
		this.parameterTypes = parameterTypes;
		this.modifiers = modifiers;
		this.ownerType = ownerType;
		this.declared = declared;
	}

	public String getName() {
		return name;
	}

	public TypeWrapper getReturnType() {
		return returnType;
	}

	public TypeWrapper[] getParameterTypes() {
		return parameterTypes;
	}

	public int getModifiers() {
		return modifiers;
	}

	public TypeWrapper getOwnerType() {
		return ownerType;
	}

	/**
	 * 
	 * @return true if the method was declared in the owner class, false if it was inherited
	 */
	public boolean isDeclared() {
		return declared;
	}

	public boolean isCompatibleParameterTypes(TypeWrapper[] paramTypes) {
		// System.out.println(Arrays.toString(parameterTypes));
		// System.out.println(Arrays.toString(paramTypes));
		int i = 0;
		for (i = 0; i < paramTypes.length && i < parameterTypes.length; ++i) {
			if (!parameterTypes[i].isAssignableFrom(paramTypes[i])) {
				// System.out.println("DIFF:" + i + ":" + parameterTypes[i] + "<>" + paramTypes[i]);
				break;
			}
		}
		if (i == paramTypes.length) {
			return true;
		}
		// try a varargs match
		if (i < parameterTypes.length && parameterTypes[i].getComponentType() != null) {
			TypeWrapper varArgParamType = parameterTypes[i].getComponentType();
			for (; i < paramTypes.length; ++i) {
				if (!varArgParamType.isAssignableFrom(paramTypes[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(returnType).append(" ").append(name).append(" (");
		for (TypeWrapper paramType : parameterTypes) {
			s.append(" ").append(paramType);
		}
		s.append(")");
		return s.toString();
	}

}
