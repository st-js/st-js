package org.stjs.generator.scope.simple;

import static org.stjs.generator.scope.classloader.ClassWrapper.wrap;
import japa.parser.ast.expr.NameExpr;

import java.util.Set;

import org.stjs.generator.scope.classloader.ClassLoaderWrapper;
import org.stjs.generator.scope.classloader.ClassWrapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

public class CompilationUnitScope extends AbstractScope {

	private final Package packageDeclaration;
	
	private final Set<NameExpr> typeImportOnDemandSet = Sets.newHashSet();

	private final ClassLoaderWrapper classLoaderWrapper;


	public CompilationUnitScope(Package packageDeclaration, ClassLoaderWrapper classLoaderWrapper) {
		super(null);
		this.packageDeclaration = packageDeclaration;
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

	public Package getPackage() {
		return packageDeclaration;
	}

	@Override
	public void apply(ScopeVisitor visitor) {
		visitor.apply(this);
	}
	
	public void addTypeImportOnDemand(NameExpr name) {
		typeImportOnDemandSet.add(name);
	}

	
	@VisibleForTesting
	public Set<NameExpr> getTypeImportOnDemandSet() {
		return typeImportOnDemandSet;
	}
	
	@Override
	public ClassWrapper resolveType(String name) {
		ClassWrapper type = super.resolveType(name);
		if (type != null) {
			return type;
		}
		// try in package
		for (ClassWrapper packageClass : classLoaderWrapper.loadClassOrInnerClass(packageDeclaration.getName()+"."+name)) {
			return packageClass;
		}
		
		// fully qualified
		for (ClassWrapper qualifiedClass : classLoaderWrapper.loadClassOrInnerClass(name)) {
			return qualifiedClass;
		}
		throw new RuntimeException("Could not resolve type "+name);
	}

}
