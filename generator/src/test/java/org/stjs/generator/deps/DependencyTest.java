package org.stjs.generator.deps;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stjs.generator.DependencyCollection;

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

	@Test(expected = IllegalArgumentException.class)
	public void test5() {
		DependencyCollection.DEPENDENCY_COMPARATOR.compare(Err1.class, Err2.class);
	}
}
