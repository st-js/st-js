package org.stjs.generator.scope.simple;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.stjs.generator.scope.classloader.ClassWrapper;
import org.stjs.generator.scope.simple.Scope.ScopeVisitor;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;

public class ClassScope extends AbstractScope {

	private final ClassWrapper clazz;
	
	ClassScope(ClassWrapper clazz, Scope parent) {
		super(parent);
		this.clazz = clazz;
	}

	@Override
	public <T> T apply(ScopeVisitor<T> visitor) {
		return visitor.apply(this);
	}

	public ClassWrapper getClazz() {
		return clazz;
	}


}
