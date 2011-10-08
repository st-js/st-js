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
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class CompilationUnitScope {
 
	private Map<String, Field> fields = Maps.newHashMap();
	
	private Multimap<String, Method> methods = ArrayListMultimap.create();
	
	private Map<String, ClassWrapper> types = Maps.newHashMap();
	
	private Set<NameExpr> typeImportOnDemandSet = Sets.newHashSet();
		
	public void addField(String alias, Field field) {
		fields.put(alias, field);
	}

	public void addMethods(String alias, List<Method> methods) {
		this.methods.putAll(alias, methods);
	}

	public void addMethod(String name, Method method) {
		methods.put(name, method);
	}

	public void addType(String alias, ClassWrapper clazz) {
		types.put(alias, clazz);
	}

	public void addTypeImportOnDemand(NameExpr name) {
		typeImportOnDemandSet.add(name);
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

	


}
