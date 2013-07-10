/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator.scope;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.type.ClassWrapper;
import org.stjs.generator.type.FieldWrapper;
import org.stjs.generator.type.MethodSelector;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.utils.Option;
import org.stjs.generator.variable.Variable;

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

	private final Map<String, Variable> variables = Maps.newHashMap();

	private final Multimap<String, MethodWrapper> methods = ArrayListMultimap.create();

	private final Map<String, TypeWrapper> types = Maps.newHashMap();

	AbstractScope(Scope parent, GenerationContext context) {
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
		children = Lists.newArrayList();
		this.context = context;
	}

	protected GenerationContext getContext() {
		return context;
	}

	public void addField(FieldWrapper field) {
		variables.put(field.getName(), field);
	}

	public void addFields(Iterable<FieldWrapper> fields) {
		for (FieldWrapper field : fields) {
			addField(field);
		}
	}

	public void addMethods(String alias, List<MethodWrapper> methods) {
		this.methods.putAll(alias, methods);
	}

	public void addMethod(MethodWrapper method) {
		methods.put(method.getName(), method);
	}

	public void addMethods(Iterable<MethodWrapper> methods) {
		for (MethodWrapper method : methods) {
			addMethod(method);
		}
	}

	public void addTypes(Iterable<? extends TypeWrapper> types) {
		for (TypeWrapper type : types) {
			addType(type);
		}
	}

	public void addType(TypeWrapper clazz) {
		types.put(clazz.getSimpleName(), clazz);
	}

	public TypeWithScope resolveType(String name) {
		String[] components = name.split("\\.");
		TypeWrapper classWrapper = types.get(components[0]);
		if (classWrapper != null) {
			if (components.length == 1) {
				return new TypeWithScope(this, classWrapper);
			} else {
				ClassWrapper inner = resolveInnerType(components, (ClassWrapper) classWrapper, 1);
				return new TypeWithScope(this, inner);
			}
		}
		if (parent != null) {
			return parent.resolveType(name);
		}
		return null;
	}

	private ClassWrapper resolveInnerType(String[] innerTypeName, ClassWrapper outerType, int index) {
		Option<ClassWrapper> inner = outerType.getDeclaredClass(innerTypeName[index]);
		if (inner.isEmpty()) {
			// failed to resolve;
			return null;
		}
		if (index >= innerTypeName.length - 1) {
			// successfully resolved
			return inner.getOrNull();
		}
		// we need to go deeper
		return resolveInnerType(innerTypeName, inner.getOrNull(), index + 1);
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
		if (methodList != null && !methodList.isEmpty()) {
			MethodWrapper w = MethodSelector.resolveMethod(methodList, paramTypes);
			if (w != null) {
				return new MethodsWithScope(this, w);
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
