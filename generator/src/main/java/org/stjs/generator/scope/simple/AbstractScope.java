package org.stjs.generator.scope.simple;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.scope.classloader.FieldWrapper;
import org.stjs.generator.scope.classloader.MethodWrapper;
import org.stjs.generator.scope.classloader.TypeWrapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public abstract class AbstractScope implements Scope {

	private final Scope parent;
	private final List<Scope> children;

	private final GenerationContext context;

	AbstractScope(Scope parent, GenerationContext context) {
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
		children = Lists.newArrayList();
		this.context = context;
	}

	private Map<String, Variable> variables = Maps.newHashMap();

	private Multimap<String, MethodWrapper> methods = ArrayListMultimap.create();

	private Map<String, TypeWrapper> types = Maps.newHashMap();

	protected GenerationContext getContext() {
		return context;
	}

	public void addField(FieldWrapper field) {
		variables.put(field.getName(), field);
	}

	public void addMethods(String alias, List<MethodWrapper> methods) {
		this.methods.putAll(alias, methods);
	}

	public void addMethod(MethodWrapper method) {
		methods.put(method.getName(), method);
	}

	public void addType(TypeWrapper clazz) {
		types.put(clazz.getSimpleName(), clazz);
	}

	public TypeWithScope resolveType(String name) {
		TypeWrapper classWrapper = types.get(name);
		if (classWrapper != null) {
			if (classWrapper.isImportable()) {
				// only classes are used as imports
				context.addResolvedImport(classWrapper.getName());
			}
			return new TypeWithScope(this, classWrapper);
		}
		if (parent != null) {
			return parent.resolveType(name);
		}
		return null;
	}

	public Scope addChild(Scope scope) {
		children.add(scope);
		return scope;
	}

	public Scope getParent() {
		return parent;
	}

	@SuppressWarnings("unchecked")
	public <T extends Scope> T closest(Class<T> scopeType) {
		Scope currentScope = this;
		while (currentScope != null && currentScope.getClass() != scopeType) {
			currentScope = currentScope.getParent();
		}
		return (T) currentScope;
	}

	public MethodsWithScope resolveMethod(final String name, TypeWrapper... paramTypes) {
		Collection<MethodWrapper> methodList = methods.get(name);
		if (methodList != null) {
			for (MethodWrapper w : methodList) {
				if (w.isCompatibleParameterTypes(paramTypes)) {
					return new MethodsWithScope(this, w);
				}
			}
		}
		if (parent != null) {
			return parent.resolveMethod(name, paramTypes);
		}
		return null;
	}

	@Override
	public List<Scope> getChildren() {
		return ImmutableList.copyOf(children);
	}

	@Override
	public VariableWithScope resolveVariable(String name) {
		Variable variable = variables.get(name);
		if (variable != null) {
			return new VariableWithScope(this, variable);
		}

		if (parent != null) {
			return parent.resolveVariable(name);
		}
		return null;
	}

	@VisibleForTesting
	public Collection<MethodWrapper> getMethods(String name) {
		return methods.get(name);
	}

	@VisibleForTesting
	public TypeWrapper getType(String name) {
		return types.get(name);
	}

	public void addVariable(Variable variable) {
		variables.put(variable.getName(), variable);
	}

}
