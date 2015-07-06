package org.stjs.testing.driver;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.TestClass;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.stjs.generator.ClassResolver;
import org.stjs.generator.DependencyCollector;
import org.stjs.testing.annotation.Scripts;
import org.stjs.testing.annotation.ScriptsAfter;
import org.stjs.testing.annotation.ScriptsBefore;

/**
 * Created by diermiichuk on 7/2/15.
 */
public class TestClassAttributesTest {

	private static final String PARENT_SCRIPT_1 = "classpath://js/parent1.js";
	private static final String PARENT_SCRIPT_2 = "classpath://js/parent2.js";
	private static final String PARENT_SCRIPT_BEFORE_1 = "classpath://js/parentBefore1.js";
	private static final String PARENT_SCRIPT_BEFORE_2 = "classpath://js/parentBefore2.js";
	private static final String PARENT_SCRIPT_AFTER_1 = "classpath://js/parentAfter1.js";
	private static final String PARENT_SCRIPT_AFTER_2 = "classpath://js/parentAfter2.js";

	private static final String CHILD_SCRIPT_1 = "classpath://js/child1.js";
	private static final String CHILD_SCRIPT_2 = "classpath://js/child2.js";
	private static final String CHILD_SCRIPT_BEFORE_1 = "classpath://js/childBefore1.js";
	private static final String CHILD_SCRIPT_BEFORE_2 = "classpath://js/childBefore2.js";
	private static final String CHILD_SCRIPT_AFTER_1 = "classpath://js/childAfter1.js";
	private static final String CHILD_SCRIPT_AFTER_2 = "classpath://js/childAfter2.js";

	@Mock
	ClassResolver classResolver;

	@Mock
	DependencyCollector dependencyCollector;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAnnotationWithNoInheritance() throws ClassNotFoundException {

		TestClass testClass = new TestClass(Parent.class);

		TestClassAttributes testClassAttributes  = new TestClassAttributes(testClass, classResolver, dependencyCollector);

		List<String> expectedScripts = Arrays.asList(PARENT_SCRIPT_1, PARENT_SCRIPT_2);
		List<String> expectedBefore = Arrays.asList(PARENT_SCRIPT_BEFORE_1, PARENT_SCRIPT_BEFORE_2);
		List<String> expectedAfter = Arrays.asList(PARENT_SCRIPT_AFTER_1, PARENT_SCRIPT_AFTER_2);

		Assert.assertEquals(expectedScripts.size(), testClassAttributes.getScripts().size());
		Assert.assertEquals(expectedBefore.size(), testClassAttributes.getScriptsBefore().size());
		Assert.assertEquals(expectedAfter.size(), testClassAttributes.getScriptsAfter().size());

		for (String script : testClassAttributes.getScripts()) {
			Assert.assertTrue(expectedScripts.contains(script));
		}

		for (String script : testClassAttributes.getScriptsBefore()) {
			Assert.assertTrue(expectedBefore.contains(script));
		}

		for (String script : testClassAttributes.getScriptsAfter()) {
			Assert.assertTrue(expectedAfter.contains(script));
		}
	}

	@Test
	public void testAnnotationWithInheritance() {
		TestClass testClass = new TestClass(Child.class);
		TestClassAttributes testClassAttributes  = new TestClassAttributes(testClass, classResolver, dependencyCollector);

		List<String> expectedScripts = Arrays.asList(PARENT_SCRIPT_1, PARENT_SCRIPT_2, CHILD_SCRIPT_1, CHILD_SCRIPT_2);
		List<String> expectedBefore = Arrays.asList(PARENT_SCRIPT_BEFORE_1, PARENT_SCRIPT_BEFORE_2, CHILD_SCRIPT_BEFORE_1, CHILD_SCRIPT_BEFORE_2);
		List<String> expectedAfter = Arrays.asList(PARENT_SCRIPT_AFTER_1, PARENT_SCRIPT_AFTER_2, CHILD_SCRIPT_AFTER_1, CHILD_SCRIPT_AFTER_2);

		Assert.assertEquals(expectedScripts.size(), testClassAttributes.getScripts().size());
		Assert.assertEquals(expectedBefore.size(), testClassAttributes.getScriptsBefore().size());
		Assert.assertEquals(expectedAfter.size(), testClassAttributes.getScriptsAfter().size());

		for (String script : testClassAttributes.getScripts()) {
			Assert.assertTrue(expectedScripts.contains(script));
		}

		for (String script : testClassAttributes.getScriptsBefore()) {
			Assert.assertTrue(expectedBefore.contains(script));
		}

		for (String script : testClassAttributes.getScriptsAfter()) {
			Assert.assertTrue(expectedAfter.contains(script));
		}
	}

	@Test
	public void testAnnotationOrderWithInheritance() {
		TestClass testClass = new TestClass(Child.class);
		TestClassAttributes testClassAttributes  = new TestClassAttributes(testClass, classResolver, dependencyCollector);

		List<String> expectedScripts = Arrays.asList(CHILD_SCRIPT_1, CHILD_SCRIPT_2, PARENT_SCRIPT_1, PARENT_SCRIPT_2);
		List<String> expectedBefore = Arrays.asList(CHILD_SCRIPT_BEFORE_1, CHILD_SCRIPT_BEFORE_2, PARENT_SCRIPT_BEFORE_1, PARENT_SCRIPT_BEFORE_2);
		List<String> expectedAfter = Arrays.asList(CHILD_SCRIPT_AFTER_1, CHILD_SCRIPT_AFTER_2, PARENT_SCRIPT_AFTER_1, PARENT_SCRIPT_AFTER_2);

		Assert.assertEquals(expectedScripts.size(), testClassAttributes.getScripts().size());
		Assert.assertEquals(expectedBefore.size(), testClassAttributes.getScriptsBefore().size());
		Assert.assertEquals(expectedAfter.size(), testClassAttributes.getScriptsAfter().size());

		for (int i = 0; i < 4; i++) {
			Assert.assertEquals(expectedScripts.get(i), testClassAttributes.getScripts().get(i));
			Assert.assertEquals(expectedBefore.get(i), testClassAttributes.getScriptsBefore().get(i));
			Assert.assertEquals(expectedAfter.get(i), testClassAttributes.getScriptsAfter().get(i));
		}
	}


	@Test
	public void testAnnotationOverridingOrderWithInheritance() {
		TestClass testClass = new TestClass(ChildWithSameScripts.class);
		TestClassAttributes testClassAttributes  = new TestClassAttributes(testClass, classResolver, dependencyCollector);

		List<String> expectedScripts = Arrays.asList(CHILD_SCRIPT_1, PARENT_SCRIPT_2, PARENT_SCRIPT_1);
		List<String> expectedBefore = Arrays.asList(CHILD_SCRIPT_BEFORE_1, PARENT_SCRIPT_BEFORE_2, PARENT_SCRIPT_BEFORE_1);
		List<String> expectedAfter = Arrays.asList(CHILD_SCRIPT_AFTER_1, PARENT_SCRIPT_AFTER_2, PARENT_SCRIPT_AFTER_1);

		Assert.assertEquals(expectedScripts.size(), testClassAttributes.getScripts().size());
		Assert.assertEquals(expectedBefore.size(), testClassAttributes.getScriptsBefore().size());
		Assert.assertEquals(expectedAfter.size(), testClassAttributes.getScriptsAfter().size());

		for (int i = 0; i < 3; i++) {
			Assert.assertEquals(expectedScripts.get(i), testClassAttributes.getScripts().get(i));
			Assert.assertEquals(expectedBefore.get(i), testClassAttributes.getScriptsBefore().get(i));
			Assert.assertEquals(expectedAfter.get(i), testClassAttributes.getScriptsAfter().get(i));
		}
	}

	@Test
	public void testAnnotationOverridingOrderWithThreeLevenInherit() {
		TestClass testClass = new TestClass(Son.class);
		TestClassAttributes testClassAttributes  = new TestClassAttributes(testClass, classResolver, dependencyCollector);

		List<String> expectedScripts = Arrays.asList(PARENT_SCRIPT_2, CHILD_SCRIPT_1, PARENT_SCRIPT_1);
		List<String> expectedBefore = Arrays.asList(CHILD_SCRIPT_BEFORE_1, CHILD_SCRIPT_BEFORE_2, PARENT_SCRIPT_BEFORE_2, PARENT_SCRIPT_BEFORE_1);
		List<String> expectedAfter = Arrays.asList(CHILD_SCRIPT_AFTER_1, CHILD_SCRIPT_AFTER_2, PARENT_SCRIPT_AFTER_2, PARENT_SCRIPT_AFTER_1);

		Assert.assertEquals(expectedScripts.size(), testClassAttributes.getScripts().size());
		Assert.assertEquals(expectedBefore.size(), testClassAttributes.getScriptsBefore().size());
		Assert.assertEquals(expectedAfter.size(), testClassAttributes.getScriptsAfter().size());

		for (int i = 0; i < expectedBefore.size(); i++) {
			Assert.assertEquals(expectedBefore.get(i), testClassAttributes.getScriptsBefore().get(i));
			Assert.assertEquals(expectedAfter.get(i), testClassAttributes.getScriptsAfter().get(i));
		}

		for (int i = 0; i < expectedScripts.size(); i++) {
			Assert.assertEquals(expectedScripts.get(i), testClassAttributes.getScripts().get(i));
		}
	}

	@Scripts({PARENT_SCRIPT_1, PARENT_SCRIPT_2})
	@ScriptsBefore({PARENT_SCRIPT_BEFORE_1, PARENT_SCRIPT_BEFORE_2})
	@ScriptsAfter({PARENT_SCRIPT_AFTER_1, PARENT_SCRIPT_AFTER_2})
	class Parent {}

	@Scripts({CHILD_SCRIPT_1, CHILD_SCRIPT_2})
	@ScriptsBefore({CHILD_SCRIPT_BEFORE_1, CHILD_SCRIPT_BEFORE_2})
	@ScriptsAfter({CHILD_SCRIPT_AFTER_1, CHILD_SCRIPT_AFTER_2})
	class Child extends Parent {}


	@Scripts({PARENT_SCRIPT_2, PARENT_SCRIPT_1})
	@ScriptsBefore({PARENT_SCRIPT_BEFORE_2, PARENT_SCRIPT_BEFORE_1})
	@ScriptsAfter({PARENT_SCRIPT_AFTER_2, PARENT_SCRIPT_AFTER_1})
	class ParentWithSameScripts {}

	@Scripts({CHILD_SCRIPT_1, PARENT_SCRIPT_2})
	@ScriptsBefore({CHILD_SCRIPT_BEFORE_1, PARENT_SCRIPT_BEFORE_2})
	@ScriptsAfter({CHILD_SCRIPT_AFTER_1, PARENT_SCRIPT_AFTER_2})
	class ChildWithSameScripts extends ParentWithSameScripts {}

	@Scripts({PARENT_SCRIPT_1})
	@ScriptsBefore({PARENT_SCRIPT_BEFORE_1})
	@ScriptsAfter({PARENT_SCRIPT_AFTER_1})
	class Grandpa {}

	@Scripts({PARENT_SCRIPT_2, CHILD_SCRIPT_1})
	@ScriptsBefore({PARENT_SCRIPT_BEFORE_2, CHILD_SCRIPT_BEFORE_1})
	@ScriptsAfter({PARENT_SCRIPT_AFTER_2, CHILD_SCRIPT_AFTER_1})
	class Father extends Grandpa {}

	@Scripts({})
	@ScriptsBefore({CHILD_SCRIPT_BEFORE_1, CHILD_SCRIPT_BEFORE_2})
	@ScriptsAfter({CHILD_SCRIPT_AFTER_1, CHILD_SCRIPT_AFTER_2})
	class Son extends Father {}
}
