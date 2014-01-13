package org.stjs.generator.deps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.stjs.generator.utils.GeneratorTestHelper.generate;
import static org.stjs.generator.utils.GeneratorTestHelper.stjsClass;

import java.util.List;

import org.junit.Test;
import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollection;
import org.stjs.javascript.JSGlobal;

public class DependencyTest {
	@Test
	public void test1() {
		assertEquals(-1, DependencyCollection.DEPENDENCY_COMPARATOR.compare(Dep1.class, Dep2.class));
		assertEquals(1, DependencyCollection.DEPENDENCY_COMPARATOR.compare(Dep2.class, Dep1.class));
	}

	@Test
	public void test2() {
		assertEquals(-1, DependencyCollection.DEPENDENCY_COMPARATOR.compare(Dep1.class, Dep3.class));
		assertEquals(1, DependencyCollection.DEPENDENCY_COMPARATOR.compare(Dep3.class, Dep1.class));
	}

	@Test
	public void test3() {
		assertEquals(-1, DependencyCollection.DEPENDENCY_COMPARATOR.compare(Dep1.class, Dep4.class));
		assertEquals(1, DependencyCollection.DEPENDENCY_COMPARATOR.compare(Dep4.class, Dep1.class));
	}

	@Test
	public void test4() {
		assertEquals(0, DependencyCollection.DEPENDENCY_COMPARATOR.compare(Dep3.class, Dep4.class));
		assertEquals(0, DependencyCollection.DEPENDENCY_COMPARATOR.compare(Dep4.class, Dep3.class));
	}

	@Test(
			expected = IllegalArgumentException.class)
	public void test5() {
		DependencyCollection.DEPENDENCY_COMPARATOR.compare(Err1.class, Err2.class);
	}

	@Test
	public void testGlobalScopeDep() {
		generate(Dep5.class);
		ClassWithJavascript jsClass = stjsClass(Dep5.class);

		assertNotNull(jsClass);
		assertTrue(!jsClass.getDirectDependencies().isEmpty());
		assertDependency(jsClass.getDirectDependencies(), JSGlobal.class);
	}

	private void assertDependency(List<ClassWithJavascript> directDependencies, Class<?> clz) {
		for (ClassWithJavascript c : directDependencies) {
			if (clz.getName().equals(c.getClassName())) {
				return;
			}
		}

		fail("Could not find the class:" + clz + " in the dependency list:" + directDependencies);
	}
}
