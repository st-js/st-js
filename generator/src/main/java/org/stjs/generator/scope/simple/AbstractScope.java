package org.stjs.generator.scope.simple;

import japa.parser.ast.expr.NameExpr;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.stjs.generator.scope.classloader.ClassWrapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public abstract class AbstractScope implements Scope {
	
	private final Scope parent;
	private final List<Scope> children;
	
	AbstractScope(Scope parent) {
		this.parent = parent;
		parent.addChild(this);
		children = Lists.newArrayList();
	}
	
	private Map<String, Field> fields = Maps.newHashMap();
	
	private Map<String, Variable> variables = Maps.newHashMap();
	
	private Multimap<String, Method> methods = ArrayListMultimap.create();
	
	private Map<String, ClassWrapper> types = Maps.newHashMap();
	
	private Set<NameExpr> typeImportOnDemandSet = Sets.newHashSet();
	
	public void addField(Field field) {
		fields.put(field.getName(), field);
	}

	public void addMethods(String alias, List<Method> methods) {
		this.methods.putAll(alias, methods);
	}

	public void addMethod(Method method) {
		methods.put(method.getName(), method);
	}

	public void addType(ClassWrapper clazz) {
		types.put(clazz.getSimpleName(), clazz);
	}

	public void addTypeImportOnDemand(NameExpr name) {
		typeImportOnDemandSet.add(name);
	}

	public ClassWrapper resolveType(String name) {
		ClassWrapper classWrapper = types.get(name);
		if (classWrapper != null) {
			return classWrapper;
		}
		if (parent != null) {
			return parent.resolveType(name);
		}
		// TODO : If I am the root scope, then check for qualified names
		return null;
	}
	
	public Scope addChild(Scope scope) {
		children.add(scope);
		return scope;
	}
	
	@VisibleForTesting
	public Set<NameExpr> getTypeImportOnDemandSet() {
		return typeImportOnDemandSet;
	}

	@VisibleForTesting
	public Field getField(String name) {
		return fields.get(name);
	}

	@VisibleForTesting
	public Collection<Method> getMethods(String name) {
		return methods.get(name);
	} 

	@VisibleForTesting
	public ClassWrapper getType(String name) {
		return types.get(name);
	}
	
	public void addVariable(Variable variable) {
		variables.put(variable.getName(), variable);
	}


}
