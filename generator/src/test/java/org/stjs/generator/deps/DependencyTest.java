package org.stjs.generator.deps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.stjs.generator.utils.AbstractStjsTest;
import org.stjs.generator.ClassWithJavascript;
import org.stjs.generator.DependencyCollection;
import org.stjs.generator.name.DependencyType;
import org.stjs.javascript.JSGlobal;

public class DependencyTest extends AbstractStjsTest {
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

	@Test(expected = IllegalArgumentException.class)
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

	@Test
	public void testExtendsDep() {
		generate(Dep6Parent.class);
		generate(Dep6Child.class);

		ClassWithJavascript jsClass = stjsClass(Dep6Child.class);

		assertNotNull(jsClass);
		assertDependency(jsClass.getDirectDependencyMap(), Dep6Parent.class, DependencyType.EXTENDS);
	}

	@Test
	public void testStaticDep() {
		generate(Dep7s.class);
		generate(Dep7.class);

		ClassWithJavascript jsClass = stjsClass(Dep7.class);

		assertNotNull(jsClass);
		assertDependency(jsClass.getDirectDependencyMap(), Dep7s.class, DependencyType.STATIC);
	}

	@Test
	public void testFieldStaticDep() {
		generate(Dep7s.class);
		generate(Dep7b.class);

		ClassWithJavascript jsClass = stjsClass(Dep7b.class);

		assertNotNull(jsClass);
		assertDependency(jsClass.getDirectDependencyMap(), Dep7s.class, DependencyType.STATIC);
	}

	@Test
	public void testNewDep() {
		generate(Dep7s.class);
		generate(Dep8.class);

		ClassWithJavascript jsClass = stjsClass(Dep8.class);

		assertNotNull(jsClass);
		assertDependency(jsClass.getDirectDependencyMap(), Dep7s.class, DependencyType.STATIC);
	}

	@Test
	public void testNewInterface() {
		generate(Dep9i.class);
		generate(Dep9.class);

		ClassWithJavascript jsClass = stjsClass(Dep9.class);

		assertNotNull(jsClass);
		assertDependency(jsClass.getDirectDependencyMap(), Dep9i.class, DependencyType.STATIC);
	}

	@Test
	public void testOtherDep() {
		generate(Dep10p.class);
		generate(Dep10.class);

		ClassWithJavascript jsClass = stjsClass(Dep10.class);

		assertNotNull(jsClass);
		assertDependency(jsClass.getDirectDependencyMap(), Dep10p.class, DependencyType.OTHER);
	}

	private void assertDependency(List<ClassWithJavascript> directDependencies, Class<?> clz) {
		for (ClassWithJavascript c : directDependencies) {
			if (clz.getName().equals(c.getJavaClassName())) {
				return;
			}
		}

		fail("Could not find the class:" + clz + " in the dependency list:" + directDependencies);
	}

	private void assertDependency(Map<ClassWithJavascript, DependencyType> directDependencies, Class<?> clz, DependencyType depType) {
		for (Map.Entry<ClassWithJavascript, DependencyType> entry : directDependencies.entrySet()) {
			if (clz.getName().equals(entry.getKey().getJavaClassName())) {
				if (depType != entry.getValue()) {
					assertEquals("Dependency for type :" + clz, depType, entry.getValue());
				}
				return;
			}
		}

		fail("Could not find the class:" + clz + " in the dependency list:" + directDependencies);
	}
}
