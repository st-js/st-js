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
import org.stjs.generator.type.FieldWrapper;
import org.stjs.generator.type.MethodSelector;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.TypeWrapper;
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
		if (methodList != null && methodList.size() > 0) {
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
