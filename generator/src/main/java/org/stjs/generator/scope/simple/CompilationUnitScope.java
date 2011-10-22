package org.stjs.generator.scope.simple;

import static org.stjs.generator.scope.classloader.TypeWrappers.wrap;
import japa.parser.ast.expr.NameExpr;

import java.util.Set;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.scope.classloader.ClassLoaderWrapper;
import org.stjs.generator.scope.classloader.ClassWrapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

public class CompilationUnitScope extends AbstractScope {

	private String packageName;

	private final Set<NameExpr> typeImportOnDemandSet = Sets.newHashSet();

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
		typeImportOnDemandSet.add(name);
	}

	@VisibleForTesting
	public Set<NameExpr> getTypeImportOnDemandSet() {
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
			getContext().addResolvedImport(packageClass.getName());
			return new TypeWithScope(this, packageClass);
		}

		// fully qualified
		for (ClassWrapper qualifiedClass : classLoaderWrapper.loadClassOrInnerClass(name)) {
			getContext().addResolvedImport(qualifiedClass.getName());
			return new TypeWithScope(this, qualifiedClass);
		}

		// try java.lang
		for (ClassWrapper qualifiedClass : classLoaderWrapper.loadClassOrInnerClass("java.lang." + name)) {
			getContext().addResolvedImport(qualifiedClass.getName());
			return new TypeWithScope(this, qualifiedClass);
		}
		return null;
	}

}
