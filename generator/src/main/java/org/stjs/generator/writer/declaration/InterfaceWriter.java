package org.stjs.generator.writer.declaration;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.Tree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.JavascriptTypes;
import org.stjs.generator.writer.WriterVisitor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.List;

public class InterfaceWriter<JS> extends JavascriptTypes<JS> {

	private List<Tree> getAllInterfaces(ClassTree clazz) {
		List<Tree> enums = new ArrayList<>();
		for (Tree member : clazz.getMembers()) {
			if (member instanceof ClassTree && member.getKind() == Tree.Kind.INTERFACE) {
				enums.add(member);
			}

			// Get enums recursively
			if (member instanceof ClassTree && member.getKind() == Tree.Kind.CLASS) {
				enums.addAll(getAllInterfaces((ClassTree) member));
			}
		}
		return enums;
	}

	public void generateAll(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);

		// Move member interfaces to the top level
		List<Tree> interfaces = getAllInterfaces(tree);
		for (Tree member : interfaces) {
			generateInterface(visitor, (ClassTree) member, context, stmts);
		}

		// Render top level interfaces correctly
		if (type.getKind() == ElementKind.INTERFACE) {
			generateInterface(visitor, tree, context, stmts);
		}
	}

	private List<Tree> getAllMembersExceptConstructors(ClassTree clazz) {
		List<Tree> nonConstructors = new ArrayList<>();
		for (Tree member : clazz.getMembers()) {
			if (!JavaNodes.isConstructor(member) && !(member instanceof BlockTree)) {
				nonConstructors.add(member);
			}
		}
		return nonConstructors;
	}

	private List<JS> getMembers(WriterVisitor<JS> visitor, ClassTree clazz, GenerationContext<JS> context) {
		// the following members must not appear in the initializer function:
		// - constructors (they are printed elsewhere)
		// - abstract methods (they should be omitted)

		List<Tree> nonConstructors = getAllMembersExceptConstructors(clazz);

		if (nonConstructors.isEmpty()) {
			return null;
		}

		List<JS> stmts = new ArrayList<>();
		for (Tree member : nonConstructors) {
			stmts.add(visitor.scan(member, context));
		}

		return stmts;
	}

	private DependencyType getDependencyTypeForClassDef(Element type) {
		if (JavaNodes.isInnerType(type)) {
			// this is an inner (anonymous or not) class -> STATIC dep type instead
			return DependencyType.STATIC;
		}
		return DependencyType.EXTENDS;
	}

	/**
	 * @return the list of implemented interfaces. for interfaces, the super class goes also in the interfaces list
	 */
	private List<JS> getInterfaces(ClassTree clazz, GenerationContext<JS> context) {
		Element type = TreeUtils.elementFromDeclaration(clazz);
		DependencyType depType = getDependencyTypeForClassDef(type);

		List<JS> ifaces = new ArrayList<>();
		for (Tree iface : clazz.getImplementsClause()) {
			TreeWrapper<Tree, JS> ifaceType = context.getCurrentWrapper().child(iface);
			if (ifaceType.isSyntheticType()) {
				continue;
			}

			JS typeName = context.js().name(ifaceType.getTypeName(depType));

			if (iface instanceof ParameterizedTypeTree) {
				List<JS> typeParameters = getTypeParams(((ParameterizedTypeTree) iface).getTypeArguments(), context);
				if (typeParameters != null) {
					ifaces.add(context.js().genericType(typeName, typeParameters));
					continue;
				}
			}

			ifaces.add(typeName);

		}

		if (clazz.getExtendsClause() != null && type.getKind() == ElementKind.INTERFACE) {
			TreeWrapper<Tree, JS> superType = context.getCurrentWrapper().child(clazz.getExtendsClause());
			if (!superType.isSyntheticType()) {
				JS typeName = context.js().name(superType.getTypeName(DependencyType.EXTENDS));

				if (superType instanceof ClassTree) {
					List<JS> typeParameters = getTypeParams(((ClassTree) superType).getTypeParameters(), context);
					if (typeParameters != null) {
						ifaces.add(0, context.js().genericType(typeName, typeParameters));
					} else {
						ifaces.add(0, typeName);
					}
				} else {
					ifaces.add(0, typeName);
				}
			}
		}
		return ifaces;
	}

	public void generateInterface(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		if (type.getKind() != ElementKind.INTERFACE) {
			return;
		}

		// TODO :: implements
		List<JS> members = getMembers(visitor, tree, context);

		String typeName = context.getNames().getTypeName(context, type, DependencyType.EXTENDS);
		String originalTypeName = typeName;

		JavaScriptBuilder<JS> js = context.js();

		typeName = typeName.replace('.', '_');
		stmts.add(js.interfaceDeclaration(typeName, members, getInterfaces(tree, context)));

		boolean outerClass = type.getEnclosingElement().getKind() == ElementKind.PACKAGE;
		if (outerClass && !typeName.equals(originalTypeName)) {
			stmts.add(js.assignment(
					AssignOperator.ASSIGN,
					js.name(originalTypeName),
					js.name(typeName)
			));
		}
	}
}
