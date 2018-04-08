package org.stjs.generator.writer.declaration;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.name.DependencyType;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.generator.writer.WriterVisitor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

public class EnumWriter<JS> {

	/**
	 * transform a.b.type in constructor.type
	 */
	private String replaceFullNameWithConstructor(String typeName) {
		int pos = typeName.lastIndexOf('.');
		return JavascriptKeywords.CONSTRUCTOR + typeName.substring(pos);
	}

	private List<Tree> getAllEnums(ClassTree clazz) {
		List<Tree> enums = new ArrayList<Tree>();
		for (Tree member : clazz.getMembers()) {
			if (member instanceof ClassTree && member.getKind() == Tree.Kind.ENUM) {
				enums.add(member);
			}

			// Get enums recursively
			if (member instanceof ClassTree && member.getKind() == Tree.Kind.CLASS) {
				enums.addAll(getAllEnums((ClassTree) member));
			}
		}
		return enums;
	}

	public void generate(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		boolean outerClass = type.getEnclosingElement().getKind() == ElementKind.PACKAGE;

		// Move member enums to the top level
		if (outerClass) {
			List<Tree> enums = getAllEnums(tree);
			for (Tree member : enums) {
				generateEnum(visitor, (ClassTree) member, context, stmts);
			}
		}

		// Render top level enums correctly
		if (type.getKind() == ElementKind.ENUM && outerClass) {
			generateEnum(visitor, tree, context, stmts);
		}
	}

	public void generateEnum(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		if (type.getKind() != ElementKind.ENUM) {
			return;
		}

		// add all enum entries
		List<String> enumEntries = new ArrayList<>();
		for (Element member : ElementUtils.getAllFieldsIn((TypeElement) type)) {
			if (member.getKind() == ElementKind.ENUM_CONSTANT) {
				enumEntries.add(member.getSimpleName().toString());
			}
		}

		String typeName = context.getNames().getTypeName(context, type, DependencyType.EXTENDS);
		String originalTypeName = typeName;

		JavaScriptBuilder<JS> js = context.js();

		typeName = typeName.replace('.', '_');
		stmts.add(js.enumDeclaration(typeName, enumEntries));

		boolean outerClass = type.getEnclosingElement().getKind() == ElementKind.PACKAGE;
		if (outerClass && !typeName.equals(originalTypeName)) {
			stmts.add(js.assignment(
					AssignOperator.ASSIGN,
					js.name(originalTypeName),
					js.name(typeName)
			));
		}
	}

	public void generateReference(WriterVisitor<JS> visitor, ClassTree tree, GenerationContext<JS> context, List<JS> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		boolean innerClass = type.getEnclosingElement().getKind() != ElementKind.PACKAGE;

		if (!innerClass) {
			return;
		}

		String typeName = context.getNames().getTypeName(context, type, DependencyType.EXTENDS);

		// TODO :: change `leftSide` to `js.name(typeName.substring(pos))` once classes are implemented
		String leftSide = replaceFullNameWithConstructor(typeName);
		typeName = typeName.replace('.', '_');

		JavaScriptBuilder<JS> js = context.js();

		stmts.add(
			js.expressionStatement(
				js.assignment(
					AssignOperator.ASSIGN,
					js.name(leftSide),
					js.name(typeName)
				)
			)
		);
	}
}
