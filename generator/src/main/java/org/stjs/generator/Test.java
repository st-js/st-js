package org.stjs.generator;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.google.common.base.Throwables;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

public class Test {
	static void compile(final List<File> sourceFiles, List<File> dependencies) {
		try {
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			fileManager.setLocation(StandardLocation.CLASS_PATH, dependencies);

			Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(sourceFiles);
			JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits1);
			JavacTask javacTask = (JavacTask) task;

			final Trees trees = Trees.instance(javacTask);
			final CompilationUnitTree cu = javacTask.parse().iterator().next();
			System.out.println(cu);
			javacTask.analyze();

			TreePathScanner<Void, Boolean> visitor = new TreePathScanner<Void, Boolean>() {
				@Override
				public Void visitMethodInvocation(MethodInvocationTree node, Boolean p) {
					// MemberSelectTree select = (MemberSelectTree) node.getMethodSelect();
					// System.out.println("SELECT:" + select.getIdentifier() + ", from " + select.getExpression());
					System.out.println(node);
					System.out.println("EL:" + trees.getElement(getCurrentPath()));
					System.out.println("TYPE:" + trees.getTypeMirror(getCurrentPath()));
					System.out.println("---");
					return super.visitMethodInvocation(node, p);
				}
			};
			visitor.scan(cu, null);
			fileManager.close();
		}
		catch (Exception e) {
			throw Throwables.propagate(e);
		}

	}

	public static void main(String[] args) {
		String f = "src/main/java/org/stjs/generator/Check.java";
		compile(Collections.singletonList(new File(f)), Collections.<File>emptyList());
	}
}
