package org.stjs.generator.scope.classloader;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.STATIC;
import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stjs.generator.utils.Option;
import org.stjs.generator.utils.PreConditions;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Primitives;

public class ClassWrapper implements TypeWrapper {
	private final Class<?> clazz;

	private Map<String, FieldWrapper> fields = null;
	private Multimap<String, MethodWrapper> methods = null;

	private TypeWrapper componentType;

	public ClassWrapper(Class<?> clazz) {
		this.clazz = clazz;
	}

	private FieldWrapper buildFieldWrapper(String name, TypeWrapper type, int modifiers, Class<?> ownerClass,
			TypeWrapper[] actualTypeArgs) {
		// substitute the field's type with the actual type if the field has a generic type
		return new FieldWrapper(name, substituteType(type, ownerClass, actualTypeArgs), modifiers, this,
				ownerClass == clazz);
	}

	private TypeWrapper substituteType(TypeWrapper origType, Class<?> ownerClass, TypeWrapper[] typeArguments) {
		if (typeArguments == null) {
			return origType;
		}
		PreConditions.checkState(ownerClass.getTypeParameters().length == typeArguments.length,
				"Difference length between class parameters (%d) and arguments list (%d)",
				ownerClass.getTypeParameters().length, typeArguments.length);
		for (int i = 0; i < ownerClass.getTypeParameters().length; ++i) {
			TypeVariable<?> typeVar = ownerClass.getTypeParameters()[i];
			if (origType.getName().equals(typeVar.getName())) {
				return typeArguments[i];
			}
		}
		return origType;
	}

	private TypeWrapper[] substituteTypes(TypeWrapper[] origTypes, Class<?> ownerClass, TypeWrapper[] actualTypeArgs) {
		if (origTypes.length == 0 || actualTypeArgs == null) {
			return origTypes;
		}
		TypeWrapper[] substTypes = new TypeWrapper[origTypes.length];
		for (int i = 0; i < origTypes.length; ++i) {
			substTypes[i] = substituteType(origTypes[i], ownerClass, actualTypeArgs);
		}
		return substTypes;
	}

	private MethodWrapper buildMethodWrapper(String name, TypeWrapper returnType, TypeWrapper[] parameterTypes,
			int modifiers, Class<?> ownerClass, TypeWrapper[] actualTypeArgs) {
		return new MethodWrapper(name, substituteType(returnType, ownerClass, actualTypeArgs), substituteTypes(
				parameterTypes, ownerClass, actualTypeArgs), modifiers, this, ownerClass == clazz);
	}

	static Class<?> getRawClazz(Type type) {
		if (type instanceof Class<?>) {
			return (Class<?>) type;
		}
		if (type instanceof ParameterizedType) {
			return getRawClazz(((ParameterizedType) type).getRawType());
		}
		// TODO what to do exacly here !?
		return Object.class;
	}

	private TypeWrapper[] getActualTypeArgs(Type type, Class<?> subTypeClass, TypeWrapper[] subTypeActualTypeArgs) {
		if (type instanceof Class<?>) {
			return null;
		}
		if (type instanceof ParameterizedType) {
			ParameterizedType ptype = (ParameterizedType) type;
			TypeWrapper[] typeArguments = new TypeWrapper[ptype.getActualTypeArguments().length];
			for (int i = 0; i < typeArguments.length; ++i) {
				TypeWrapper actualType = TypeWrappers.wrap(ptype.getActualTypeArguments()[i]);
				typeArguments[i] = substituteType(actualType, subTypeClass, subTypeActualTypeArgs);
			}
			return typeArguments;
		}

		throw new RuntimeException("Received unknown type:" + type + " of class:" + type.getClass());
	}

	private void prepareFieldsAndMethods() {
		if (fields != null) {
			return;
		}
		fields = new HashMap<String, FieldWrapper>();
		methods = ArrayListMultimap.create();
		TypeWrapper[] actualTypeArgs = null;
		Class<?> rawClass = null;
		for (Type c = getType(); c != null; c = rawClass.getGenericSuperclass()) {
			actualTypeArgs = getActualTypeArgs(c, rawClass, actualTypeArgs);
			rawClass = getRawClazz(c);
			addFields(rawClass, actualTypeArgs);
			addMethods(rawClass, actualTypeArgs);
		}
	}

	private void addFields(Class<?> rawClass, TypeWrapper[] actualTypeArgs) {
		for (Field f : rawClass.getDeclaredFields()) {
			if (fields.get(f.getName()) == null) {
				// keep the version from the most specific class
				fields.put(
						f.getName(),
						buildFieldWrapper(f.getName(), TypeWrappers.wrap(f.getGenericType()), f.getModifiers(),
								rawClass, actualTypeArgs));
			}
		}
	}

	private void addMethods(Class<?> rawClass, TypeWrapper[] actualTypeArgs) {
		// if (clazz.isEnum()) {
		// // XXX not sure how to deal with ordinal/name methods
		// methods = new ArrayList<MethodWrapper>(methods);
		// methods.addAll(asList(Enum.class.getDeclaredMethods()));
		// }
		//
		for (Method m : rawClass.getDeclaredMethods()) {
			TypeWrapper[] paramTypes = new TypeWrapper[m.getGenericParameterTypes().length];
			for (int i = 0; i < m.getGenericParameterTypes().length; ++i) {
				paramTypes[i] = TypeWrappers.wrap(m.getGenericParameterTypes()[i]);
			}
			methods.put(
					m.getName(),
					buildMethodWrapper(m.getName(), TypeWrappers.wrap(m.getGenericReturnType()), paramTypes,
							m.getModifiers(), rawClass, actualTypeArgs));
		}

	}

	public Type getType() {
		return clazz;
	}

	public String getPackageName() {
		return clazz.getPackage().getName();
	}

	public String getSimpleName() {
		return clazz.getSimpleName();
	}

	@Override
	public String getExternalName() {
		String name = clazz.getSimpleName();
		for (Class<?> c = clazz.getDeclaringClass(); c != null; c = c.getDeclaringClass()) {
			name = c.getSimpleName() + "." + name;
		}
		return name;
	}

	public Option<ClassWrapper> getDeclaringClass() {
		Class<?> declaringClass = clazz.getDeclaringClass();
		return declaringClass != null ? Option.some(new ClassWrapper(declaringClass)) : Option.<ClassWrapper> none();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TypeVariableWrapper<Class<?>>[] getTypeParameters() {
		TypeVariableWrapper[] types = new TypeVariableWrapper[clazz.getTypeParameters().length];
		for (int i = 0; i < clazz.getTypeParameters().length; ++i) {
			types[i] = TypeWrappers.wrap(clazz.getTypeParameters()[i]);
		}
		return types;
	}

	public int getModifiers() {
		return clazz.getModifiers();
	}

	public Annotation[] getAnnotations() {
		return clazz.getAnnotations();
	}

	public Option<FieldWrapper> findField(String name) {
		prepareFieldsAndMethods();
		FieldWrapper f = fields.get(name);
		return f != null ? Option.some(f) : Option.<FieldWrapper> none();
	}

	public List<MethodWrapper> findMethods(final String name) {
		return ImmutableList.copyOf(Iterables.filter(getDeclaredMethods(), new Predicate<MethodWrapper>() {
			@Override
			public boolean apply(MethodWrapper method) {
				return method.getName().equals(name);
			}
		}));
	}

	public <T> Option<ClassWrapper> getDeclaredClass(String name) {
		try {
			for (Class<?> innerClass : clazz.getDeclaredClasses()) {
				if (innerClass.getSimpleName().equals(name)) {
					return Option.some(new ClassWrapper(innerClass));
				}
			}
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
		return Option.none();
	}

	public Option<MethodWrapper> findMethod(final String name, TypeWrapper... paramTypes) {
		prepareFieldsAndMethods();
		Collection<MethodWrapper> wrappers = methods.get(name);
		if (wrappers == null) {
			return Option.none();
		}
		for (MethodWrapper w : wrappers) {
			if (w.isCompatibleParameterTypes(paramTypes)) {
				return Option.some(w);
			}
		}
		return Option.none();
	}

	public List<MethodWrapper> getDeclaredMethods() {
		prepareFieldsAndMethods();
		return Lists.newArrayList(methods.values());
	}

	public boolean hasDeclaredField(String fieldName) {
		return findField(fieldName).isDefined();
	}

	public Option<ClassWrapper> getSuperclass() {
		Class<?> superClass = clazz.getSuperclass();
		return superClass != null ? Option.some(new ClassWrapper(superClass)) : Option.<ClassWrapper> none();
	}

	public String getName() {
		return clazz.getName();
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public List<FieldWrapper> getDeclaredNonPrivateStaticFields() {
		return ImmutableList.copyOf(filter(getDeclaredFields(), new Predicate<FieldWrapper>() {
			@Override
			public boolean apply(FieldWrapper field) {
				return isStaticButNotPrivate(field.getModifiers());
			}
		}));
	}

	private boolean isStaticButNotPrivate(int modifiers) {
		return (modifiers & (PRIVATE | STATIC)) == STATIC;
	}

	public List<MethodWrapper> getDeclaredNonPrivateStaticMethods() {
		return ImmutableList.copyOf(filter(getDeclaredMethods(), new Predicate<MethodWrapper>() {

			@Override
			public boolean apply(MethodWrapper method) {
				return isStaticButNotPrivate(method.getModifiers());
			}
		}));
	}

	private static final Function<Class<?>, ClassWrapper> WrapClass = new Function<Class<?>, ClassWrapper>() {
		@Override
		public ClassWrapper apply(Class<?> clazz) {
			return new ClassWrapper(clazz);
		}
	};

	public List<ClassWrapper> getDeclaredNonPrivateStaticClasses() {

		return ImmutableList.copyOf(transform(filter(asList(clazz.getDeclaredClasses()), new Predicate<Class<?>>() {
			@Override
			public boolean apply(Class<?> clazz) {
				return isStaticButNotPrivate(clazz.getModifiers());
			}
		}), WrapClass));
	}

	public List<ClassWrapper> getDeclaredClasses() {
		return ImmutableList.copyOf(transform(asList(clazz.getDeclaredClasses()), WrapClass));
	}

	public List<FieldWrapper> getDeclaredFields() {
		prepareFieldsAndMethods();
		return Lists.newArrayList(fields.values());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ClassWrapper other = (ClassWrapper) obj;
		return clazz == other.clazz;
	}

	public boolean isParentClassOf(ClassWrapper otherClass) {
		Class<?> classPointer = otherClass.getClazz();
		do {
			classPointer = classPointer.getSuperclass();
			if (classPointer == null) {
				return false;
			}
			if (classPointer == this.clazz) {
				return true;
			}
		} while (true);
	}

	public boolean isInnerType() {
		return clazz.getDeclaringClass() != null;
	}

	@Override
	public boolean isImportable() {
		return true;
	}

	@Override
	public boolean hasAnnotation(Class<? extends Annotation> class1) {
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			if (c.isAnnotationPresent(class1)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isAssignableFrom(TypeWrapper typeWrapper) {
		if (typeWrapper == null) {
			// assignable from a null value
			return true;
		}
		if (typeWrapper instanceof ClassWrapper) {
			// try first normal assignable
			Class<?> otherClass = ((ClassWrapper) typeWrapper).clazz;
			if (clazz.isAssignableFrom(otherClass)) {
				return true;
			}
			// try primitives
			if (Primitives.wrap(clazz).isAssignableFrom(Primitives.wrap(otherClass))) {
				return true;
			}

			// go on with primitive rules double/float -> accept long/int -> accept byte/char (but this only if there is
			// none more specific!)
			return false;
		}
		// TODO do for the other types
		return false;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public TypeWrapper getComponentType() {
		prepareFieldsAndMethods();
		// TODO fix this

		return TypeWrappers.wrap(clazz.getComponentType());
	}
}