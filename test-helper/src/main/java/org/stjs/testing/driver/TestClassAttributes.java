package org.stjs.testing.driver;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.stjs.generator.ClassResolver;
import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollector;
import org.stjs.javascript.functions.Function1;
import org.stjs.testing.annotation.HTMLFixture;
import org.stjs.testing.annotation.Scripts;
import org.stjs.testing.annotation.ScriptsAfter;
import org.stjs.testing.annotation.ScriptsBefore;

public class TestClassAttributes {

	private final List<FrameworkMethod> beforeMethods;
	private final List<FrameworkMethod> afterMethods;

	private final HTMLFixture htmlFixture;

	private final List<String> scripts;
	private final List<String> scriptsBefore;
	private final List<String> scriptsAfter;

	private final ClassWithJavascript stjsClass;
	private final List<ClassWithJavascript> dependencies;

	public TestClassAttributes(TestClass testClass, ClassResolver classResolver, DependencyCollector dependencyCollector) {
		beforeMethods = testClass.getAnnotatedMethods(Before.class);
		afterMethods = testClass.getAnnotatedMethods(After.class);
		stjsClass = classResolver.resolve(testClass.getName());

		htmlFixture = testClass.getJavaClass().getAnnotation(HTMLFixture.class);

		scripts = getScriptsPathes(testClass.getJavaClass(), Scripts.class, new Function1<Annotation, Collection<String>>() {
			@Override
			public Collection<String> $invoke(Annotation annotation) {
				return Arrays.asList(Scripts.class.cast(annotation).value());
			}
		});

		scriptsBefore = getScriptsPathes(testClass.getJavaClass(), ScriptsBefore.class, new Function1<Annotation, Collection<String>>() {
			@Override
			public Collection<String> $invoke(Annotation annotation) {
				return Arrays.asList(ScriptsBefore.class.cast(annotation).value());
			}
		});

		scriptsAfter = getScriptsPathes(testClass.getJavaClass(), ScriptsAfter.class, new Function1<Annotation, Collection<String>>() {
			@Override
			public Collection<String> $invoke(Annotation annotation) {
				return Arrays.asList(ScriptsAfter.class.cast(annotation).value());
			}
		});

		dependencies = dependencyCollector.orderAllDependencies(stjsClass);
	}

	/**
	 * @return scripts pathes or empty list
	 */
	public List<String> getScripts() {
		return scripts;
	}

	/**
	 * @return scripts pathes or empty list
	 */
	public List<String> getScriptsAfter() {
		return scriptsAfter;
	}

	/**
	 * @return scripts pathes or empty list
	 */
	public List<String> getScriptsBefore() {
		return scriptsBefore;
	}

	public List<FrameworkMethod> getAfterMethods() {
		return afterMethods;
	}

	public List<FrameworkMethod> getBeforeMethods() {
		return beforeMethods;
	}

	public List<ClassWithJavascript> getDependencies() {
		return dependencies;
	}

	public HTMLFixture getHtmlFixture() {
		return htmlFixture;
	}

	public ClassWithJavascript getStjsClass() {
		return stjsClass;
	}

	private List<String> getScriptsPathes(Class clazz, Class<? extends Annotation> anotClazz, Function1<Annotation, Collection<String>> caster) {
		LinkedList<Annotation> scriptsAnnots = accumulateAnnots(clazz, anotClazz, new LinkedList<Annotation>());
		Set<String> scripts = new LinkedHashSet<>();
		int scriptsAnnotsSize = scriptsAnnots.size();
		for (int i = 0; i < scriptsAnnotsSize; i++) {
			scripts.addAll(caster.$invoke(scriptsAnnots.removeLast()));
		}

		return new ArrayList<>(scripts);
	}

	private LinkedList<Annotation> accumulateAnnots(Class clazz, Class<? extends Annotation> annotation, LinkedList<Annotation> accum) {
		if (Object.class.equals(clazz)) {
			return accum;
		} else {
			Annotation annot = clazz.getAnnotation(annotation);
			if (annot != null) {
				accum.add(annot);
			}
			return accumulateAnnots(clazz.getSuperclass(), annotation, accum);
		}
	}
}
