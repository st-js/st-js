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

import static org.stjs.generator.type.TypeWrappers.wrap;
import japa.parser.ast.expr.NameExpr;

import java.util.Set;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.type.ClassLoaderWrapper;
import org.stjs.generator.type.ClassWrapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

public class CompilationUnitScope extends AbstractScope {

	private String packageName;

	private final Set<String> typeImportOnDemandSet = Sets.newHashSet();

	private final ClassLoaderWrapper classLoaderWrapper;

	public CompilationUnitScope(ClassLoaderWrapper classLoaderWrapper, GenerationContext context) {
		super(null, context);
		this.classLoaderWrapper = classLoaderWrapper;
		addType(wrap(String.class));
		addType(wrap(Integer.class));
		addType(wrap(Boolean.class));
		addType(wrap(Character.class));
		addType(wrap(Byte.class));
		addType(wrap(Short.class));
		addType(wrap(Float.class));
		addType(wrap(Double.class));
		addType(wrap(Exception.class));
		addType(wrap(RuntimeException.class));

	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public <T> T apply(ScopeVisitor<T> visitor) {
		return visitor.apply(this);
	}

	public void addTypeImportOnDemand(NameExpr name) {
		typeImportOnDemandSet.add(name.toString());
	}

	@VisibleForTesting
	public Set<String> getTypeImportOnDemandSet() {
		return typeImportOnDemandSet;
	}

	@Override
	public TypeWithScope resolveType(String name) {
		TypeWithScope type = super.resolveType(name);
		if (type != null) {
			return type;
		}
		// try in package
		for (ClassWrapper packageClass : classLoaderWrapper.loadClassOrInnerClass(packageName + "." + name)) {
			super.addType(packageClass);
			return new TypeWithScope(this, packageClass);
		}

		// fully qualified
		for (ClassWrapper qualifiedClass : classLoaderWrapper.loadClassOrInnerClass(name)) {
			super.addType(qualifiedClass);
			return new TypeWithScope(this, qualifiedClass);
		}

		// try java.lang
		for (ClassWrapper qualifiedClass : classLoaderWrapper.loadClassOrInnerClass("java.lang." + name)) {
			super.addType(qualifiedClass);
			return new TypeWithScope(this, qualifiedClass);
		}

		// try on demand
		for (String pack : typeImportOnDemandSet) {
			for (ClassWrapper qualifiedClass : classLoaderWrapper.loadClassOrInnerClass(pack + "." + name)) {
				super.addType(qualifiedClass);
				return new TypeWithScope(this, qualifiedClass);
			}
		}

		// could be A.B.C and part of the path to be imported
		int pos = name.lastIndexOf('.');
		if (pos >= 0) {
			String parent = name.substring(0, pos);
			String inner = name.substring(pos + 1, name.length());
			TypeWithScope ts = resolveType(parent);
			if (ts != null) {
				for (ClassWrapper qualifiedClass : classLoaderWrapper.loadClassOrInnerClass(ts.getType().getName()
						+ "." + inner)) {
					super.addType(qualifiedClass);
					return new TypeWithScope(this, qualifiedClass);
				}
			}
		}

		return null;
	}

}
