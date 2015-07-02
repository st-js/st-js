package org.stjs.testing.driver;

import java.util.concurrent.ConcurrentMap;

import org.junit.runners.model.TestClass;
import org.stjs.generator.ClassResolver;
import org.stjs.generator.DependencyCollector;

import com.google.common.collect.MapMaker;

public class TestClassAttributesRepository {

	private final ClassResolver classResolver;
	private final DependencyCollector dependencyCollector;

	// Junit doesn't notify us when a TestClass starts or finishes running. That means we can't easily clear unused
	// cache entries. We therefore use a Map implementation that has Weak keys, so that the map entry is removed when
	// the TestClass is garbage collected.
	private final ConcurrentMap<TestClass, TestClassAttributes> cache = new MapMaker().weakKeys().makeMap();

	public TestClassAttributesRepository(ClassResolver classResolver, DependencyCollector dependencyCollector) {
		this.classResolver = classResolver;
		this.dependencyCollector = dependencyCollector;
	}

	public TestClassAttributes getAttributes(TestClass testClass){
		TestClassAttributes attr = cache.get(testClass);
		if(attr == null){
			// not present yet, lets calculate it and put it in the cache
			attr = new TestClassAttributes(testClass, classResolver, dependencyCollector);
			TestClassAttributes existing = cache.putIfAbsent(testClass, attr);
			if(existing != null){
				// hmm... some other thread has put the value in the cache since we started calculating.
				// We lost the race and should use the version that's already in the cache
				return existing;
			}
		}
		return attr;
	}
}
