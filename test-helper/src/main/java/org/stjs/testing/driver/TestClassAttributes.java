package org.stjs.testing.driver;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.stjs.generator.ClassResolver;
import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollector;
import org.stjs.testing.annotation.HTMLFixture;
import org.stjs.testing.annotation.Scripts;
import org.stjs.testing.annotation.ScriptsAfter;
import org.stjs.testing.annotation.ScriptsBefore;

public class TestClassAttributes {

	private final List<FrameworkMethod> beforeMethods;
	private final List<FrameworkMethod> afterMethods;

	private final HTMLFixture htmlFixture;

	private final Scripts scripts;
	private final ScriptsBefore scriptsBefore;
	private final ScriptsAfter scriptsAfter;

	private final ClassWithJavascript stjsClass;
	private final List<ClassWithJavascript> dependencies;

	public TestClassAttributes(TestClass testClass, ClassResolver classResolver, DependencyCollector dependencyCollector){
		beforeMethods = testClass.getAnnotatedMethods(Before.class);
		afterMethods = testClass.getAnnotatedMethods(After.class);
		stjsClass = classResolver.resolve(testClass.getName());

		htmlFixture = testClass.getJavaClass().getAnnotation(HTMLFixture.class);

		scripts = testClass.getJavaClass().getAnnotation(Scripts.class);
		scriptsBefore = testClass.getJavaClass().getAnnotation(ScriptsBefore.class);
		scriptsAfter = testClass.getJavaClass().getAnnotation(ScriptsAfter.class);

		dependencies = dependencyCollector.orderAllDependencies(stjsClass);
	}

	public Scripts getScripts() {
		return scripts;
	}

	public ScriptsAfter getScriptsAfter() {
		return scriptsAfter;
	}

	public ScriptsBefore getScriptsBefore() {
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
}
